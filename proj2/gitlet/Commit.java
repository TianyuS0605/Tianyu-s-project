package gitlet;

// TODO: any imports you need here

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
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
    /* TODO: fill in the rest of this class. */
    public static final File COMMITS_DIR = Utils.join(Repository.GITLET_DIR, "/commits");
    private final String message;
    private final Date timestamp;
    private final String ParentID;
    private final String hash;
    // fileName, blobId
    private HashMap<String, String> blobs;

    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.ParentID = null;
        hash = calcHash();
        blobs = new HashMap<>();
    }

    public Commit(String message, String ParentID) {
        this.message = message;
        this.timestamp = new Date();
        this.ParentID = ParentID;
        hash = calcHash();

        Commit firstParentCommit = Repository.loadCommit(ParentID);
        blobs = new HashMap<>();
        blobs.putAll(firstParentCommit.blobs);
    }

    private String calcHash() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return Utils.sha1((Object) bos.toByteArray());
    }

    public String getHash() {
        return hash;
    }

    public String getParentID() {
        return ParentID;
    }

    public HashMap<String, String> getBlobs() {
        return blobs;
    }

    public String getBlobID(String fileName) {
        return blobs.get(fileName);
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        String dummyString = "===\n";
        String commitString = String.format("commit %s\n", hash);

        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en", "US"));
        String date = simpleDateFormat.format(timestamp);
        String dateString = String.format("Date: %s\n", date);

        String messageString = String.format("%s\n", message);
        return dummyString + commitString + dateString + messageString;
    }
}