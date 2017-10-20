package avuuna.player;

import avuuna.player.controller.*;
import avuuna.player.exception.*;
import avuuna.player.gui.*;
import avuuna.player.model.*;
import avuuna.player.utils.*;

import java.awt.*;
import javax.swing.*;
import javazoom.jl.player.basic.*;

/**
 *
 * @author Avuuna, la Luz del Alba
 * 
 */
public class MainPlayer {

	public static void main(String[] args) {
		try {
			Utils.setLookAndFeel("Nimbus");
			BasicPlayer basicPlayer = new BasicPlayer();
			Player player = Player.getInstance(basicPlayer);
			
			PlayerController controller = new PlayerController(basicPlayer);
			player.addObserver(controller);
//			PlayerPanel playerPanel = new PlayerPanel(player);
//			PlaylistPanel playlistPanel = new PlaylistPanel(player);
//
//			PlayerFrame playerFrame = new PlayerFrame(player, playerPanel, playlistPanel);
//			drawFrame(playerFrame);
//			player.addObserver(playerFrame.getPlayerPanel());
//			player.addObserver(playerFrame.getPlaylistPanel());
		} catch (LookAndFeelException ex) {
			Utils.display(ex.getClass().getName() + ": " + ex.getMessage());
			Utils.log(MainPlayer.class.getName(), ex);
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
