import javax.crypto.*;
import java.security.*;

import static javax.crypto.Cipher.ENCRYPT_MODE;

/**
 * Created by Gilles Callebaut on 23/02/2016.
 */
public class Crypto {
    public static final String DIGEST_ALGORITHM = "SHA-256";
    public static final String SIGN_ALGORITHM = "SHA256withRSA";
    public static final String ASYMMETRIC_ALGORITHM = "RSA";
    public static final String SYMMETRIC_ALGORITHM = "AES";



    /**************************** GENERATING KEYS *******************************/


    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ASYMMETRIC_ALGORITHM);
            keyGen.initialize(1024);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKey generateSymmetricKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(SYMMETRIC_ALGORITHM);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**************************** ENCRYPTION / DECRYPTION *******************************/

    public static byte[] encrypt(byte[] encryptMe, Key key, Cipher c) {
        try {
            c.init(ENCRYPT_MODE, key);
            return c.doFinal(encryptMe);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] decryptMe, Key key, Cipher c) {
        try {
            byte[] d = decryptMe;
            c.init(Cipher.DECRYPT_MODE, key);
            return c.doFinal(d);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**************************** SIGNATURE / DIGEST *******************************/

    public static byte[] digest(String content) {
        MessageDigest md;
        byte[] digest;
        try {
            md = MessageDigest.getInstance(DIGEST_ALGORITHM);
            md.update(content.getBytes());
            digest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }
        return digest;
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) {
        Signature signer;
        try {
            signer = Signature.getInstance(SIGN_ALGORITHM);
            signer.initSign(privateKey);
            signer.update(data);
            return (signer.sign());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean verify(byte[] data, PublicKey publicKey, byte[] sign) {
        Signature signer;
        try {
            signer = Signature.getInstance(SIGN_ALGORITHM);
            signer.initVerify(publicKey);
            signer.update(data);
            return (signer.verify(sign));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }
}
