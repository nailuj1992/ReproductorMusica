package avuuna.player.view;

import avuuna.player.utils.Images;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Base class for all application windows.
 * Part of the MVC View layer.
 */
public abstract class View extends JFrame implements Serializable {
    private static final long serialVersionUID = 8317884983802007076L;

    public static final int SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT;

    static {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH = (int) screen.getWidth();
        SCREEN_HEIGHT = (int) screen.getHeight() - 35;
    }

    public View(String title) {
        super(title);

        setIconImage(Images.getImageIcon(Images.IMG_LOGO).getImage());
        setSize(SCREEN_WIDTH / 2, SCREEN_HEIGHT * 5 / 12);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    /**
     * Finalizes window setup and makes it visible.
     */
    public final void initView() {
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
    }
}
