package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *       does at a high level.
 *
 *  @author mengtaoli
 *
 * The structure of a .gitlet repository is as follows :
 *  .gitlet/ --
 *      - objects / -- a directory that includes files including : commits
 *      - blobs / -- a directory that includes blobs (files)
 *      - HEAD / -- a file that points to the current commit
 *      - master / -- a file that represents the initial branch, also points to the current commit
 *      - stagingForAddition / -- a file that represents the staging area for addition, contains all the addition staging info.
 *      - removal / -- a file for staging for removal
 *      // TODO : to be continued...
 *
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    static final File GITLET_DIR = join(CWD, ".gitlet");
    //  .gitlet/objects  directory
    static final File OBJECTS = join(GITLET_DIR, "objects");

    static final File BLOBS = join(GITLET_DIR, "blobs");
    static final File MASTER = join(GITLET_DIR, "master");

    static final File HEAD = join(GITLET_DIR, "HEAD");

    static final File STAGING_FOR_ADDITION = join(GITLET_DIR, "stagingForAddition");

    static final File STAGING_FOR_REMOVAL = join(GITLET_DIR, "removal");
    // <file name, SHA-1 code of the content of the file>
    private static HashMap<String, String> additionContent = new HashMap<>();

    private static HashSet<String> removalContent = new HashSet<>();
    // todo : maybe Map<String, String> additionContent = new HashMap<>(); ?

    /**
     * todo : setup of .gitlet and some internal structure of .gitlet
     *
     *  .gitlet/ --
     *      - objects / -- a directory that includes files including : commits, blobs(files)
     *      - HEAD / -- a file that points to the current commit
     *      - master / -- a file that represents the initial branch, also points to the current commit
     *      - stagingForAddition / -- a file that represents the staging area for addition, contains all the addition staging info.
     *      - removal / -- a file that represents the staging area for removal
     *      // TODO : to be continued...
     */
    public static void setUpGitlet() throws IOException {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        }
        if (!OBJECTS.exists()) {
            OBJECTS.mkdir();
        }
        if (!BLOBS.exists()) {
            BLOBS.mkdir();
        }
        if (!HEAD.exists()) {
            HEAD.createNewFile();
        }
        if (!MASTER.exists()) {
            MASTER.createNewFile();
        }
        if (!STAGING_FOR_ADDITION.exists()) {
            STAGING_FOR_ADDITION.createNewFile();
            // init with the initial additionContent HashMap
            Utils.writeObject(STAGING_FOR_ADDITION, additionContent);
        }
        if (!STAGING_FOR_REMOVAL.exists()) {
            STAGING_FOR_REMOVAL.createNewFile();

            Utils.writeObject(STAGING_FOR_REMOVAL, removalContent);
        }
    }

    /**
     * todo : need to VERIFY this 100% worked, so I reserve these TODOs.
     *
     *
     * todo : Creates a new Gitlet version-control system in the current directory
     * todo : initial commit with commit message and date, no tracking files.
     * todo : a single branch : master, initially points to the initial commit
     *       i guess the branch is a pointer i.e. it is the hash code of the commit it points to.
     * todo : what is UID ?
     */
    public static void init() throws IOException {
        // failure case
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        // set up version control system in CWD
        setUpGitlet();
        // create initial commit with its SHA-1 ID, store it in .gitlet/objects
        Commit initialCommit = new Commit();
        File initial = join(OBJECTS, initialCommit.SHA_1());
        if (!initial.exists()) {
            initial.createNewFile();
        }
        Utils.writeObject(initial, initialCommit);
        // both HEAD and master point to the initial commit i.e. contain its SHA-1 ID
        changeHeadMaster(initialCommit.SHA_1());
    }

    /**
     * todo : Adds a copy of the file as it currently exists to the staging area
     *         (see the description of the commit command)
     *         adding a file is also called staging the file for addition
     *   what is "staging area" ? where is it ?
     *   staged for addition, staged for removal ?
     *   file : .gitlet/stagingForAdition
     *
     * todo : Staging an already-staged file overwrites
     *          the previous entry in the staging area with the new contents
     *
     * todo : If the current working version of the file is identical to the version in the current commit,
     *      do not stage it to be added, and remove it from the staging area if it is already there
     *      (as can happen when a file is changed, added, and then changed back to it’s original version)
     *   how i know "identical" ? i guess using hash code
     *   what is "the current commit" ? i guess some commit that pointer like HEAD points to ?
     *
     *
     * todo : Runtime: In the worst case,
     *          should run in linear time relative to the size of the file being added
     *          and lgN, for N the number of files in the commit
     * @param file the file name to be added
     */
    public static void add(String file) throws IOException {
        // failure case : file does not exist
        File stagingFile = join(CWD, file);
        if (!stagingFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        // get the SHA-1 code of the file content of CWD version
        File addedFile = join(CWD, file);
        String hash_addedFile = Utils.sha1(Files.readString(addedFile.toPath()));

        // todo : read the current commit from HEAD
        //         1. see if the file is already there
        //          2. if it is there && the current commit version & the current working version are the same, do not stage the file
        //          3. remove the file from the staging area if it is already there


        /**  read from the staging area */
        additionContent = (HashMap<String, String>) Utils.readObject(STAGING_FOR_ADDITION, HashMap.class);
        // if already staged the file,
        // 1. compare the content between the already staged one and current working version
        if (additionContent.containsKey(file)) {
            String old_sha_1 = additionContent.get(file);
            if (old_sha_1.equals(hash_addedFile)) {
                return;
            }
            /** if the CWD version is newer, remove the old one from the staging area*/
            /** todo : do I need to remove the old blob in .gitlet/objects ????
             *       i guess so.
             *       verify this
             */
            additionContent.remove(file);
            File oldFile = join(BLOBS, old_sha_1);
            if (!Utils.restrictedDelete(oldFile)) {
                System.out.println("delete failed");
                System.exit(0);
            }
        }

        // create blob with its sha-1 id in .gitlet/blobs, and write current state of the file to the blob
        File hash_file = join(BLOBS, hash_addedFile);
        if (!hash_file.exists()) {
            hash_file.createNewFile();
            // todo : need to verify this write
            Utils.writeObject(hash_file, addedFile);
        }
        // put the new mapping into staging addition area
        additionContent.put(file, hash_addedFile);
        // write to the staging area
        Utils.writeObject(STAGING_FOR_ADDITION, additionContent);
    }

    /**
     * todo : Saves a snapshot of tracked files in the current commit
     *      and staging area so they can be restored at a later time,
     *      creating a new commit
     *   the new commit combines "the current commit" and "staging area"
     *
     * todo :
     *      1. By default, each commit’s snapshot of files will be exactly the same as
     *      its parent commit’s snapshot of files
     *      2. A commit will only update the contents of files it is tracking
     *      that have been staged for addition at the time of commit,
     *      in which case the commit will now include the version of the file that was staged
     *      instead of the version it got from its parent
     *      A commit will save and start tracking any files
     *      that were staged for addition but weren’t tracked by its parent.
     *      3. Finally, files tracked in the current commit may be untracked in the new commit
     *      as a result being staged for removal by the rm command (below
     *      Summmary :
     *          The bottom line: By default a commit has the same file contents as its parent.
     *          Files staged for addition and removal are the updates to the commit.
     *          Of course, the date (and likely the mesage) will also different from the parent.
     *
     * todo :
     *      The staging area is cleared after a commit.
     *
     * todo :
     *      The commit command never adds, changes, or removes files in the working directory
     *      (other than those in the .gitlet directory)
     *
     * todo :
     *      After the commit command, the new commit is added as a new node in the commit tree.
     *
     * todo :
     *      The commit just made becomes the “current commit”, and the head pointer now points to it.
     *      The previous head commit is this commit’s parent commit.
     *
     * todo :
     *      Each commit is identified by its SHA-1 id, which must include
     *      1. the file (blob) references of its files,
     *      2. parent reference,
     *      3. log message,
     *      4. and commit time.
     *
     * todo : failure cases
     *      1. If no files have been staged, abort.
     *      Print the message No changes added to the commit.
     *      2. Every commit must have a non-blank message.
     *      If it doesn’t, print the error message Please enter a commit message.
     * @param message the commit message
     */
    public static void commit(String message) throws IOException {
        /** failure cases */
        additionContent = (HashMap<String, String>) Utils.readObject(STAGING_FOR_ADDITION, HashMap.class);
        if (additionContent.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        Commit newCommit = new Commit(message);
        newCommit.updateBlobReferences(additionContent);

        /** works after the commit */
        //clear staging area
        additionContent.clear();
        Utils.writeObject(STAGING_FOR_ADDITION, additionContent);
        // todo : removal staging area
        // "move" HEAD pointer & MASTER
        changeHeadMaster(newCommit.SHA_1());
        // create the commit in OBJECTS
        File newCommitBlob = join(OBJECTS, newCommit.SHA_1());
        newCommitBlob.createNewFile();
        Utils.writeObject(newCommitBlob, newCommit);
    }

    /**
     *  todo : Unstage the file if it is currently staged for addition
     *
     *  todo :
     *      If the file is tracked in the current commit
     *      stage it for removal and remove the file from the working directory
     *      if the user has not already done
     *      so (do not remove it unless it is tracked in the current commit).
     *
     *  todo :
     *      If the file is neither staged nor tracked by the head commit,
     *      print the error message No reason to remove the file.
     * @param file
     */
    public static void rm(String file) {
        // flag used to indicate whether it has reason to remove the file
        boolean flag = false;
        additionContent = readObject(STAGING_FOR_ADDITION, HashMap.class);
        if (additionContent.containsKey(file)) {
            flag = true;
            additionContent.remove(file);
            writeObject(STAGING_FOR_ADDITION, additionContent);
        }

        String head = readContentsAsString(HEAD);
        Commit currentCommit = readObject(join(OBJECTS, head), Commit.class);
        HashMap<String, String> currentBolbs = currentCommit.getBlobs();
        if (currentBolbs != null && currentBolbs.containsKey(file)) {
            flag = true;
            // stage it for removal
            removalContent = readObject(STAGING_FOR_REMOVAL, HashSet.class);
            removalContent.add(file);
            writeObject(STAGING_FOR_REMOVAL, removalContent);
            // delete it
            Utils.restrictedDelete(join(CWD, file));
        }

        if (!flag) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    /**
     * todo :
     *      Starting at the current head commit,
     *      display information about each commit backwards along the commit tree
     *      until the initial commit,
     *      following the first parent commit links,
     *      ignoring any second parents found in merge commits
     *
     *  todo :
     *      the format is in the spec
     *
     *  todo :
     *      hint :
     *      By the way, you’ll find that the Java classes java.util.Date and java.util.Formatter
     *      are useful for getting and formatting times
     *
     *  todo :
     *      For merge commits
     */
    public static void log() {
        String p = readContentsAsString(HEAD);
        Commit pCommit = readObject(join(OBJECTS, p), Commit.class);
        // get this from chat GPT
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM d HH:mm:ss yyyy Z", Locale.US);
        // while pCommit is not initial commit
        while (pCommit.getBlobs() != null) {

            // todo : merge commit
            System.out.println("===");
            System.out.println("commit " + pCommit.SHA_1());
            System.out.println("Date: " + sdf.format(pCommit.getDate()));
            System.out.println(pCommit.getMessage());
            System.out.println();

            p = pCommit.getParent();
            pCommit = readObject(join(OBJECTS, p), Commit.class);
        }

        System.out.println("===");
        System.out.println("commit " + pCommit.SHA_1());
        System.out.println("Date: " + sdf.format(pCommit.getDate()));
        System.out.println(pCommit.getMessage());
        System.out.println();

    }

    public static void globalLog() {
        List<String> allCommits = plainFilenamesIn(OBJECTS);
        Commit pCommit;
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM d HH:mm:ss yyyy Z", Locale.US);
        for (String s : allCommits) {
             pCommit = readObject(join(OBJECTS, s), Commit.class);
            // todo : merge commit
            System.out.println("===");
            System.out.println("commit " + pCommit.SHA_1());
            System.out.println("Date: " + sdf.format(pCommit.getDate()));
            System.out.println(pCommit.getMessage());
            System.out.println();
        }
    }

    /**
     *
     * @param sha1 the new content that HEAD & MASTER have
     */
    private static void changeHeadMaster(String sha1) {
        Utils.writeContents(HEAD, sha1);
        Utils.writeContents(MASTER, sha1);
    }
}
