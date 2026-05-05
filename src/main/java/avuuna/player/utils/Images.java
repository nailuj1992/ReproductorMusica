package avuuna.player.utils;

import java.io.*;
import java.util.*;

import javax.swing.*;

/**
 * Loads and caches application images on startup.
 */
public class Images implements Serializable {
    private static final long serialVersionUID = -3401165048323259279L;

    public static final String IMAGES_PATH = "resources/images/";

    public static final String IMG_LOGO = "logo.png";
    public static final String IMG_OPEN = "Open16.png";

    public static final String BTN_PAUSE = "Pause64.png";
    public static final String BTN_PLAY = "Play64.png";
    public static final String BTN_PREV = "Skip-backward64.png";
    public static final String BTN_NEXT = "Skip-forward64.png";
    public static final String BTN_STOP = "Stop64.png";
    public static final String BTN_PLUS = "Plus16.png";
    public static final String BTN_LESS = "Less16.png";
    public static final String BTN_CLOSE = "Close16.png";

    private static final Map<String, ImageIcon> imageCache;

    static {
        imageCache = new HashMap<String, ImageIcon>();

        imageCache.put(IMG_LOGO,  createImageIcon(IMG_LOGO));
        imageCache.put(IMG_OPEN,  createImageIcon(IMG_OPEN));
        imageCache.put(BTN_PAUSE, createImageIcon(BTN_PAUSE));
        imageCache.put(BTN_PLAY,  createImageIcon(BTN_PLAY));
        imageCache.put(BTN_PREV,  createImageIcon(BTN_PREV));
        imageCache.put(BTN_NEXT,  createImageIcon(BTN_NEXT));
        imageCache.put(BTN_STOP,  createImageIcon(BTN_STOP));
        imageCache.put(BTN_PLUS,  createImageIcon(BTN_PLUS));
        imageCache.put(BTN_LESS,  createImageIcon(BTN_LESS));
        imageCache.put(BTN_CLOSE, createImageIcon(BTN_CLOSE));
    }

    private static ImageIcon createImageIcon(String name) {
        java.net.URL url = Images.class.getResource("/images/" + name);
        if (url != null) {
            return new ImageIcon(url);
        }
        System.err.println("Could not find image: " + name);
        return null;
    }

    public static ImageIcon getImageIcon(String path) {
        return imageCache.get(path);
    }
}
