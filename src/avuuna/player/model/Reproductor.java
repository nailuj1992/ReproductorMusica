package avuuna.player.model;

import avuuna.player.exception.*;
import avuuna.player.utils.*;

import java.io.*;
import java.util.*;
import javazoom.jl.player.basic.*;

/**
 *
 * @author Avuuna, la Luz del Alba
 * 
 */
public class Reproductor extends Sujeto implements BasicPlayerListener, Serializable {
	private static final long serialVersionUID = -6648685344329730558L;

	public static Reproductor reproductor = null;

	private int actualEvent;
	private long progressTime;
	private double progressBytes;
	private final BasicPlayer player;
	private Cancion actual;
	private List<Cancion> songs;

	public static Reproductor getInstance(BasicPlayer player) {
		if (reproductor == null) {
			reproductor = new Reproductor(player);
		}
		return reproductor;
	}

	private Reproductor(BasicPlayer player) {
		this.player = player;
		setSongs(new ArrayList<Cancion>());

		player.addBasicPlayerListener(this);
		setController(player);
	}

	public BasicPlayer getPlayer() {
		return player;
	}

	public Cancion getActual() {
		return actual;
	}

	public void setActual(Cancion actual) {
		this.actual = actual;
	}

	public int getActualEvent() {
		return actualEvent;
	}

	public long getProgressTime() {
		return progressTime;
	}

	public void setProgressTime(long progressTime) {
		this.progressTime = progressTime;
	}

	public double getProgressBytes() {
		return progressBytes;
	}

	public void setProgressBytes(double progressBytes) {
		this.progressBytes = progressBytes;
	}

	public void addSong(Cancion song) throws PlayerException {
		if (isSongInList(song)) {
			throw new PlayerException(PlayerException.REPEATED_SONG);
		}
		songs.add(song);
		notifyObservers();
	}

	public void removeSong(Cancion song) {
		songs.remove(song);
		notifyObservers();
	}

	public boolean isSongInList(Cancion song) {
		boolean found = false;
		for (int i = 0; i < songs.size() && !found; i++) {
			if (songs.get(i).equals(song)) {
				found = true;
			}
		}
		return found;
	}

	public void clearList() throws BasicPlayerException {
		songs.clear();
		stop();
		setActual(null);
		notifyObservers();
	}

	public List<Cancion> getSongs() {
		return songs;
	}

	public void setSongs(List<Cancion> songs) {
		this.songs = songs;
	}

	public Cancion getSong(String name) {
		Cancion resp = null;
		for (Cancion song : songs) {
			if (name.equals(song.getName())) {
				resp = song;
				break;
			}
		}
		return resp;
	}

	@Override
	public void opened(Object stream, @SuppressWarnings("rawtypes") Map properties) {
		// Utils.display("opened : " + properties.toString());
		if (properties.containsKey("duration")) {
			actual.setDuration(Long.parseLong(properties.get("duration").toString()));
		}
		if (properties.containsKey("audio.length.bytes")) {
			actual.setBytesLength(Double.parseDouble(properties.get("audio.length.bytes").toString()));
		}
	}

	@Override
	public void progress(int bytesread, long microseconds, byte[] pcmdata, @SuppressWarnings("rawtypes") Map properties) {
		// Utils.display("progress : " + properties.toString());
		if (properties.containsKey("mp3.position.microseconds")) {
			this.setProgressTime(Long.parseLong(properties.get("mp3.position.microseconds").toString()));
		}
		if (properties.containsKey("mp3.position.byte")) {
			this.setProgressBytes(Double.parseDouble(properties.get("mp3.position.byte").toString()));
		}
	}

	@Override
	public void stateUpdated(BasicPlayerEvent event) {
		// Utils.display("stateUpdated : " + event.toString());
		if (event.getCode() == BasicPlayerEvent.EOM) {
			try {
				int current = songs.indexOf(actual);
				if (current == songs.size() - 1) {
					stop();
				} else if (current < songs.size() - 1) {
					actual = songs.get(current + 1);
					open(actual);
					play();
				}
			} catch (BasicPlayerException ex) {
				Utils.log(Reproductor.class.getName(), ex);
			}
		}
	}

	public void open(Cancion song) throws BasicPlayerException {
		player.open(song);
		actualEvent = BasicPlayerEvent.OPENED;
		notifyObservers();
	}

	public void play() throws BasicPlayerException {
		player.play();
		actualEvent = BasicPlayerEvent.PLAYING;
		notifyObservers();
	}

	public void pause() throws BasicPlayerException {
		player.pause();
		actualEvent = BasicPlayerEvent.PAUSED;
		notifyObservers();
	}

	public void resume() throws BasicPlayerException {
		player.resume();
		actualEvent = BasicPlayerEvent.RESUMED;
		notifyObservers();
	}

	public void stop() throws BasicPlayerException {
		player.stop();
		actualEvent = BasicPlayerEvent.STOPPED;
		this.setProgressTime(0);
		this.setProgressBytes(0);
		notifyObservers();
	}

	public void next() throws BasicPlayerException {
		if (songs.size() > 0 && actual != null) {
			stop();
			int index = songs.indexOf(actual);
			if (index == songs.size() - 1) {
				actual = songs.get(0);
			} else {
				actual = songs.get(index + 1);
			}
			open(actual);
		}
	}

	public void previous() throws BasicPlayerException {
		if (songs.size() > 0 && actual != null) {
			stop();
			int index = songs.indexOf(actual);
			if (index == 0) {
				actual = songs.get(songs.size() - 1);
			} else {
				actual = songs.get(index - 1);
			}
			open(actual);
		}
	}

	@Override
	public void setController(BasicController controller) {
		Utils.display("setController : " + controller);
	}
}
