import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

/**
 * Klasse die een eigen keypair bevat en ook de publieke sleutel van de andere partij
 */
public class Person {
    private KeyPair myKeyPair;
    private PublicKey publicKeyOtherParty;


    /**************************** GENERATING KEYS *******************************/

    public void generateKeyPair() {
        myKeyPair = Crypto.generateKeyPair();
    }

    public PublicKey getPublicKey() {
        return myKeyPair.getPublic();
    }

    public void setPublicKeyOtherParty(PublicKey publicKeyOtherParty) {
        this.publicKeyOtherParty = publicKeyOtherParty;
    }


    public SecretKey generateSessionKey() {
        return Crypto.generateSymmetricKey();
    }


    /**************************** ENCRYPTION / DECRYPTION *******************************/

    public void encryptMessage(Message msg, byte[] sig, SecretKey sessionKey) {
        try {
            Cipher c = Cipher.getInstance(Crypto.SYMMETRIC_ALGORITHM);
            //content
            byte[] encryptedContent = Crypto.encrypt(msg.getContent().getBytes(), sessionKey, c);
            msg.setEncryptedContent(encryptedContent);

            // disgest
            byte[] encryptedSig = Crypto.encrypt(sig, sessionKey, c);
            msg.setEncryptedSig(encryptedSig);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }

    public byte[] encryptSessionKey(SecretKey sessionKey) {
        try {
            Cipher c = Cipher.getInstance(Crypto.ASYMMETRIC_ALGORITHM);

            return Crypto.encrypt(sessionKey.getEncoded(), publicKeyOtherParty, c);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decryptEncryptedSessionKey(byte[] encryptedSessionKey) {
        try {
            Cipher c = Cipher.getInstance(Crypto.ASYMMETRIC_ALGORITHM);
            return Crypto.decrypt(encryptedSessionKey, this.myKeyPair.getPrivate(), c);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void decryptMessage(Message msg, SecretKey secretKey) {
        try {
            Cipher c = Cipher.getInstance(Crypto.SYMMETRIC_ALGORITHM);
            //content
            byte[] decryptedContent = Crypto.decrypt(msg.getEncryptedContent(), secretKey, c);
            msg.setContent(new String(decryptedContent, "UTF-8"));

            // disgest
            byte[] decryptedSig= Crypto.decrypt(msg.getEncryptedSig(), secretKey, c);
            msg.setSig(decryptedSig);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**************************** SIGNATURE *******************************/

    public byte[] sign(Message msg) {
        String content = msg.getContent();
        byte[] digest = Crypto.digest(content);
        byte[] sig = Crypto.sign(digest, myKeyPair.getPrivate());
        return sig;
    }

    public boolean verify(Message msg) {
        byte[] digest = Crypto.digest(msg.getContent());
        return Crypto.verify(digest, publicKeyOtherParty, msg.getSig());
    }
}
