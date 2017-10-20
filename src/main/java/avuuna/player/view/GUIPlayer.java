package avuuna.player.view;

import javax.swing.*;

/**
 * 
 * @author Avuuna, la Luz del Alba
 *
 */
public class GUIPlayer extends View {
	private static final long serialVersionUID = -422073346876789713L;

	public JFileChooser fileChooser;
	public JMenu fileMenu;
	public JMenuBar menuBar;
	public JMenuItem openItem;

	public GUIPlayer() {
		super("Reproductor de Música - by Avuuna, la Luz del Alba");

		prepareElementosMenu();

		fileChooser = new JFileChooser();
		
		lastConfig();
	}

	private void prepareElementosMenu() {
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		fileMenu = new JMenu();
		fileMenu.setText("Archivo");
		menuBar.add(fileMenu);

		openItem = new JMenuItem();
		openItem.setText("Abrir");
		fileMenu.add(openItem);
	}

}
