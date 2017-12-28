package avuuna.player;

import javax.swing.*;

import avuuna.player.controller.*;
import avuuna.player.exception.*;
import avuuna.player.utils.*;

/**
 * En esta clase se encuentra el metodo principal.
 * @author Avuuna, la Luz del Alba
 * 
 */
public class MainPlayer {
	
	public static final Control control = Control.getInstance("resources\\images\\player");

	/**
	 * Metodo principal de la aplicacion.
	 * @param args
	 */
	public static void main(String[] args) {
		if (control.comprobar()) {
			main();
		} else {
			System.exit(0);
		}
	}

	private static void main() {
		try {
			Utils.setLookAndFeel(Utils.windowsLookAndFeel);
			PlayerController controller = PlayerController.getInstance();
			controller.iniciar();
		} catch (LookAndFeelException ex) {
			Utils.display(ex.getClass().getName() + ": " + ex.getMessage());
			Utils.log(MainPlayer.class.getName(), ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
		}
	}
}
