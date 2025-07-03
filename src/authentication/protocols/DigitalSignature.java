package authentication.protocols;

import java.security.*;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class DigitalSignature {
    
    private static Set<String> usedNonces = new HashSet<>(); // Prevent replay attacks


    public static String signMessage(String message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }

    public static boolean verifySignature(String message, String signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(message.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return sig.verify(signatureBytes);
    }

    public static void verifyReceivedMessage(String message, String nonce, String signature, PublicKey senderPublicKey) {
       // System.out.println("\nBob's Verification Process:");
        if (usedNonces.contains(nonce)) {
            System.out.println("️Replay Attack Detected!");
            return;
        }
        boolean isVerified;
        try {
            isVerified = verifySignature(message + "," + nonce, signature, senderPublicKey);
        } catch (Exception e) {
            System.out.println("Error verifying signature: " + e.getMessage());
            return;
        }
        if (isVerified) {
            System.out.println("Signature Verified: Message is authentic.");
            usedNonces.add(nonce);
        } else {
            System.out.println("Signature Verification Failed: Message is invalid.");
        }
        System.out.println();
    }

    
}
