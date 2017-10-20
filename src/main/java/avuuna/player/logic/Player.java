package avuuna.player.logic;

import avuuna.player.utils.*;
import avuuna.player.exception.*;
import java.util.*;
import javazoom.jl.player.basic.*;

/**
 *
 * @author pegasusmax
 */
public class Player extends Sujeto implements BasicPlayerListener {

    private int actualEvent;
    private long progressTime;
    private double progressBytes;
    private final BasicPlayer player;
    private Song actual;
    private List<Song> songs;

    public Player(BasicPlayer player) {
        this.player = player;
        setSongs(new ArrayList());
    }

    public BasicPlayer getPlayer() {
        return player;
    }

    public Song getActual() {
        return actual;
    }

    public void setActual(Song actual) {
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

    public void addSong(Song song) throws PlayerException {
        if (isSongInList(song)) {
            throw new PlayerException(PlayerException.REPEATED_SONG);
        }
        songs.add(song);
        notifyObservers();
    }

    public void removeSong(Song song) {
        songs.remove(song);
        notifyObservers();
    }

    public boolean isSongInList(Song song) {
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

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Song getSong(String name) {
        Song resp = null;
        for (Song song : songs) {
            if (name.equals(song.getName())) {
                resp = song;
                break;
            }
        }
        return resp;
    }

    @Override
    public void opened(Object stream, Map properties) {
        //Utils.display("opened : " + properties.toString());
        if (properties.containsKey("duration")) {
            actual.setDuration(Long.parseLong(properties.get("duration").toString()));
        }
        if (properties.containsKey("audio.length.bytes")) {
            actual.setBytesLength(Double.parseDouble(properties.get("audio.length.bytes").toString()));
        }
    }

    @Override
    public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
        //Utils.display("progress : " + properties.toString());
        if (properties.containsKey("mp3.position.microseconds")) {
            this.setProgressTime(Long.parseLong(properties.get("mp3.position.microseconds").toString()));
        }
        if (properties.containsKey("mp3.position.byte")) {
            this.setProgressBytes(Double.parseDouble(properties.get("mp3.position.byte").toString()));
        }
    }

    @Override
    public void stateUpdated(BasicPlayerEvent event) {
        Utils.display("stateUpdated : " + event.toString());
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
                Utils.log(Player.class.getName(), ex);
            }
        }
    }

    public void open(Song song) throws BasicPlayerException {
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
