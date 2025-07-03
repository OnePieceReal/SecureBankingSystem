package bankingapp.server;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

//We decided to implement the Client Handler for the Bank Server as a separate component
//because it made developing the program much easier. This way, everything is much more
//organized and easier to understand.
public class ClientHandler implements Runnable {
    private Socket socket;
    private static final String PRE_SHARED_KEY = "thisisasecretkey";
    private static final Map<String, String> userCredentials = new HashMap<>();
    private static final Map<String, Integer> accountBalances = new HashMap<>();
    private String currentUser;
    private String masterSecret;

    static {
        userCredentials.put("alice", "password123");
        userCredentials.put("bob", "securepass");
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    //Method to log the actions taken by users. The information is encrypted so that
    //attackers cannot ascertain the information.
    private void logAudit(String username, String action) {
        try (FileWriter fw = new FileWriter("audit_log.txt", true)) {
            String timestamp = LocalDateTime.now().toString();
            String entry = username + " | " + action + " | " + timestamp + "\n";
            SecretKey logKey = CryptoUtils.getAESKeyFromString("logfileencryption!");
            String encryptedEntry = CryptoUtils.encrypt(entry, logKey);
            fw.write(encryptedEntry + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            SecretKey kEnc;
            SecretKey kMac;
            String test = in.readLine();
            if(test.equals("1")){
            String registration = in.readLine();
            String[] msgparts = registration.split(",");
            String uname = msgparts[0];
            String pswd = msgparts[1];
            userCredentials.put(uname,pswd);}
            while(true){
            SecretKey preSharedKey = CryptoUtils.getAESKeyFromString(PRE_SHARED_KEY);

            String encryptedMessage = in.readLine();
            String[] parts = CryptoUtils.decrypt(encryptedMessage, preSharedKey).split(":");
            String username = parts[0];
            String password = parts[1];
            String nonce1 = parts[2];
            
            //Check if the password entered by user matches the password known by the Server.
            //This authenticates the user to the Server.
            String storedPassword = userCredentials.get(username);
            if (storedPassword == null || !storedPassword.equals(password)) {
                out.println("AUTH_FAILED");
                continue;
            }

            //Use Nonces to authenticate the Server to the Client. In this implementation,
            //we decided to randomly generate Nonces instead of hard coding them.
            currentUser = username;
            String nonce2 = CryptoUtils.generateNonce();
            masterSecret = CryptoUtils.generateMasterSecret();
            String response = nonce1 + ":" + nonce2 + ":" + masterSecret;
            out.println(CryptoUtils.encrypt(response, preSharedKey));

            //Check if Nonce received is the same.
            String receivedNonce2 = CryptoUtils.decrypt(in.readLine(), preSharedKey);
            if (!receivedNonce2.equals(nonce2)) {
                out.println("AUTH_FAILED");
                continue;
            }

            //Notify the Client that authentication was successful.
            out.println("AUTH_SUCCESS");
            accountBalances.putIfAbsent(currentUser, 1000);

            //Derive the encryption and MAC symmetric keys from the shared key.
            //This fulfills the final component of the symmetric key requirements.
             kEnc = CryptoUtils.deriveKey(masterSecret, "encryption");
             kMac = CryptoUtils.deriveKey(masterSecret, "mac");
            break;
            }
            //This loop is indefinite until the user enters 'exit'.
            //This loop accepts banking commands from the Client and executes instructions accordingly.
            while (true) {
                String encryptedCommand = in.readLine();
                if (encryptedCommand == null) break;

                String[] cmdParts = CryptoUtils.decrypt(encryptedCommand, kEnc).split(":");
                String command = cmdParts[0];
                String receivedMac = cmdParts[1];

                //If the MAC is not verified, that means an attacker is trying to access the Bank; end connection.
                if (!CryptoUtils.verifyHMAC(command, receivedMac, kMac)) {
                    out.println(CryptoUtils.encrypt("Invalid MAC:FAIL", kEnc));
                    continue;
                }

                //Process the user command and log it to the output file.
                String reply = processCommand(command);
                logAudit(currentUser, command);
                String mac = CryptoUtils.generateHMAC(reply, kMac);
                out.println(CryptoUtils.encrypt(reply + ":" + mac, kEnc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to process the user commands. The user can Deposit and Withdraw money, display their Balance, and Exit.
    private String processCommand(String command) {
    int balance = accountBalances.get(currentUser);

    if (command.startsWith("deposit")) {
        try {
            int amount = Integer.parseInt(command.split(" ")[1]);
            balance += amount;
            accountBalances.put(currentUser, balance);
            return String.valueOf(balance);
        } catch (Exception e) {
            return "failed";
        }
    } else if (command.startsWith("withdraw")) {
        try {
            int amount = Integer.parseInt(command.split(" ")[1]);
            if (amount > balance) {
                return "failed";
            }
            balance -= amount;
            accountBalances.put(currentUser, balance);
            return String.valueOf(balance);
        } catch (Exception e) {
            return "failed";
        }
    } else if (command.equals("balance")) {
        return String.valueOf(balance);
    } else {
        return "failed";
    }
}
}