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
    public static void main(String[] args) throws IOException {
       /** if no command line arguments input */
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                initializedGitletRepository();
                validateNumArgs("add", args, 2);
                // TODO : implement the validateFormat()
                validateFormat();
                Repository.add(args[1]);
                break;
            case "commit":
                // todo : "commit [message]"
                initializedGitletRepository();
                validateNumArgs("commit", args, 2);
                // TODO : implement the validateFormat()
                validateFormat();
                Repository.commit(args[1]);
                break;
            // TODO: FILL THE REST IN

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
