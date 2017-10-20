package avuuna.player.logic;

import java.io.*;
import java.net.*;

/**
 *
 * @author pegasusmax
 */
public class Song extends File {

    private long duration;
    private double bytesLength;

    public Song(URI uri) {
        super(uri);
    }

    public Song(File parent, String child) {
        super(parent, child);
    }

    public Song(String pathname) {
        super(pathname);
    }

    public Song(String parent, String child) {
        super(parent, child);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getBytesLength() {
        return bytesLength;
    }

    public void setBytesLength(double bytesLength) {
        this.bytesLength = bytesLength;
    }
}
