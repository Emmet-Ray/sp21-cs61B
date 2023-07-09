package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author mengtaoli
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    private Date date;

    private String parent;

    private HashMap<String, String> blobs;
    /**
     *  for initial commit
     */
    public Commit() {
        this.message = "initial commit";
        // the epoch : 00:00:00 UTC, Thursday, 1 January 1970
        this.date = new Date(0);
        this.parent = "";
        this.blobs = null;
    }

    /**
     * new commit
     *
     */
    public Commit(String message) {
        this.message = message;
        // current time
        this.date = new Date();
    }

    public String SHA_1() {
        return get_SHA1();
    }
    private String get_SHA1() {
        String input = message + date.toString() + parent;
        if (blobs != null) {
            input += blobs.toString();
        }
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
