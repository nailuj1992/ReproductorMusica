package avuuna.player.gui;

import avuuna.player.exception.*;
import avuuna.player.model.*;

import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javazoom.jl.player.basic.*;

/**
 *
 * @author pegasusmax
 */
public class PlayerFrame extends JFrame {

    private JFileChooser fc;
    private JMenu fileMenu;
    private JMenuBar menuBar;
    private JMenuItem openItem;
    private PlayerPanel playerPanel;
    private PlaylistPanel playlistPanel;
    private Player player;

    public JFileChooser getFileChooser() {
        return fc;
    }

    public void setFileChooser(JFileChooser fc) {
        this.fc = fc;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerPanel getPlayerPanel() {
        return playerPanel;
    }

    public void setPlayerPanel(PlayerPanel playerPanel) {
        this.playerPanel = playerPanel;
    }

    public PlaylistPanel getPlaylistPanel() {
        return playlistPanel;
    }

    public void setPlaylistPanel(PlaylistPanel playlistPanel) {
        this.playlistPanel = playlistPanel;
    }

    public PlayerFrame(Player player, PlayerPanel playerPanel, PlaylistPanel playlistPanel) {
        setTitle("Reproductor de Música - by Avuuna, la Luz del Alba");
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        openItem = new JMenuItem();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        fileMenu.setText("File");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        openItem.setText("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                openItemActionPerformed(evt);
            }
        });
        fileMenu.add(openItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
        fc = new JFileChooser();
        setPlayer(player);
        setPlayerPanel(playerPanel);
        setPlaylistPanel(playlistPanel);
    }

    private void openSong(Song song) {
        try {
            player.addSong(song);
            if (player.getActual() == null) {
                player.setActual(song);
                player.open(player.getActual());
            }
        } catch (BasicPlayerException | PlayerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found while opening song", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openItemActionPerformed(ActionEvent evt) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos MP3", "mp3");
        fc.setFileFilter(filter);
        fc.setMultiSelectionEnabled(true);
        int seleccion = fc.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File[] songs = fc.getSelectedFiles();
            if(songs.length > 0) {
                Song song;
                for(File s: songs) {
                    song = new Song(s.getAbsolutePath());
                    openSong(song);
                }
            }
        }
    }
}
