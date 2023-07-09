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
     * todo : the signature may be modified
     */
    public Commit(String message) {
        this.message = message;
        // current time
        this.date = new Date();
    }

    public String SHA_1() {
        //blobs == null i.e. if is initial commit
        if (blobs == null) {
            return Utils.sha1(message, date.toString(), parent);
        } else {
            return Utils.sha1(message, date.toString(), parent, blobs.toString());
        }
    }


}
