package bankingapp.server;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ATMClient {
    private static final String PRE_SHARED_KEY = "thisisasecretkey";

    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 5000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            SecretKey preSharedKey = CryptoUtils.getAESKeyFromString(PRE_SHARED_KEY);
            
            //Prompt user for username and password
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            String nonce1 = CryptoUtils.generateNonce();

            //Send the login info to Server to get authenticated.
            String loginPayload = username + ":" + password + ":" + nonce1;
            out.println(CryptoUtils.encrypt(loginPayload, preSharedKey));

            String encryptedResponse = in.readLine();
            if (encryptedResponse.equals("AUTH_FAILED")) {
                System.out.println("Authentication failed: Invalid credentials.");
                return;
            }

            String[] parts = CryptoUtils.decrypt(encryptedResponse, preSharedKey).split(":");
            String echoedNonce1 = parts[0];
            String nonce2 = parts[1];
            String masterSecret = parts[2];
            
            //Check if received Nonce is the same as the sent Nonce. If not, it is an attacker.
            if (!echoedNonce1.equals(nonce1)) {
                System.out.println("Authentication failed. Server sent wrong nonce.");
                return;
            }
            
            //Check if received Nonce is the same as the sent Nonce. If not, it is an attacker.
            out.println(CryptoUtils.encrypt(nonce2, preSharedKey));
            String finalAuth = in.readLine();
            if (!finalAuth.equals("AUTH_SUCCESS")) {
                System.out.println("Authentication failed.");
                return;
            }

            //Derive the two Encryption and MAC symmetric keys from shared key.
            System.out.println("Authentication successful!");
            SecretKey kEnc = CryptoUtils.deriveKey(masterSecret, "encryption");
            SecretKey kMac = CryptoUtils.deriveKey(masterSecret, "mac");

            //This loop takes commands from the user and sends them to the Server for processing.
            while (true) {
                System.out.print("Enter command (deposit <amt>/withdraw <amt>/balance/exit): ");
                String command = scanner.nextLine();
                if (command.equals("exit")) break;

                String mac = CryptoUtils.generateHMAC(command, kMac);
                String encryptedCommand = CryptoUtils.encrypt(command + ":" + mac, kEnc);
                out.println(encryptedCommand);

                String encryptedReply = in.readLine();
                String decryptedReply = CryptoUtils.decrypt(encryptedReply, kEnc);
                int sepIndex = decryptedReply.lastIndexOf(':');
                if (sepIndex == -1) {
                    System.out.println("Bank> Invalid reply format.");
                    continue;
                }

                String reply = decryptedReply.substring(0, sepIndex);
                String receivedMac = decryptedReply.substring(sepIndex + 1);

                if (CryptoUtils.verifyHMAC(reply, receivedMac, kMac)) {
                    System.out.println("Bank> " + reply);
                } else {
                    System.out.println("Bank> Message authentication failed.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}