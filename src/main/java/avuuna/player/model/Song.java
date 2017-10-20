package avuuna.player.model;

import java.io.*;
import java.net.*;

/**
 *
 * @author Avuuna, la Luz del Alba
 */
public class Song extends File {
	private static final long serialVersionUID = 8353266338494775220L;

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
