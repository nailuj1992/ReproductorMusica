package avuuna.player;

import javax.swing.*;

import avuuna.player.controller.*;
import avuuna.player.exception.*;
import avuuna.player.utils.*;

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
            controller.start(() -> control.shutdown());
        } catch (LookAndFeelException ex) {
            Utils.display(ex.getClass().getName() + ": " + ex.getMessage());
            Utils.log(MainPlayer.class.getName(), ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
