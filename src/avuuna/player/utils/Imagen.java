package avuuna.player.utils;

import java.io.*;
import java.util.*;

import javax.swing.*;

/**
 * Clase encargada de la carga y busqueda de imagenes.
 * @author Avuunita
 *
 */
public class Imagen implements Serializable {
	private static final long serialVersionUID = -3401165048323259279L;

	public static final String rutaImagenes = "resources/images/";

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

	public static final Map<String, ImageIcon> imagenes;

	static {
		imagenes = new HashMap<String, ImageIcon>();

		imagenes.put(IMG_LOGO, createImageIcon(rutaImagenes + IMG_LOGO));
		imagenes.put(IMG_OPEN, createImageIcon(rutaImagenes + IMG_OPEN));
		imagenes.put(BTN_PAUSE, createImageIcon(rutaImagenes + BTN_PAUSE));
		imagenes.put(BTN_PLAY, createImageIcon(rutaImagenes + BTN_PLAY));
		imagenes.put(BTN_PREV, createImageIcon(rutaImagenes + BTN_PREV));
		imagenes.put(BTN_NEXT, createImageIcon(rutaImagenes + BTN_NEXT));
		imagenes.put(BTN_STOP, createImageIcon(rutaImagenes + BTN_STOP));
		imagenes.put(BTN_PLUS, createImageIcon(rutaImagenes + BTN_PLUS));
		imagenes.put(BTN_LESS, createImageIcon(rutaImagenes + BTN_LESS));
		imagenes.put(BTN_CLOSE, createImageIcon(rutaImagenes + BTN_CLOSE));
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
