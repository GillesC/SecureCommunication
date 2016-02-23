import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64EncoderStream;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Gilles Callebaut on 23/02/2016.
 */
public class Main {
    public static void main(String[] args) {
        Channel unsecureChannel = new Channel();
        Person personA = new Person();
        Person personB = new Person();

        // GENERATING KEYPAIRS
        personA.generateKeyPair();
        personB.generateKeyPair();

        // EXCHANGE PUBLIC KEY
        // niet via unsecure channel
        personB.setPublicKeyOtherParty(personA.getPublicKey());
        personA.setPublicKeyOtherParty(personB.getPublicKey());

        // SEND OVER UNSECURE CHANNEL

        System.out.println("============================================================================================================== PERSON A =================================================================================================");


        Message msg = new Message("Some text");


        // PersonA ---- msg ----> PersonB
        //signing
        byte[] sig = personA.sign(msg);
        System.out.println("Generated Signature: "+ new String(BASE64EncoderStream.encode(sig)));
        //generating symmetric session key
        SecretKey sessionKeyA = personA.generateSessionKey();

        System.out.println("Generated Session key: "+ new String(BASE64EncoderStream.encode(sessionKeyA.getEncoded())));

        //encryptMessage messgae with sym. key
        // and store value in msg
        personA.encryptMessage(msg, sig, sessionKeyA);

        //encryptMessage session key with public key other party
        byte[] encryptedSessionKey = personA.encryptSessionKey(sessionKeyA);
        msg.setEncryptedSessionKey(encryptedSessionKey);

        msg.isEncrypted(true);


        // gewoon voor de schijn
        unsecureChannel.send(msg, personA, personB);

        unsecureChannel.listen();


        System.out.println("============================================================================================================== PERSON B =================================================================================================");

        // extract session key (asymmetric)
        byte[] sessionKeyB = personB.decryptEncryptedSessionKey(msg.getEncryptedSessionKey());

        System.out.println("Retrieved session key: "+ new String(BASE64EncoderStream.encode(sessionKeyB)));

        // use extracted sessionKey to decrypt message and sig (symmetric)

        personB.decryptMessage(msg, new SecretKeySpec(sessionKeyB, 0, sessionKeyB.length, Crypto.SYMMETRIC_ALGORITHM));

        msg.isEncrypted(false);

        System.out.println(msg.toString());

        System.out.println("Is message verified ? "+personB.verify(msg));


        // use sig to check integrety







    }
}
