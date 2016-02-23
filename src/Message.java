import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64DecoderStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64EncoderStream;

import java.io.UnsupportedEncodingException;

/**
 * Klasse die een bericht nabootst
 * Methoden hierin zijn cryptografische methoden die het bericht kunnen signen, encrypteren en valideren.
 */
public class Message {

    private boolean encrypted = false;

    private byte[] encryptedSessionKey;
    private byte[] encryptedContent;
    private byte[] encryptedSig;

    private String content;
    private byte[] sig;



    public Message(String content) {
        this.content = content;
        System.out.println("MESSAGE MADE \""+content+"\"");
    }

    public String getContent() {
        return content;
    }

    public void clearContent() {
        content = null;
    }

    public void setEncryptedContent(byte[] encryptedContent) {
        this.encryptedContent = encryptedContent;
        clearContent();
    }

    public void setEncryptedSig(byte[] encryptedSig) {
        this.encryptedSig = encryptedSig;
    }

    public void setEncryptedSessionKey(byte[] encryptedSessionKey) {
        this.encryptedSessionKey = encryptedSessionKey;
    }

    public String toString()
    {
        String stringMessage = "";
        try {

        if(encrypted){
            stringMessage = "---------------------- ENCRYPTED MESSAGE --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            stringMessage += ("SessionKey:      " + new String(BASE64EncoderStream.encode(encryptedSessionKey))+"\n");
            stringMessage += ("Content:         " + new String(BASE64EncoderStream.encode(encryptedContent)) +"\n");
            stringMessage += ("Sig:             " + new String(BASE64EncoderStream.encode(encryptedSig)) + "\n");
        }else {
            stringMessage = "---------------------- MESSAGE ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            stringMessage += ("Content:         " + this.content + "\n");

            stringMessage += ("Sig:             " + new String(BASE64EncoderStream.encode(this.sig), "UTF-8") + "\n");
        }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        stringMessage += "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";

        return stringMessage;
    }

    public byte[] getEncryptedSessionKey() {
        return encryptedSessionKey;
    }

    public byte[] getEncryptedContent() {
        return encryptedContent;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getEncryptedSig() {
        return encryptedSig;
    }

    public void setSig(byte[] sig) {
        this.sig = sig;
    }

    public void isEncrypted(boolean b) {
        encrypted = b;
    }

    public byte[] getSig() {
        return sig;
    }
}
