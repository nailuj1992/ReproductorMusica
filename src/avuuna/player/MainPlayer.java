package avuuna.player;

import javax.swing.*;

import avuuna.player.controller.*;
import avuuna.player.exception.*;
import avuuna.player.utils.*;

/**
 *
 * @author Avuuna, la Luz del Alba
 * 
 */
public class MainPlayer {

	public static void main(String[] args) {
		try {
			Utils.setLookAndFeel(Utils.windowsLookAndFeel);
			PlayerController controller = new PlayerController();
			controller.model.addObserver(controller);
		} catch (LookAndFeelException ex) {
			Utils.display(ex.getClass().getName() + ": " + ex.getMessage());
			Utils.log(MainPlayer.class.getName(), ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
		}
	}
}
