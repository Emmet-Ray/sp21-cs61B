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
                validateFormat(args);
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
                validateFormat(args);
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
                validateFormat(args);
                Repository.rm(args[1]);
                break;
            case "log":
                // todo : "log"
                initializedGitletRepository();
                validateNumArgs("log", args, 1);
                // TODO : implement the validateFormat()
                validateFormat(args);
                Repository.log();
                break;
            case "global-log":
                // todo : "log"
                initializedGitletRepository();
                validateNumArgs("global-log", args, 1);
                // TODO : implement the validateFormat()
                validateFormat(args);
                Repository.globalLog();
                break;
            case "checkout":
                /**
                 *  todo : 3 kinds of checkout
                 */
                initializedGitletRepository();
                if (args.length == 3) {
                    validateFormat(args);
                    //"checkout -- [file name]"
                    try {
                        Repository.checkout1(args[2]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (args.length == 4) {
                    validateFormat(args);
                    //"checkout [commit id] -- [file name]"
                    try {
                        Repository.checkout2(args[1], args[3]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (args.length == 2){
                    validateFormat(args);
                    // todo 3. "checkout [branch name]"
                    try {
                        Repository.checkout3(args[1]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

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
                validateFormat(args);
                Repository.find(args[1]);
                break;
            case "status":
                // todo : "status"
                initializedGitletRepository();
                validateNumArgs("status", args, 1);
                validateFormat(args);
                Repository.status();
                break;
            case "branch":
                // todo : "branch [branch name]"
                initializedGitletRepository();
                validateNumArgs("branch", args, 2);
                validateFormat(args);
                try {
                    Repository.branch(args[1]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "rm-branch":
                // todo : "rm-branch [branch name]"
                initializedGitletRepository();
                validateNumArgs("rm-branch", args, 2);
                validateFormat(args);
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                // todo : "reset [commit id]"
                initializedGitletRepository();
                validateNumArgs("reset", args, 2);
                validateFormat(args);
                try {
                    Repository.reset(args[1]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "merge":
                initializedGitletRepository();
                validateNumArgs("merge", args, 2);
                validateFormat(args);
                try {
                    Repository.merge(args[1]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
    public static void validateFormat(String[] args) {

        switch (args[0]) {
            case "checkout":
                if (args.length == 4 && !args[2].equals("--")) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                } else if (args.length == 3 && !args[1].equals("--")) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;
        }
    }
}
