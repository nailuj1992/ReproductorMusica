package avuuna.player.model;

import avuuna.player.exception.*;
import avuuna.player.utils.*;

import java.io.*;
import java.util.*;
import javazoom.jl.player.basic.*;

/**
 * Core playback model. Manages the playlist, player state, and repeat/shuffle logic.<br>
 * <br>
 * Implements the Subject role of the Observer pattern: notifies registered
 * {@link ModelObserver}s whenever state changes.
 */
public class MusicPlayer implements BasicPlayerListener, ModelSubject, Serializable {
    private static final long serialVersionUID = -6648685344329730558L;

    private static MusicPlayer instance = null;

    private final List<ModelObserver> observers;

    private int currentEvent;
    private long progressTime;
    private double progressBytes;
    private final BasicPlayer player;
    private Song currentSong;
    private List<Song> playlist;
    private double volume;

    /** {@code true} = repeat all, {@code false} = repeat one, {@code null} = no repeat. */
    private Boolean repeatMode;

    private boolean randomMode;
    private List<Song> shuffleHistory;

    public static MusicPlayer getInstance() {
        if (instance == null) {
            BasicPlayer basicPlayer = new BasicPlayer();
            instance = new MusicPlayer(basicPlayer);
        }
        return instance;
    }

    private MusicPlayer(BasicPlayer player) {
        this.player = player;
        this.playlist = new ArrayList<Song>();
        repeatMode = null;
        randomMode = false;
        shuffleHistory = new ArrayList<Song>();

        player.addBasicPlayerListener(this);
        setController(player);

        volume = 0.5d;
        observers = new ArrayList<ModelObserver>();
    }

    // -------------------------------------------------------------------------
    // Repeat / shuffle mode
    // -------------------------------------------------------------------------

    public Boolean getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(Boolean mode) {
        this.repeatMode = mode;
        notifyObservers();
    }

    public boolean isRandomMode() {
        return randomMode;
    }

    public void setRandomMode(boolean randomMode) {
        this.randomMode = randomMode;
        if (!randomMode) {
            shuffleHistory.clear();
        } else {
            if (currentSong != null) {
                shuffleHistory.add(currentSong);
            }
        }
        notifyObservers();
    }

    // -------------------------------------------------------------------------
    // Current song / state accessors
    // -------------------------------------------------------------------------

    public Song getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(Song song) {
        this.currentSong = song;
    }

    public int getCurrentEvent() {
        return currentEvent;
    }

    public long getProgressTime() {
        return progressTime;
    }

    private void setProgressTime(long progressTime) {
        this.progressTime = progressTime;
    }

    public double getProgressBytes() {
        return progressBytes;
    }

    private void setProgressBytes(double progressBytes) {
        this.progressBytes = progressBytes;
    }

    // -------------------------------------------------------------------------
    // Playlist management
    // -------------------------------------------------------------------------

    public void addSong(Song song) throws PlayerException {
        if (isSongInList(song)) {
            throw new PlayerException(PlayerException.REPEATED_SONG);
        }
        playlist.add(song);
        notifyObservers();
    }

    public void removeSong(Song song) throws PlayerException, BasicPlayerException {
        int index = playlist.indexOf(song);
        boolean removed = playlist.remove(song);
        if (!removed) {
            throw new PlayerException(PlayerException.ERROR_REMOVE_SONG_NO_EXISTS);
        }
        if (currentSong == song) {
            if (playlist.size() > 0) {
                if (shuffleHistory.contains(currentSong)) {
                    shuffleHistory.remove(currentSong);
                }
                nextFrom(--index, false);
            } else {
                stop();
                setCurrentSong(null);
            }
        }
        notifyObservers();
    }

    public boolean isSongInList(Song song) {
        for (Song s : playlist) {
            if (s.equals(song)) {
                return true;
            }
        }
        return false;
    }

    public void clearPlaylist() throws BasicPlayerException {
        playlist.clear();
        stop();
        setCurrentSong(null);
        notifyObservers();
    }

    public void clearShuffleHistory() {
        if (randomMode) {
            shuffleHistory.clear();
        }
    }

    public int getSongCount() {
        return playlist.size();
    }

    public Song getSong(int pos) {
        return playlist.get(pos);
    }

    public Song getSong(String name) {
        for (Song song : playlist) {
            if (name.equals(song.getName())) {
                return song;
            }
        }
        return null;
    }

    public void swapSongs(int index1, int index2) {
        if ((index1 > index2 && index1 > 0) || (index1 < index2 && index1 < playlist.size() - 1)) {
            Song a = playlist.get(index1);
            Song b = playlist.get(index2);
            playlist.set(index1, b);
            playlist.set(index2, a);
            notifyObservers();
        }
    }

    // -------------------------------------------------------------------------
    // Playback controls
    // -------------------------------------------------------------------------

    public void open(Song song) throws BasicPlayerException {
        player.open(song);
        currentEvent = BasicPlayerEvent.OPENED;
        notifyObservers();
    }

    public void play() throws BasicPlayerException {
        player.play();
        player.setGain(volume);
        currentEvent = BasicPlayerEvent.PLAYING;
        notifyObservers();
    }

    public void pause() throws BasicPlayerException {
        player.pause();
        currentEvent = BasicPlayerEvent.PAUSED;
        notifyObservers();
    }

    public void resume() throws BasicPlayerException {
        player.resume();
        currentEvent = BasicPlayerEvent.RESUMED;
        notifyObservers();
    }

    public void stop() throws BasicPlayerException {
        player.stop();
        currentEvent = BasicPlayerEvent.STOPPED;
        setProgressTime(0);
        setProgressBytes(0);
        notifyObservers();
    }

    public void next(boolean clearShuffleHistory) throws BasicPlayerException {
        int index = playlist.indexOf(currentSong);
        nextFrom(index, clearShuffleHistory);
    }

    public void nextFrom(int index, boolean clearShuffleHistory) throws BasicPlayerException {
        if (playlist.size() > 0 && currentSong != null) {
            stop();
            currentSong = (index == playlist.size() - 1) ? playlist.get(0) : playlist.get(index + 1);
            if (clearShuffleHistory) {
                this.shuffleHistory.clear();
            }
            open(currentSong);
        }
    }

    public void advanceAndStop(boolean clearShuffleHistory) throws BasicPlayerException {
        next(clearShuffleHistory);
        stop();
    }

    public void replayCurrent() throws BasicPlayerException {
        stop();
        open(currentSong);
    }

    public void previous(boolean clearShuffleHistory) throws BasicPlayerException {
        if (playlist.size() > 0 && currentSong != null) {
            stop();
            int index = playlist.indexOf(currentSong);
            currentSong = (index == 0) ? playlist.get(playlist.size() - 1) : playlist.get(index - 1);
            if (clearShuffleHistory) {
                this.shuffleHistory.clear();
            }
            open(currentSong);
        }
    }

    public void setVolume(double volume) throws BasicPlayerException {
        this.volume = volume;
        player.setGain(volume);
    }

    // -------------------------------------------------------------------------
    // BasicPlayerListener callbacks
    // -------------------------------------------------------------------------

    @Override
    public void opened(Object stream, @SuppressWarnings("rawtypes") Map properties) {
        if (properties.containsKey("duration")) {
            currentSong.setDuration(Long.parseLong(properties.get("duration").toString()));
        }
        if (properties.containsKey("audio.length.bytes")) {
            currentSong.setBytesLength(Double.parseDouble(properties.get("audio.length.bytes").toString()));
        }
    }

    @Override
    public void progress(int bytesread, long microseconds, byte[] pcmdata,
            @SuppressWarnings("rawtypes") Map properties) {
        if (properties.containsKey("mp3.position.microseconds")) {
            setProgressTime(Long.parseLong(properties.get("mp3.position.microseconds").toString()));
        }
        if (properties.containsKey("mp3.position.byte")) {
            setProgressBytes(Double.parseDouble(properties.get("mp3.position.byte").toString()));
        }
    }

    @Override
    public void stateUpdated(BasicPlayerEvent event) {
        if (event.getCode() == BasicPlayerEvent.EOM) {
            try {
                if (repeatMode != null && !repeatMode) {
                    replayCurrent();
                } else {
                    int current = playlist.indexOf(currentSong);
                    if (randomMode) {
                        if (shuffleHistory.size() < playlist.size() - 1) {
                            if (!shuffleHistory.contains(currentSong)) {
                                shuffleHistory.add(currentSong);
                            }
                            int[] excludes = new int[shuffleHistory.size()];
                            for (int i = 0; i < shuffleHistory.size(); i++) {
                                excludes[i] = playlist.indexOf(shuffleHistory.get(i));
                            }
                            int random = Utils.getRandomWithExclusion(0, playlist.size() - 1, excludes);
                            currentSong = playlist.get(random);
                            open(currentSong);
                            play();
                        } else {
                            shuffleHistory.clear();
                            if (repeatMode != null && repeatMode) {
                                int random = Utils.getRandomWithExclusion(0, playlist.size() - 1, current);
                                currentSong = playlist.get(random);
                                open(currentSong);
                                play();
                            } else {
                                stop();
                            }
                        }
                    } else {
                        if (current == playlist.size() - 1) {
                            if (repeatMode != null && repeatMode) {
                                next(false);
                            } else {
                                advanceAndStop(false);
                            }
                        } else if (current < playlist.size() - 1) {
                            currentSong = playlist.get(current + 1);
                            open(currentSong);
                            play();
                        }
                    }
                }
            } catch (BasicPlayerException ex) {
                Utils.log(MusicPlayer.class.getName(), ex);
            }
        }
    }

    // -------------------------------------------------------------------------
    // ModelSubject implementation
    // -------------------------------------------------------------------------

    @Override
    public void addObserver(ModelObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(ModelObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (ModelObserver o : observers) {
            o.update();
        }
    }

    @Override
    public void setController(BasicController controller) {
        return;
    }
}
