package avuuna.player;

import avuuna.player.controller.*;
import avuuna.player.exception.*;
import avuuna.player.utils.*;

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
			
			PlayerController controller = new PlayerController(basicPlayer);
			controller.model.addObserver(controller);
		} catch (LookAndFeelException ex) {
			Utils.display(ex.getClass().getName() + ": " + ex.getMessage());
			Utils.log(MainPlayer.class.getName(), ex);
		}
	}
}
