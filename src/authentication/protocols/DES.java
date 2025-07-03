package authentication.protocols;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

public class DES {

    public DES() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56);
    }

    static public String encrypt(String data, String customKey) throws Exception {
        SecretKey key = generateKeyFromString(customKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    static public String decrypt(String encryptedData,String customKey) throws Exception {
        SecretKey key = generateKeyFromString(customKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    // Utility method to generate a custom SecretKey from a string
    static public SecretKey generateKeyFromString(String keyString) {
        return new SecretKeySpec(keyString.getBytes(), "DES");
    }

}
