package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob  implements Serializable {

    private final byte[] contents;
    private final String blobID;

    public Blob(byte[] contents) {
        this.contents = contents;
        this.blobID = Utils.sha1((Object) this.contents);
    }

    public String getID() {
        return blobID;
    }

    public byte[] getContents() {
        return contents;
    }
}