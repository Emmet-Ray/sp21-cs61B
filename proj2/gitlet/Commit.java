package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Repository.*;

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

    // second parent for merge commits
    private String secondParent = null;
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
     * get parent from HEAD
     * get parent's blobs from OBJECTS/*HEAD
     */
    public Commit(String message) {
        this.message = message;
        // current time
        this.date = new Date();
        String parent = readHeadCommit();
        this.parent = parent;

        File parentBlob = Utils.join(OBJECTS, parent);
        Commit parentCommit = Utils.readObject(parentBlob, Commit.class);
        this.blobs = new HashMap<>();
        // parent commit is not initial commit
        if (parentCommit.blobs != null) {
            for (String s : parentCommit.blobs.keySet()) {
                this.blobs.put(s, parentCommit.blobs.get(s));
            }
        }
    }

    public Commit(String message, String secondParent) {
        this.message = message;
        // current time
        this.date = new Date();
        String parent = readHeadCommit();
        this.parent = parent;
        this.secondParent = secondParent;

        File parentBlob = Utils.join(OBJECTS, parent);
        Commit parentCommit = Utils.readObject(parentBlob, Commit.class);
        this.blobs = new HashMap<>();
        // parent commit is not initial commit
        if (parentCommit.blobs != null) {
            for (String s : parentCommit.blobs.keySet()) {
                this.blobs.put(s, parentCommit.blobs.get(s));
            }
        }
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public String getParent() {
        return parent;
    }

    public String getSecondParent() {
        return secondParent;
    }

    public HashMap<String, String> getBlobs() {
        return blobs;
    }

    public String SHA_1() {
        //blobs == null i.e. if is initial commit
        if (blobs == null) {
            return Utils.sha1(message, date.toString(), parent);
        } else {
            return Utils.sha1(message, date.toString(), parent, blobs.toString());
        }
    }

    /**
     *   update the blobs according to staging for addition
     *   todo : & staging for removal
     *          need to modify the signature
     *
     *   todo : need to verify if this removal update is right
      * @param additionContent  the staging for addition files
     * @param removalContent the staging for removal files
     */
    public void updateBlobReferences(Map<String, String> additionContent, Set<String> removalContent) {
            for (String s : additionContent.keySet()) {
                    this.blobs.put(s, additionContent.get(s));
            }
            for (String s : removalContent) {
                this.blobs.remove(s);
            }
    }


}
