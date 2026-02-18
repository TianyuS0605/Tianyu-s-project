package gitlet;

import java.io.Serializable;

public class Branch implements Serializable {
    private String name;
    private Commit head;
    public Branch(String name, Commit head) {
        this.name = name;
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public Commit getHead() {
        return head;
    }

    public void setHead(Commit newHead) {
        head = newHead;
    }

}
