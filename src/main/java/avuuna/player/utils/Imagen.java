package avuuna.player.utils;

import java.io.*;
import java.util.*;

import javax.swing.*;

public class Imagen implements Serializable {
	private static final long serialVersionUID = -3401165048323259279L;

	public static final String rutaImagenes = "resources/images/";

	public static final String IMG_LOGO = "logo.png";
	public static final String IMG_OPEN = "Open16.png";
	public static final String BTN_PAUSE = "Pause64.png";
	public static final String BTN_PLAY = "Play64.png";
	public static final String BTN_CLOSE = "Close16.png";
	public static final String BTN_PREV = "Skip-backward64.png";
	public static final String BTN_NEXT = "Skip-forward64.png";
	public static final String BTN_STOP = "Stop64.png";

	public static final Map<String, ImageIcon> imagenes;

	static {
		imagenes = new HashMap<String, ImageIcon>();

		ImageIcon img_logo = createImageIcon(rutaImagenes + IMG_LOGO);
		ImageIcon img_open = createImageIcon(rutaImagenes + IMG_OPEN);
		ImageIcon btn_pause = createImageIcon(rutaImagenes + BTN_PAUSE);
		ImageIcon btn_play = createImageIcon(rutaImagenes + BTN_PLAY);
		ImageIcon btn_close = createImageIcon(rutaImagenes + BTN_CLOSE);
		ImageIcon btn_prev = createImageIcon(rutaImagenes + BTN_PREV);
		ImageIcon btn_next = createImageIcon(rutaImagenes + BTN_NEXT);
		ImageIcon btn_stop = createImageIcon(rutaImagenes + BTN_STOP);

		imagenes.put(IMG_LOGO, img_logo);
		imagenes.put(IMG_OPEN, img_open);
		imagenes.put(BTN_PAUSE, btn_pause);
		imagenes.put(BTN_PLAY, btn_play);
		imagenes.put(BTN_CLOSE, btn_close);
		imagenes.put(BTN_PREV, btn_prev);
		imagenes.put(BTN_NEXT, btn_next);
		imagenes.put(BTN_STOP, btn_stop);
	}

	private static ImageIcon createImageIcon(String path) {
		ImageIcon icono = null;
		if (path != null) {
			icono = new ImageIcon(path);
		} else {
			System.err.println("No se pudo encontrar el archivo: " + path);
			icono = null;
		}
		return icono;
	}

}
