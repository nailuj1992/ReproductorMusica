package avuuna.player;

import avuuna.player.controller.PlayerController;
import avuuna.player.exception.LookAndFeelException;
import avuuna.player.exception.PlayerException;
import avuuna.player.utils.Utils;

import javax.swing.*;

public class MainPlayer {

    public static final Control control = Control.getInstance("avuuna-player");

    public static void main(String[] args) {
        if (control.check()) {
            launch();
        } else {
            System.exit(0);
        }
    }

    private static void launch() {
        try {
            Utils.setLookAndFeel(Utils.WINDOWS_LOOK_AND_FEEL);
            PlayerController controller = PlayerController.getInstance();
            controller.start(control::shutdown);
        } catch (LookAndFeelException ex) {
            Utils.display(ex.getClass().getName() + ": " + ex.getMessage());
            Utils.log(MainPlayer.class.getName(), ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
