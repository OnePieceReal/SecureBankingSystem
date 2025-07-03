
package authentication.protocols;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

public class Nonce {
    public static String generateTimestampNonce() {
        return String.valueOf(Instant.now().toEpochMilli());
    }

    public static String generateRandomNonce() {
        SecureRandom random = new SecureRandom();
        byte[] nonce = new byte[8];
        random.nextBytes(nonce);
        return Base64.getEncoder().encodeToString(nonce);
    }
    
//    public static void main(String args[]){
//        System.out.println(generateTimestampNonce());
//    }
}
