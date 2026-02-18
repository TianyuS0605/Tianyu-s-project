package gitlet;

import java.awt.desktop.SystemEventListener;
import java.io.File;
import static gitlet.Utils.*;
import java.io.File;
import java.util.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
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
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File COMMITS_DIR = join(OBJECTS_DIR, "commits");
    public static final File HEAD_FILE = join(GITLET_DIR, "head");
    public static final File BRANCH_DIR = join(GITLET_DIR, "branches");
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    public static final File STAGE_FILE = join(GITLET_DIR, "staging_area");
    public static final String DEFAULT_BRANCH = "main";


    /* TODO: fill in the rest of this class. */
    public static void init() {
        Failure_cases.failInit();

        GITLET_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        BRANCH_DIR.mkdir();

        Commit initialCommit = new Commit();
        Branch mainBranch = new Branch(DEFAULT_BRANCH, initialCommit);
        StagingArea stagingArea = new StagingArea();
        save(initialCommit);
        save(mainBranch);
        save(stagingArea);

        writeObject(HEAD_FILE, mainBranch);
    }

    public static void add(String filename) {

        File newFile = join(CWD, filename);
        Failure_cases.failAdd(filename);

        Blob newBlob = new Blob(readContents(newFile));
        String newBlobID = newBlob.getID();
        byte[] newBlobContents = newBlob.getContents();

        Branch currentBranch = readObject(HEAD_FILE, Branch.class);
        String blobIdInCommit = currentBranch.getHead().getBlobID(filename);

        StagingArea stagingArea = loadStage();
        String blobIdInStage = stagingArea.getAddition().get(filename);

        if (newBlobID.equals(blobIdInCommit)) {
            if (stagingArea.getRemoval().contains(filename)) {
                stagingArea.removeFromRemoval(filename);
                save(stagingArea);
            }
            System.exit(0);
        }

        if (newBlobID.equals(blobIdInStage)) {
            System.exit(0);
        }


        save(newBlobID, newBlobContents);
        stagingArea.addFile(filename, newBlobID);
        save(stagingArea);
    }



    private static void commit(String message, String currentCommitID) {
        StagingArea stagingArea = loadStage();
        Failure_cases.failCommit(message);

        Commit newCommit = new Commit(message, currentCommitID);
        Commit modifiedCommit = modifyCommit(newCommit, stagingArea);

        Branch currentBranch = readObject(HEAD_FILE, Branch.class);
        currentBranch.setHead(modifiedCommit);
        save(currentBranch);
        writeObject(HEAD_FILE, currentBranch);
        stagingArea.clear();
        save(stagingArea);
        save(modifiedCommit);
    }

    public static void commit(String message) {
        Branch branch = readObject(HEAD_FILE, Branch.class);
        String currentCommitId = getHeadCommitID(branch.getName());
        commit(message, currentCommitId);
    }


    public static void restore(String[] args) {
        if (args.length != 3 && args.length != 4) {
            System.out.println("Incorrect operands.");
        }
        if (args.length == 3) {
            if (!args[1].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            Branch branch = readObject(HEAD_FILE, Branch.class);
            restoreFile(getHeadCommitID(branch.getName()), args[2]);
        }
        if (args.length == 4) {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            restoreFile(args[1], args[3]);
        }
    }

    public static void restoreFile(String commitID, String filename) {
        Failure_cases.failRestore(commitID, filename);
        Commit commit = loadCommit(commitID);
        String blobID = commit.getBlobs().get(filename);

        byte[] blobContents = readContents(join(BLOBS_DIR, blobID));
        writeContents(join(CWD, filename), (Object) blobContents);
    }

    public static void log() {
        Branch branch = readObject(HEAD_FILE, Branch.class);
        String commitID = getHeadCommitID(branch.getName());
        while (commitID != null) {
            Commit commit = loadCommit(commitID);
            if (commit != null) {
                System.out.println(commit);
                commitID = commit.getParentID();
            }
        }
    }

    public static void find(String commitMessage) {
        boolean found = false;
        List<String> commitHashes = Utils.plainFilenamesIn(COMMITS_DIR);
        for (String hash : commitHashes) {
            Commit commit = readObject(join(COMMITS_DIR, hash), Commit.class);
            if (commit.getMessage().equals(commitMessage)) {
                System.out.println(commit.getHash());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
        }

    }

    public static void globalLog() {
        List<String> commitHashes = Utils.plainFilenamesIn(COMMITS_DIR);
        for (String hash : commitHashes) {
            Commit commit = readObject(join(COMMITS_DIR, hash), Commit.class);
            System.out.println(commit);
        }
    }

    public static void remove(String fileName) {
        StagingArea stagingArea = loadStage();
        Branch currentBranch = readObject(HEAD_FILE, Branch.class);
        Commit currentCommit = currentBranch.getHead();

        if (currentCommit.getBlobs().containsKey(fileName)) {
            File file = new File(CWD, fileName);
            Utils.restrictedDelete(file);
            stagingArea.addToRemoval(fileName);
            if (stagingArea.getAddition().containsKey(fileName)) {
                stagingArea.removeFromAddition(fileName);
            }
            save(stagingArea);
        } else if (stagingArea.getAddition().containsKey(fileName)) {
            stagingArea.removeFromAddition(fileName);
            save(stagingArea);
        }  else {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    public static void createNewBranch(String branchName) {
        List<String> branchNames = Utils.plainFilenamesIn(BRANCH_DIR);
        if (branchNames.contains(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Branch currentBranch = readObject(HEAD_FILE, Branch.class);
        Commit currentCommitHead = currentBranch.getHead();
        Branch newBranch = new Branch(branchName, currentCommitHead);
        save(newBranch);
    }

    public static void removeBranch(String branchName) {
        Branch currentBranch = readObject(HEAD_FILE, Branch.class);
        if (currentBranch.getName().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        File newFile = Utils.join(BRANCH_DIR, branchName);
        if (!newFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        File file = new File(BRANCH_DIR, branchName);
        file.delete();
    }

    public static void switchBranch(String branchName) {
        File branchFile = Utils.join(BRANCH_DIR, branchName);
        if (!branchFile.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        StagingArea stagingArea = loadStage();
        Branch currentBranch = readObject(HEAD_FILE, Branch.class);
        Commit currentCommit = currentBranch.getHead();
        List<String> fileNames = plainFilenamesIn(CWD);

        Branch targetBranch = readObject(join(BRANCH_DIR, branchName), Branch.class);
        Commit targetCommit = targetBranch.getHead();
        Commit target = readObject(join(COMMITS_DIR, targetCommit.getHash()), Commit.class);

        for (String fileName: fileNames) {
            if (!currentCommit.getBlobs().containsKey(fileName) && targetCommit.getBlobs().containsKey(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        for (String fileName: fileNames) {
            Utils.restrictedDelete(join(CWD, fileName));
        }

        for (Map.Entry<String, String> entry: target.getBlobs().entrySet()) {
            byte[] blobContents = readContents(join(BLOBS_DIR, entry.getValue()));
            writeContents(join(CWD, entry.getKey()), (Object) blobContents);
        }
        writeObject(HEAD_FILE, targetBranch);
        stagingArea.clear();
        save(stagingArea);

    }

    public static void getStatus() {
        List<String> branchNames = plainFilenamesIn(BRANCH_DIR);
        Branch currentBranch = readObject(HEAD_FILE, Branch.class);
        Collections.sort(branchNames);
        System.out.println("=== Branches ===");
        if (branchNames.indexOf(currentBranch.getName()) != -1) {
            branchNames.set(0, "*" + currentBranch.getName());
        }
        for (String name: branchNames) {
            System.out.println(name);
        }
        System.out.println();

        StagingArea stagingArea = loadStage();
        System.out.println("=== Staged Files ===");
        List<String> stagedFileList = new ArrayList(stagingArea.getAddition().keySet());
        Collections.sort(stagedFileList);
        for (String fileName : stagedFileList) {
            System.out.println(fileName);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        List<String> removedFileList = new ArrayList(stagingArea.getRemoval());
        Collections.sort(removedFileList);
        for (String fileName : removedFileList) {
            System.out.println(fileName);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public static void reset(String commitID) {
        File newFile = Utils.join(COMMITS_DIR, commitID);
        if (!newFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        StagingArea stagingArea = loadStage();
//        if (stagingArea.getAddition().keySet().size() > 0 || stagingArea.getRemoval().size() > 0) {
//            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
//            System.exit(0);
//        }
        Branch currentBranch = readObject(HEAD_FILE, Branch.class);
        Commit currentCommit = currentBranch.getHead();
        Commit targetCommit = readObject(join(COMMITS_DIR, commitID), Commit.class);
        List<String> fileNames = plainFilenamesIn(CWD);
        for (String fileName: fileNames) {
            if (!currentCommit.getBlobs().containsKey(fileName) && targetCommit.getBlobs().containsKey(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        for (String fileName: fileNames) {
            Utils.restrictedDelete(join(CWD, fileName));
        }

        Commit target = readObject(join(COMMITS_DIR, commitID), Commit.class);
        for (Map.Entry<String, String> entry: target.getBlobs().entrySet()) {
            byte[] blobContents = readContents(join(BLOBS_DIR, entry.getValue()));
            writeContents(join(CWD, entry.getKey()), (Object) blobContents);
        }

        stagingArea.clear();
        save(stagingArea);
        currentBranch.setHead(targetCommit);
        save(currentBranch);
        writeObject(HEAD_FILE, currentBranch);
    }

    public static void merge(String branchName) {
        boolean hasConflicts = false;
        StagingArea stagingArea = loadStage();
        if (stagingArea.getAddition().size() > 0 || stagingArea.getRemoval().size() > 0) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        File newFile = Utils.join(BRANCH_DIR, branchName);
        if (!newFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        Branch currentBranch = readObject(HEAD_FILE, Branch.class);
        if (currentBranch.getName().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }

        List<String> fileNames = plainFilenamesIn(CWD);
        Commit currentCommit = currentBranch.getHead();
        for (String fileName: fileNames) {
            if (!currentCommit.getBlobs().keySet().contains(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        Branch targetBranch = readObject(join(BRANCH_DIR, branchName), Branch.class);
        Commit splitPointCommit = findSplitPoint(currentBranch, targetBranch);

        Commit targetCommit = targetBranch.getHead();
        Map<String, String> currentBlobs = currentCommit.getBlobs();
        Map<String, String> targetBlobs = targetCommit.getBlobs();
        Map<String, String> splitPointBlobs = splitPointCommit.getBlobs();
        // processed by default: 2, 3, 4, 7
        for (Map.Entry<String, String> splitPointEntry: splitPointBlobs.entrySet()) {
            if (currentBlobs.containsKey(splitPointEntry.getKey())
                    && targetBlobs.containsKey(splitPointEntry.getKey())) {
                // 1. currentBranch unmodified, targetBranch modified
                if (currentBlobs.get(splitPointEntry.getKey()).equals(splitPointEntry.getValue()) &&
                        !targetBlobs.get(splitPointEntry.getKey()).equals(splitPointEntry.getValue())) {
                    stagingArea.addFile(splitPointEntry.getKey(), targetBlobs.get(splitPointEntry.getKey()));
                    Utils.restrictedDelete(join(CWD, splitPointEntry.getKey()));
                    restoreFile(targetCommit.getHash(), splitPointEntry.getKey());
                    stagingArea.addFile(splitPointEntry.getKey(), targetBlobs.get(splitPointEntry.getKey()));
                }
                // 8. conflict - both modified after split point
                else if (!currentBlobs.get(splitPointEntry.getKey()).equals(splitPointEntry.getValue()) &&
                        !targetBlobs.get(splitPointEntry.getKey()).equals(splitPointEntry.getValue()) &&
                        !currentBlobs.get(splitPointEntry.getKey()).equals(targetBlobs.get(splitPointEntry.getKey()))) {
                    hasConflicts = true;
                    File currentBlobFile = Utils.join(BLOBS_DIR, currentBlobs.get(splitPointEntry.getKey()));
                    File targetBlobFile = Utils.join(BLOBS_DIR, targetBlobs.get(splitPointEntry.getKey()));
                    byte[] updatedContent = mergeConflictFile(currentBlobFile, targetBlobFile);
                    Blob newBlob = new Blob(updatedContent);
                    writeContents(join(CWD, splitPointEntry.getKey()), updatedContent);
                    stagingArea.addFile(splitPointEntry.getKey(), newBlob.getID());
                }

            } else if (currentBlobs.containsKey(splitPointEntry.getKey()) // 6
                    && (currentBlobs.get(splitPointEntry.getKey()).equals(splitPointEntry.getValue()))
                    && !targetBlobs.containsKey(splitPointEntry.getKey())) {
                stagingArea.removeFile(splitPointEntry.getKey());
                Utils.restrictedDelete(join(CWD, splitPointEntry.getKey()));

            } else if (currentBlobs.containsKey(splitPointEntry.getKey())) {
                // 8. conflict - current modified and target deleted
                if (!currentBlobs.get(splitPointEntry.getKey()).equals(splitPointEntry.getValue())) {
                    hasConflicts = true;
                    File currentBlobFile = Utils.join(BLOBS_DIR, currentBlobs.get(splitPointEntry.getKey()));
                    byte[] updatedContent = mergeConflictFile(currentBlobFile, null);
                    Blob newBlob = new Blob(updatedContent);
                    writeContents(join(CWD, splitPointEntry.getKey()), updatedContent);
                    stagingArea.addFile(splitPointEntry.getKey(), newBlob.getID());
                }
            } else if (targetBlobs.containsKey(splitPointEntry.getKey())) {
                // 8. conflict - current deleted and target modified
                if (!targetBlobs.get(splitPointEntry.getKey()).equals(splitPointEntry.getValue())) {
                    hasConflicts = true;
                    File targetBlobFile = Utils.join(BLOBS_DIR, targetBlobs.get(splitPointEntry.getKey()));
                    byte[] updatedContent = mergeConflictFile(null, targetBlobFile);
                    Blob newBlob = new Blob(updatedContent);
                    writeContents(join(CWD, splitPointEntry.getKey()), updatedContent);
                    stagingArea.addFile(splitPointEntry.getKey(), newBlob.getID());
                }
            }
        }
        for (Map.Entry<String, String> targetEntry: targetBlobs.entrySet()) {
            // 8. conflict - both created after split point
            if (currentBlobs.containsKey(targetEntry.getKey()) &&
                    !currentBlobs.get(targetEntry.getKey()).equals(targetEntry.getValue()) &&
                    !splitPointBlobs.containsKey(targetEntry.getKey())) {
                hasConflicts = true;
                File currentBlobFile = Utils.join(BLOBS_DIR, currentBlobs.get(targetEntry.getKey()));
                File targetBlobFile = Utils.join(BLOBS_DIR, targetEntry.getValue());
                byte[] updatedContent = mergeConflictFile(currentBlobFile, targetBlobFile);
                Blob newBlob = new Blob(updatedContent);
                writeContents(join(CWD, targetEntry.getKey()), updatedContent);
                stagingArea.addFile(targetEntry.getKey(), newBlob.getID());
            } else if (!currentBlobs.containsKey(targetEntry.getKey()) && !splitPointBlobs.containsKey(targetEntry.getKey())) {
                restoreFile(targetCommit.getHash(), targetEntry.getKey());
                stagingArea.addFile(targetEntry.getKey(), targetEntry.getValue());
            }
        }
        if (hasConflicts) {
            System.out.println("Encountered a merge conflict.");
        }
        save(stagingArea);
        commit("Merged " + branchName + " into " + currentBranch.getName() + ".");
    }

    private static byte[] mergeConflictFile(File currentBlobFile, File targetBlobFile) {
        String newContent = "<<<<<<< HEAD\n";
        if (currentBlobFile != null) {
            newContent += readContentsAsString(currentBlobFile);
        }
        newContent += "=======\n";
        if (targetBlobFile != null) {
            newContent += readContentsAsString(targetBlobFile);
        }
        newContent += (">>>>>>>\n");
        byte[] updatedContent = newContent.getBytes();
        return updatedContent;
    }

    private static Commit findSplitPoint(Branch currentBranch, Branch targetBranch) {

        Commit currentCommit = currentBranch.getHead();
        Commit targetCommit = targetBranch.getHead();
        Stack<Commit> currentStack = new Stack<>();
        Stack<Commit> targetStack = new Stack<>();
        while (currentCommit.getParentID() != null) {
            currentStack.push(currentCommit);
            currentCommit = readObject(join(COMMITS_DIR, currentCommit.getParentID()), Commit.class);
        }
        currentStack.push(currentCommit);
        while (targetCommit.getParentID() != null) {
            targetStack.push(targetCommit);
            targetCommit = readObject(join(COMMITS_DIR, targetCommit.getParentID()), Commit.class);
        }
        targetStack.push(targetCommit);
        Commit parentCommit = currentStack.pop();
        targetStack.pop();

        while (currentStack.size() > 0 || targetStack.size() > 0) {
            if (currentStack.size() == 0) {
                System.out.println("Current branch fast-forwarded.");
                writeObject(HEAD_FILE, targetBranch);
                List<String> fileNames = plainFilenamesIn(CWD);
                for (String fileName: fileNames) {
                    Utils.restrictedDelete(join(CWD, fileName));
                }
                String headCommitID = targetBranch.getHead().getHash();
                Map<String, String> blobs = currentCommit.getBlobs();
                for (String fileName: blobs.keySet()) {
                    restoreFile(headCommitID, fileName);
                }
                System.exit(0);
            }
            if (targetStack.size() == 0) {
                System.out.println("Given branch is an ancestor of the current branch.");
                System.exit(0);
            }
            Commit tempCommit = currentStack.pop();
            if (!tempCommit.getHash().equals(targetStack.pop().getHash())) {
                return parentCommit;
            }
            parentCommit = tempCommit;
        }
        return parentCommit;
    }

    public static String getHeadCommitID(String branchName) {
        File branchFile = join(BRANCH_DIR, branchName);
        if (!branchFile.exists()) {
            return null;
        } else {
            Branch branch = readObject(branchFile, Branch.class);
            return branch.getHead().getHash();
        }
    }

    public static void save(Commit commit) {
        writeObject(join(COMMITS_DIR, commit.getHash()), commit);
    }

    public static void save(Branch branch) {
        writeObject(join(BRANCH_DIR, branch.getName()), branch);
    }

    public static Commit loadCommit(String commitID) {
        if (commitID.length() < 40) {
            List<String> commitIdList = plainFilenamesIn(COMMITS_DIR);
            if (commitIdList != null){
                for (String findCommitId : commitIdList) {
                    if (findCommitId.startsWith(commitID)) {
                        commitID = findCommitId;
                        break;
                    }
                }
            }

        }
        File file = join(COMMITS_DIR, commitID);
        if (!file.exists()) {
            return null;
        } else {
            return readObject(file, Commit.class);
        }
    }

    public static Commit modifyCommit(Commit commit, StagingArea stagingArea) {
        for (Map.Entry<String, String> entry : stagingArea.getAddition().entrySet()) {
            String fileName = entry.getKey();
            String blobID = entry.getValue();
            commit.getBlobs().put(fileName, blobID);
        }
        for (String fileName : stagingArea.getRemoval()) {
            commit.getBlobs().remove(fileName);
        }
        return commit;
    }

    public static void save(String blobID, byte[] contents) {
        writeContents(join(BLOBS_DIR, blobID), (Object) contents);
    }

    public static StagingArea loadStage() {
        return readObject(STAGE_FILE, StagingArea.class);
    }

    public static void save(StagingArea stage) {
        writeObject(STAGE_FILE, stage);
    }

}