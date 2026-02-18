package gitlet;
import java.io.File;
import static gitlet.Utils.*;
import java.io.File;
import java.util.*;

public class Failure_cases {
    public static void failInit() {
        if(Repository.GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
    }

    public static void failAdd(String filename) {
        File newFile = Utils.join(Repository.CWD, filename);
        if (!newFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }

    public static void failCommit(String message) {
        StagingArea stagingArea = Repository.loadStage();
        if (stagingArea.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
    }

    public static void failRestore(String commitID, String filename) {
        Commit commit = Repository.loadCommit(commitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
            return;
        }
        String blobID = commit.getBlobs().get(filename);
        if (blobID == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }
}
