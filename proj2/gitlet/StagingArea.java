package gitlet;

import java.io.*;
import java.util.*;

public class StagingArea implements Serializable{
    private HashMap<String, String> addition;
    // fileName
    private HashSet<String> removal;

    public StagingArea() {
        addition = new HashMap<>();
        removal = new HashSet<>();
    }

    public boolean isEmpty() {
        return addition.isEmpty() && removal.isEmpty();
    }

    public void addFile(String filename, String blobID) {
        addition.put(filename, blobID);
        removal.remove(filename);
    }

    public void removeFile(String filename) {
        addition.remove(filename);
        removal.add(filename);
    }

    public void removeFromAddition(String fileName) {
        addition.remove(fileName);
    }

    public void addToRemoval(String fileName) {
        removal.add(fileName);
    }

    public void removeFromRemoval(String fileName) {
        removal.remove(fileName);
    }

    public HashMap<String, String> getAddition() {
        return addition;
    }

    public HashSet<String> getRemoval() {
        return removal;
    }

    public void clear() {
        addition.clear();
        removal.clear();
    }
}