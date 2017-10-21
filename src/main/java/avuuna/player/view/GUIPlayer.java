package avuuna.player.view;

import java.awt.*;

import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * 
 * @author Avuuna, la Luz del Alba
 *
 */
public class GUIPlayer extends View {
	private static final long serialVersionUID = 422073346876789713L;
	
	public static final String archivo = "Archivo";
	public static final String abrir = "Abrir";

	public JFileChooser fileChooser;
	public JMenu fileMenu;
	public JMenuBar menuBar;
	public JMenuItem openItem;
	
	public GUIPanelPlayer playerPanel;
	public GUIPlaylistPanel playlistPanel;

	public GUIPlayer() {
		super("Reproductor de Música - by Avuuna, la Luz del Alba");

		prepareElementos();
		prepareElementosMenu();

		lastConfig();
	}

	private void prepareElementosMenu() {
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		fileMenu = new JMenu();
		fileMenu.setText(archivo);
		menuBar.add(fileMenu);

		openItem = new JMenuItem();
		openItem.setText(abrir);
		fileMenu.add(openItem);
	}
	
	private void prepareElementos() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos MP3", "mp3");
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		fileChooser.setMultiSelectionEnabled(true);
		
		playerPanel = new GUIPanelPlayer();
		playlistPanel = new GUIPlaylistPanel();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, playerPanel, playlistPanel);
		splitPane.setEnabled(false);
		this.add(splitPane, BorderLayout.CENTER);
	}

}
