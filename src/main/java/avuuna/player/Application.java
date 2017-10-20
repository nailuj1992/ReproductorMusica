package avuuna.player;

import avuuna.player.utils.*;
import avuuna.player.exception.*;
import avuuna.player.gui.*;
import avuuna.player.logic.*;
import java.awt.*;
import javax.swing.*;
import javazoom.jl.player.basic.BasicPlayer;

/**
 *
 * @author pegasusmax
 */
public class Application {

    public static void main(String[] args) {
        try {
            Utils.setLookAndFeel("Nimbus");
            BasicPlayer basicPlayer = new BasicPlayer();
            Player player = new Player(basicPlayer);
            PlayerPanel playerPanel = new PlayerPanel(player);
            PlaylistPanel playlistPanel = new PlaylistPanel(player);
            
            PlayerFrame playerFrame = new PlayerFrame(player, playerPanel, playlistPanel);
            drawFrame(playerFrame);
            player.getPlayer().addBasicPlayerListener(player);
            player.addObserver(playerFrame.getPlayerPanel());
            player.addObserver(playerFrame.getPlaylistPanel());
        } catch (LookAndFeelException ex) {
            Utils.display(ex.getClass().getName() + ": " + ex.getMessage());
            Utils.log(Application.class.getName(), ex);
        }
    }

    private static void drawFrame(PlayerFrame frame) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, frame.getPlayerPanel(), frame.getPlaylistPanel());
        splitPane.setEnabled(false);
        frame.add(splitPane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }
}
