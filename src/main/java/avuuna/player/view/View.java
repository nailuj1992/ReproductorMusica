package avuuna.player.view;

import java.awt.*;
import java.io.*;

import javax.swing.*;

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
		setTitle(titulo);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setSize(ANCHO_PANTALLA / 2, ALTO_PANTALLA * 5 / 12);
	}

	protected final void lastConfig() {
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
	}
}
