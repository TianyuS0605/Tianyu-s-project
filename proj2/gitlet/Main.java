package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        validCommand(args);

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                correctArgsNumber(args, 1);
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validCWD();
                correctArgsNumber(args, 2);
                Repository.add(args[1]);
                break;
            case "commit":
                validCWD();
                correctArgsNumber(args, 2);
                Repository.commit(args[1]);
                break;
            case "restore":
                validCWD();
                Repository.restore(args);
                break;
            case "log":
                validCWD();
                correctArgsNumber(args, 1);
                Repository.log();
                break;
            case "global-log":
                validCWD();
                correctArgsNumber(args, 1);
                Repository.globalLog();
                break;
            case "rm":
                validCWD();
                correctArgsNumber(args, 2);
                Repository.remove(args[1]);
                break;
            case "find":
                validCWD();
                correctArgsNumber(args, 2);
                Repository.find(args[1]);
                break;
            case "status":
                validCWD();
                correctArgsNumber(args, 1);
                Repository.getStatus();
                break;
            case "branch":
                validCWD();
                correctArgsNumber(args, 2);
                Repository.createNewBranch(args[1]);
                break;
            case "rm-branch":
                validCWD();
                correctArgsNumber(args, 2);
                Repository.removeBranch(args[1]);
                break;
            case "switch":
                validCWD();
                correctArgsNumber(args, 2);
                Repository.switchBranch(args[1]);
                break;
            case "reset":
                validCWD();
                correctArgsNumber(args, 2);
                Repository.reset(args[1]);
                break;
            case "merge":
                validCWD();
                correctArgsNumber(args, 2);
                Repository.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    public static void validCommand(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
    }

    public static void correctArgsNumber(String[] args, int num) {
        if (args.length != num) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void validCWD() {
        if (!Repository.GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
}