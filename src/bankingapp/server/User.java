
package bankingapp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.crypto.SecretKey;



public class User {
    public String username;
    public String password; 
    public BufferedReader in;
    public PrintWriter out;
    public Socket socket;
    private static final String PRE_SHARED_KEY = "thisisasecretkey";
    private String masterSecret;
    private SecretKey kEnc;
    private SecretKey kMac ;
    private static User instance;
    
    private User() throws IOException{
        this.socket = new Socket("localhost", 5000);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }
  
    public static User getInstance() throws IOException{
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
    
    public void logout(){
        instance=null;
    }
   
    public void register(String username, String password){
        out.println("1");               
        this.out.println(username+","+password);
    }
    
    public boolean login(String username, String password) throws Exception{
            out.println("0");
            SecretKey preSharedKey = CryptoUtils.getAESKeyFromString(PRE_SHARED_KEY);
            String nonce1 = CryptoUtils.generateNonce();
            String loginPayload = username + ":" + password + ":" + nonce1;
            this.out.println(CryptoUtils.encrypt(loginPayload, preSharedKey));
            String encryptedResponse = this.in.readLine();  
            if (encryptedResponse.equals("AUTH_FAILED")) {
                System.out.println("Authentication failed: Invalid credentials.");
                return false;
            }
            String[] parts = CryptoUtils.decrypt(encryptedResponse, preSharedKey).split(":");
            String echoedNonce1 = parts[0];
            String nonce2 = parts[1];
            this.masterSecret = parts[2];
            //Check if received Nonce is the same as the sent Nonce. If not, it is an attacker.
            if (!echoedNonce1.equals(nonce1)) {
                System.out.println("Authentication failed. Server sent wrong nonce.");
                return false;
            }
            //Check if received Nonce is the same as the sent Nonce. If not, it is an attacker.
            out.println(CryptoUtils.encrypt(nonce2, preSharedKey));
            String finalAuth = in.readLine();
            if (!finalAuth.equals("AUTH_SUCCESS")) {
                System.out.println("Authentication failed.");
                return false;
            }
        System.out.println("Authentication successful!");    
        this.kEnc = CryptoUtils.deriveKey(masterSecret, "encryption");
        this.kMac = CryptoUtils.deriveKey(masterSecret, "mac");
        this.username =username;
        this.password = password;
        return true;   
    }
     
    public String action(String command) throws Exception{
//        System.out.print("Enter command (deposit <amt>/withdraw <amt>/balance/exit): ");
        if (command.equals("exit")) return null;
        String mac = CryptoUtils.generateHMAC(command, this.kMac);
        String encryptedCommand = CryptoUtils.encrypt(command + ":" + mac, this.kEnc);
        this.out.println(encryptedCommand);
        String encryptedReply = this.in.readLine();
        String decryptedReply = CryptoUtils.decrypt(encryptedReply, kEnc);
        int sepIndex = decryptedReply.lastIndexOf(':');
        if (sepIndex == -1) {
            System.out.println("Bank> Invalid reply format.");
        }
        String reply = decryptedReply.substring(0, sepIndex);
        String receivedMac = decryptedReply.substring(sepIndex + 1);
        if (CryptoUtils.verifyHMAC(reply, receivedMac, kMac)) {
            System.out.println("Bank> " + reply);
        } else {
            System.out.println("Bank> Message authentication failed.");
        }  
        return reply;
    }
}
