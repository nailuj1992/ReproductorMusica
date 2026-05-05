package avuuna.player.controller;

import avuuna.player.exception.PlayerException;
import avuuna.player.model.MusicPlayer;
import avuuna.player.model.Song;
import avuuna.player.utils.ModelObserver;
import avuuna.player.utils.Strings;
import avuuna.player.utils.Utils;
import avuuna.player.view.GUIPlayer;
import javazoom.jl.player.basic.BasicPlayerEvent;
import javazoom.jl.player.basic.BasicPlayerException;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * MVC Controller. Wires user actions from the view to the model and reflects
 * model state changes back into the view via the Observer pattern.
 */
public class PlayerController implements Serializable, ModelObserver {
    private static final long serialVersionUID = 232492703123683857L;

    private static PlayerController controller = null;

    private final MusicPlayer model;
    private final GUIPlayer view;

    private boolean running;

    public static PlayerController getInstance() {
        if (controller == null) {
            controller = new PlayerController();
        }
        return controller;
    }

    private PlayerController() {
        model = MusicPlayer.getInstance();
        view = new GUIPlayer();
        bindActions();
    }

    /**
     * Starts the player. {@code onClose} is called when the user closes the window,
     * so the entry point can manage the application lifecycle.
     */
    public final void start(Runnable onClose) {
        running = false;
        model.addObserver(this);

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose.run();
            }
        });

        startProgressThread();
        view.initView();
    }

    private void startProgressThread() {
        Thread progressThread = new Thread(() -> {
            while (true) {
                if (model.getCurrentSong() != null) {
                    view.setProgressValue((int) model.getProgressBytes());
                    try {
                        view.setProgressText(Utils.formatTime(model.getProgressTime())
                                + Strings.OF + Utils.formatTime(model.getCurrentSong().getDuration()));
                    } catch (Exception ex) {
                        Utils.display("Exception in progress thread: " + ex.getMessage());
                    }
                } else {
                    view.setProgressValue(0);
                    view.setProgressText(Strings.ZERO_TIME);
                }
            }
        });
        progressThread.start();
    }

    private void bindActions() {
        view.setOpenMenuAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        view.addOpenMenuListener(e -> openSongDialog());
        view.addAddSongListener(e -> openSongDialog());

        view.addPlayListener(e -> {
            if (view.isShowingPause()) {
                pauseSong();
            } else if (!running && model.getCurrentSong() != null) {
                playSong();
            } else if (running) {
                resumeSong();
            }
        });

        view.addStopListener(e -> stopSong());
        view.addNextListener(e -> nextSong());
        view.addPrevListener(e -> previousSong());

        view.addRepeatListener(e -> {
            Boolean current = model.getRepeatMode();
            Boolean nextValidation = Boolean.TRUE.equals(current) ? Boolean.FALSE : null;
            Boolean next = (current == null) ? Boolean.TRUE : nextValidation;
            model.setRepeatMode(next);
        });

        view.addShuffleListener(e -> model.setRandomMode(!model.isRandomMode()));

        view.addVolumeChangeListener(e -> setVolume());

        view.addProgressClickListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (model.getCurrentSong() == null) return;
                double ratio = (double) e.getX() / e.getComponent().getWidth();
                long targetBytes = (long) (ratio * model.getCurrentSong().getBytesLength());
                try {
                    model.seek(targetBytes);
                } catch (BasicPlayerException ex) {
                    Utils.display("Seek error: " + ex.getMessage());
                }
            }
        });

        view.addSongListMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String selected = view.getSelectedValue();
                    if (selected != null && !selected.contains(Strings.CURRENT_MARKER)) {
                        int sel = view.getSelectedIndex();
                        openSong(selected);
                        model.clearShuffleHistory();
                        view.setSelectedIndex(sel);
                    }
                }
            }
        });

        view.addRemoveSongListener(e -> {
            if (model.getSongCount() > 0) {
                String selected = view.getSelectedValue();
                if (selected != null) {
                    int confirmed = JOptionPane.showConfirmDialog(view, Strings.CONFIRM_REMOVE_MSG,
                            Strings.CONFIRM_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
                    if (confirmed == JOptionPane.YES_OPTION) {
                        int sel = view.getSelectedIndex();
                        removeSong(selected.replace(Strings.CURRENT_MARKER, ""));
                        view.setSelectedIndex(sel);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, PlayerException.ERROR_NO_SONG_SELECTED,
                            PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, PlayerException.ERROR_NO_SONGS_LIST,
                        PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
            }
        });

        view.addClearPlaylistListener(e -> {
            if (model.getSongCount() > 0) {
                if (!view.isPlaylistEmpty()) {
                    int confirmed = JOptionPane.showConfirmDialog(view, Strings.CONFIRM_CLEAR_MSG,
                            Strings.CONFIRM_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
                    if (confirmed == JOptionPane.YES_OPTION) {
                        clearPlaylist();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, PlayerException.ERROR_NO_SONGS_LIST,
                        PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
            }
        });

        view.addMoveUpListener(e -> {
            if (model.getSongCount() > 0) {
                int selected = view.getSelectedIndex();
                if (selected != -1) {
                    model.swapSongs(selected, selected - 1);
                    view.setSelectedIndex(selected - 1);
                } else {
                    JOptionPane.showMessageDialog(null, PlayerException.ERROR_NO_SONG_SELECTED,
                            PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        view.addMoveDownListener(e -> {
            if (model.getSongCount() > 0) {
                int selected = view.getSelectedIndex();
                if (selected != -1) {
                    model.swapSongs(selected, selected + 1);
                    view.setSelectedIndex(selected + 1);
                } else {
                    JOptionPane.showMessageDialog(null, PlayerException.ERROR_NO_SONG_SELECTED,
                            PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // -------------------------------------------------------------------------
    // Observer — drives view from model state
    // -------------------------------------------------------------------------

    @Override
    public void update() {
        if (model.getCurrentSong() != null) {
            switch (model.getCurrentEvent()) {
                case BasicPlayerEvent.RESUMED:
                    view.setPlayingState(true);
                    break;
                case BasicPlayerEvent.PAUSED:
                    view.setPlayingState(false);
                    break;
                case BasicPlayerEvent.PLAYING:
                    view.setPlayingState(true);
                    running = true;
                    break;
                case BasicPlayerEvent.STOPPED:
                    view.setPlayingState(false);
                    running = false;
                    break;
                case BasicPlayerEvent.OPENED:
                    view.setSongLabel(model.getCurrentSong().getName());
                    view.setProgressMax((int) model.getCurrentSong().getBytesLength());
                    view.setProgressText(Strings.ZERO + Strings.OF
                            + Utils.formatTime(model.getCurrentSong().getDuration()));
                    if (!running) {
                        playSong();
                    }
                    setVolume();
                    break;
                default:
                    break;
            }
        } else {
            view.setSongLabel(null);
            view.setProgressMax(0);
        }

        view.setRepeatButtonState(model.getRepeatMode());
        view.setShuffleButtonState(model.isRandomMode());
        updatePlaylistDisplay();
        view.repaint();
    }

    private void updatePlaylistDisplay() {
        List<String> displayNames = new ArrayList<>();
        for (int i = 0; i < model.getSongCount(); i++) {
            Song song = model.getSong(i);
            String name = song.getName();
            if (model.getCurrentSong() != null && model.getCurrentSong().equals(song)) {
                name = Strings.CURRENT_MARKER + name;
            }
            displayNames.add(name);
        }
        view.refreshPlaylist(displayNames);
    }

    // -------------------------------------------------------------------------
    // Playback actions
    // -------------------------------------------------------------------------

    private void openSongDialog() {
        File[] files = view.showOpenDialog();
        if (files == null || files.length == 0) {
            return;
        }
        for (File f : files) {
            if (f.getAbsolutePath().endsWith(".mp3")) {
                try {
                    Song song = new Song(f.getAbsolutePath());
                    model.addSong(song);
                    if (model.getCurrentSong() == null) {
                        model.setCurrentSong(song);
                        model.open(model.getCurrentSong());
                        view.setSelectedIndex(model.getSongCount() - 1);
                    }
                } catch (BasicPlayerException | PlayerException ex) {
                    Utils.log(PlayerException.ERROR_OPENING_SONG, ex);
                    if (files.length == 1) {
                        JOptionPane.showMessageDialog(null, PlayerException.ERROR_OPENING_SONG,
                                PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void playSong() {
        try {
            model.play();
        } catch (BasicPlayerException ex) {
            Utils.log(PlayerException.ERROR_PLAYING_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_PLAYING_SONG,
                    PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pauseSong() {
        try {
            model.pause();
        } catch (BasicPlayerException ex) {
            Utils.log(PlayerException.ERROR_PAUSING_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_PAUSING_SONG,
                    PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resumeSong() {
        try {
            model.resume();
        } catch (BasicPlayerException ex) {
            Utils.log(PlayerException.ERROR_RESUMING_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_RESUMING_SONG,
                    PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopSong() {
        try {
            model.stop();
        } catch (BasicPlayerException ex) {
            Utils.log(PlayerException.ERROR_STOPPING_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_STOPPING_SONG,
                    PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void nextSong() {
        try {
            model.next(true);
        } catch (BasicPlayerException ex) {
            Utils.log(PlayerException.ERROR_NEXT_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_NEXT_SONG,
                    PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void previousSong() {
        try {
            model.previous(true);
        } catch (BasicPlayerException ex) {
            Utils.log(PlayerException.ERROR_PREV_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_PREV_SONG,
                    PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setVolume() {
        try {
            model.setVolume((double) view.getVolumeValue() / 100);
        } catch (BasicPlayerException ex) {
            // Silent — volume errors occur frequently when no song is loaded
        }
    }

    private void clearPlaylist() {
        try {
            model.clearPlaylist();
        } catch (BasicPlayerException ex) {
            Utils.log(PlayerException.ERROR_CLEAR_LIST, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_CLEAR_LIST,
                    PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSong(String name) {
        try {
            Song song = model.getSong(name);
            model.setCurrentSong(song);
            model.stop();
            model.open(song);
        } catch (BasicPlayerException ex) {
            Utils.log(PlayerException.ERROR_OPENING_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_OPENING_SONG,
                    PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeSong(String name) {
        try {
            Song song = model.getSong(name);
            model.removeSong(song);
        } catch (BasicPlayerException ex) {
            Utils.log(PlayerException.ERROR_REMOVE_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_REMOVE_SONG,
                    PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        } catch (PlayerException ex) {
            Utils.log(PlayerException.ERROR_REMOVE_SONG_NO_EXISTS, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_REMOVE_SONG_NO_EXISTS,
                    PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
