package gitlet;

import java.io.IOException;

import static gitlet.Repository.GITLET_DIR;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author mengtaoli
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
       /** if no command line arguments input */
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                try {
                    Repository.init();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                initializedGitletRepository();
                validateNumArgs("add", args, 2);
                // TODO : implement the validateFormat()
                validateFormat();
                try {
                    Repository.add(args[1]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "commit":
                // todo : "commit [message]"
                initializedGitletRepository();
                validateNumArgs("commit", args, 2);
                // TODO : implement the validateFormat()
                validateFormat();
                try {
                    Repository.commit(args[1]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "rm":
                // todo : "rm [file name]"
                initializedGitletRepository();
                validateNumArgs("rm", args, 2);
                // TODO : implement the validateFormat()
                validateFormat();
                Repository.rm(args[1]);
                break;
            case "log":
                // todo : "log"
                initializedGitletRepository();
                validateNumArgs("log", args, 1);
                // TODO : implement the validateFormat()
                validateFormat();
                Repository.log();
                break;
            case "global-log":
                // todo : "log"
                initializedGitletRepository();
                validateNumArgs("global-log", args, 1);
                // TODO : implement the validateFormat()
                validateFormat();
                Repository.globalLog();
                break;
            case "checkout":
                /**
                 *  todo : 3 kinds of checkout
                 */
                initializedGitletRepository();
                if (args.length == 3) {
                    validateFormat();
                    //"checkout -- [file name]"
                    try {
                        Repository.checkout1(args[2]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (args.length == 4) {
                    validateFormat();
                    //"checkout [commit id] -- [file name]"
                    try {
                        Repository.checkout2(args[1], args[3]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (args.length == 2){
                    validateFormat();
                    // todo 3. "checkout [branch name]"
                    Repository.checkout3(args[1]);

                } else {
                    // invalid
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;
            case "find":
                // todo : "find [commit message]"
                initializedGitletRepository();
                validateNumArgs("find", args, 2);
                validateFormat();
                Repository.find(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    /**
     * if the command need to be in an initialized gitlet repo, but not
     * print the error message
     * abort
     */
    public static void initializedGitletRepository() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    // TODO
    public static void validateFormat() {

    }
}
