//Having all of these methods in every single java file would be very tedious and unorganized,
//therefore we decided to implement a component specifically for invoking these methods.
package bankingapp.server;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class CryptoUtils {
    public static String encrypt(String plainText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(cipherBytes);
    }

    public static String decrypt(String cipherText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainBytes);
    }

    public static SecretKey getAESKeyFromString(String keyStr) {
        byte[] keyBytes = keyStr.getBytes();
        return new SecretKeySpec(keyBytes, 0, 16, "AES");
    }

    public static String generateNonce() {
        byte[] nonce = new byte[8];
        new SecureRandom().nextBytes(nonce);
        return Base64.getEncoder().encodeToString(nonce);
    }

    public static String generateMasterSecret() {
        byte[] key = new byte[16];
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public static SecretKey deriveKey(String masterSecret, String label) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(masterSecret), "HmacSHA256");
        mac.init(keySpec);
        byte[] result = mac.doFinal(label.getBytes());
        return new SecretKeySpec(result, 0, 16, "AES");
    }

    public static String generateHMAC(String data, SecretKey macKey) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(macKey);
        byte[] hmacBytes = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    public static boolean verifyHMAC(String data, String receivedHmac, SecretKey macKey) throws Exception {
        String computedHmac = generateHMAC(data, macKey);
        return computedHmac.equals(receivedHmac);
    }
}
