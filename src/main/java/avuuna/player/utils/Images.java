package avuuna.player.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads and caches application images on startup.
 */
public class Images implements Serializable {
    private static final long serialVersionUID = -3401165048323259279L;

    public static final String IMAGES_PATH = "resources/images/";

    public static final String IMG_LOGO = "logo.png";
    public static final String IMG_OPEN = "Open16.png";

    public static final String BTN_PAUSE = "btn-pause";
    public static final String BTN_PLAY  = "btn-play";
    public static final String BTN_PREV  = "btn-prev";
    public static final String BTN_NEXT  = "btn-next";
    public static final String BTN_STOP  = "btn-stop";
    public static final String BTN_PLUS  = "btn-plus";
    public static final String BTN_LESS  = "btn-less";
    public static final String BTN_CLOSE = "btn-close";

    public static final String BTN_REPEAT_NONE = "btn-repeat-none";
    public static final String BTN_REPEAT_ALL = "btn-repeat-all";
    public static final String BTN_REPEAT_ONE = "btn-repeat-one";
    public static final String BTN_SHUFFLE_OFF = "btn-shuffle-off";
    public static final String BTN_SHUFFLE_ON = "btn-shuffle-on";
    public static final String BTN_MOVE_UP   = "btn-move-up";
    public static final String BTN_MOVE_DOWN = "btn-move-down";

    private static final Color ICON_ACTIVE = new Color(30, 120, 215);
    private static final Color ICON_INACTIVE = new Color(150, 150, 150);
    private static final Color ICON_PLAYBACK = new Color(60, 60, 60);
    private static final int SYMBOL_SIZE   = 40;
    private static final int PLAYBACK_SIZE = 48;
    private static final int PLAYLIST_SIZE = 16;

    private static final Map<String, ImageIcon> imageCache;

    static {
        imageCache = new HashMap<>();

        imageCache.put(IMG_LOGO, createImageIcon(IMG_LOGO));
        imageCache.put(IMG_OPEN, createImageIcon(IMG_OPEN));
        imageCache.put(BTN_PLAY, createSymbolIcon("▶", ICON_PLAYBACK, PLAYBACK_SIZE, false));
        imageCache.put(BTN_PAUSE, createSymbolIcon("⏸", ICON_PLAYBACK, PLAYBACK_SIZE, false));
        imageCache.put(BTN_STOP, createSymbolIcon("■", ICON_PLAYBACK, PLAYBACK_SIZE, false));
        imageCache.put(BTN_PREV, createSymbolIcon("⏮", ICON_PLAYBACK, PLAYBACK_SIZE, false));
        imageCache.put(BTN_NEXT, createSymbolIcon("⏭", ICON_PLAYBACK, PLAYBACK_SIZE, false));
        imageCache.put(BTN_PLUS,  createSymbolIcon("+", ICON_PLAYBACK, PLAYLIST_SIZE, false));
        imageCache.put(BTN_LESS,  createSymbolIcon("−", ICON_PLAYBACK, PLAYLIST_SIZE, false));
        imageCache.put(BTN_CLOSE, createSymbolIcon("×", ICON_PLAYBACK, PLAYLIST_SIZE, false));

        imageCache.put(BTN_REPEAT_NONE, createSymbolIcon("↻", ICON_INACTIVE, SYMBOL_SIZE, false));
        imageCache.put(BTN_REPEAT_ALL, createSymbolIcon("↻", ICON_ACTIVE, SYMBOL_SIZE, false));
        imageCache.put(BTN_REPEAT_ONE, createSymbolIcon("↻", ICON_ACTIVE, SYMBOL_SIZE, true));
        imageCache.put(BTN_SHUFFLE_OFF, createSymbolIcon("⇄", ICON_INACTIVE, SYMBOL_SIZE, false));
        imageCache.put(BTN_SHUFFLE_ON, createSymbolIcon("⇄", ICON_ACTIVE, SYMBOL_SIZE, false));
        imageCache.put(BTN_MOVE_UP,   createSymbolIcon("↑", ICON_PLAYBACK, PLAYLIST_SIZE, false));
        imageCache.put(BTN_MOVE_DOWN, createSymbolIcon("↓", ICON_PLAYBACK, PLAYLIST_SIZE, false));
    }

    private static ImageIcon createSymbolIcon(String symbol, Color color, int size, boolean badgeOne) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(color);

        Font font = new Font("Segoe UI Symbol", Font.PLAIN, (int) (size * 0.72));
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int x = (size - fm.stringWidth(symbol)) / 2;
        int y = (size + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(symbol, x, y);

        if (badgeOne) {
            Font badgeFont = new Font(Font.SANS_SERIF, Font.BOLD, (int) (size * 0.28));
            g.setFont(badgeFont);
            FontMetrics bfm = g.getFontMetrics();
            int bx = size - bfm.stringWidth("1") - 1;
            int by = bfm.getAscent();
            g.drawString("1", bx, by);
        }

        g.dispose();
        return new ImageIcon(img);
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
