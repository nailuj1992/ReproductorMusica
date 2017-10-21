package avuuna.player.view;

import java.awt.*;
import java.io.*;

import javax.swing.*;

import avuuna.player.utils.*;

/**
 * Clase madre de las interfaces graficas, ventana madre donde se alojara todo componente grafico.<br>
 * Esta clase hace parte del patron MVC (Model - View - Controller).
 * @author Avuunita
 *
 */
public abstract class View extends JFrame implements Serializable {
	private static final long serialVersionUID = 8317884983802007076L;

	public static final int ANCHO_PANTALLA;
	public static final int ALTO_PANTALLA;

	static {
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		ANCHO_PANTALLA = (int) pantalla.getWidth();
		ALTO_PANTALLA = (int) pantalla.getHeight() - 35;
	}

	public View(String titulo) {
		super(titulo);
		setIconImage(Imagen.imagenes.get(Imagen.IMG_LOGO).getImage());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(ANCHO_PANTALLA / 2, ALTO_PANTALLA * 5 / 12);
	}

	/**
	 * Realiza los ultimos ajustes a la ventana, y luego la inicia.
	 */
	public final void initView() {
		pack();
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
	}
}
