package avuuna.player.model;

import java.io.*;
import java.net.*;

/**
 * Esta clase permite trabajar con una cancion.
 * @author Avuuna, la Luz del Alba
 * 
 */
public class Cancion extends File implements Serializable {
	private static final long serialVersionUID = 8353266338494775220L;

	private long duration;
	private double bytesLength;

	public Cancion(URI uri) {
		super(uri);
	}

	public Cancion(File parent, String child) {
		super(parent, child);
	}

	public Cancion(String pathname) {
		super(pathname);
	}

	public Cancion(String parent, String child) {
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
