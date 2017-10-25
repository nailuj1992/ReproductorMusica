package avuuna.player.view;

import java.awt.*;

import javax.swing.*;
import javax.swing.filechooser.*;

import avuuna.player.utils.*;

/**
 * Esta clase es una ventana, objeto de tipo <code>View</code>, asi que por tanto hace parte del patron MVC.
 * @author Avuuna, la Luz del Alba
 *
 */
public class GUIPlayer extends View {
	private static final long serialVersionUID = 422073346876789713L;

	public JFileChooser fileChooser;
	public JMenu fileMenu;
	public JMenuBar menuBar;
	public JMenuItem openItem;

	// Panel de Reproduccion
	public JPanel panel_player;

	//// Panel de control Principal
	public JPanel panel_detalles;
	public JLabel lbl_cancionActual;
	public JButton btn_play, btn_stop;
	public JButton btn_previous, btn_next;
	public JButton btn_repeat, btn_random;
	public JProgressBar bar_progreso;

	//// Panel de Volumen
	public JPanel panel_volumen;
	public JSlider slider_barraVolumen;

	// Panel de Lista de Reproduccion
	public JPanel panel_playlist;

	//// Lista de Reproduccion
	public JScrollPane scroll_lista;
	public JList<String> list_canciones;
	public DefaultListModel<String> dlm_datosLista;

	//// Botones de la Lista
	public JButton btn_adicionar, btn_quitarUno, btn_borrar, btn_moverArriba, btn_moverAbajo;

	public GUIPlayer() {
		super("Reproductor de Música - by Avuuna, la Luz del Alba");

		prepareElementos();
		prepareElementosMenu();
	}

	/**
	 * Declara y posiciona los elementos del menu.
	 */
	private void prepareElementosMenu() {
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		fileMenu = new JMenu();
		fileMenu.setText(Strings.archivo);
		menuBar.add(fileMenu);

		openItem = new JMenuItem();
		openItem.setText(Strings.abrir);
		openItem.setIcon(Imagen.imagenes.get(Imagen.IMG_OPEN));
		fileMenu.add(openItem);
	}

	/**
	 * Declara y posiciona todos los elementos graficos (excepto del menu).
	 */
	private void prepareElementos() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos MP3", "mp3");
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		panel_player = new JPanel();
		panel_playlist = new JPanel();

		prepareElementosPanelReproduccion();
		posicioneElementosPanelReproduccion();

		prepareElementosPanelLista();
		posicioneElementosPanelLista();

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel_player, panel_playlist);
		splitPane.setEnabled(false);
		this.add(splitPane, BorderLayout.CENTER);
	}

	/**
	 * Declara todos los elementos graficos del panel de reproduccion.
	 */
	private void prepareElementosPanelReproduccion() {
		lbl_cancionActual = new JLabel();
		lbl_cancionActual.setText(Strings.cancionActual);

		bar_progreso = new JProgressBar();
		bar_progreso.setMinimum(0);
		bar_progreso.setMaximum(0);
		bar_progreso.setStringPainted(true);
		bar_progreso.setString(Strings.TIEMPO_CERO);

		btn_previous = new JButton();
		btn_previous.setToolTipText(Strings.prev);
		btn_previous.setIcon(Imagen.imagenes.get(Imagen.BTN_PREV));
		btn_previous.setFocusable(false);

		btn_play = new JButton();
		btn_play.setToolTipText(Strings.play);
		btn_play.setIcon(Imagen.imagenes.get(Imagen.BTN_PLAY));
		btn_play.setFocusable(false);

		btn_stop = new JButton();
		btn_stop.setToolTipText(Strings.stop);
		btn_stop.setIcon(Imagen.imagenes.get(Imagen.BTN_STOP));
		btn_stop.setFocusable(false);

		btn_next = new JButton();
		btn_next.setToolTipText(Strings.next);
		btn_next.setIcon(Imagen.imagenes.get(Imagen.BTN_NEXT));
		btn_next.setFocusable(false);

		btn_repeat = new JButton();
		btn_repeat.setToolTipText(Strings.noRepeat);
		btn_repeat.setText("R No");
		// btn_repeat.setIcon(Imagen.imagenes.get(Imagen.BTN_));
		btn_repeat.setFocusable(false);

		btn_random = new JButton();
		btn_random.setToolTipText(Strings.randomOff);
		btn_random.setText("Rand OFF");
		// btn_random.setIcon(Imagen.imagenes.get(Imagen.BTN_));
		btn_random.setFocusable(false);

		slider_barraVolumen = new JSlider();
		slider_barraVolumen.setFocusable(false);
	}

	/**
	 * Posicionar todos los elementos dentro del panel de reproduccion.
	 */
	private void posicioneElementosPanelReproduccion() {
		panel_player.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

		panel_detalles = new JPanel();
		panel_detalles.setBorder(BorderFactory.createTitledBorder(Strings.panelReproduccion));

		GroupLayout detailsPanelLayout = new GroupLayout(panel_detalles);
		panel_detalles.setLayout(detailsPanelLayout);
		detailsPanelLayout.setHorizontalGroup(detailsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(lbl_cancionActual, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(detailsPanelLayout.createSequentialGroup().addGap(80, 80, 80)
						.addComponent(btn_previous, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(btn_play, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(btn_stop, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(btn_next, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(btn_repeat, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(btn_random, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(80, Short.MAX_VALUE))
				.addComponent(bar_progreso, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		detailsPanelLayout.setVerticalGroup(detailsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(detailsPanelLayout.createSequentialGroup().addGap(7, 7, 7).addComponent(lbl_cancionActual)
						.addGap(5, 5, 5)
						.addComponent(bar_progreso, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addGap(10, 10, 10)
						.addGroup(detailsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(detailsPanelLayout.createSequentialGroup()
										.addGroup(detailsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(btn_play, GroupLayout.PREFERRED_SIZE, 65,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(btn_stop, GroupLayout.PREFERRED_SIZE, 65,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(btn_previous, GroupLayout.PREFERRED_SIZE, 65,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(btn_next, GroupLayout.PREFERRED_SIZE, 65,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(btn_repeat, GroupLayout.PREFERRED_SIZE, 65,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(btn_random, GroupLayout.PREFERRED_SIZE, 65,
														GroupLayout.PREFERRED_SIZE))
										.addGap(7, 7, Short.MAX_VALUE)))
						.addContainerGap()));

		panel_volumen = new JPanel();
		panel_volumen.setBorder(BorderFactory.createTitledBorder(Strings.panelVolumen));

		GroupLayout volumePanelLayout = new GroupLayout(panel_volumen);
		panel_volumen.setLayout(volumePanelLayout);
		volumePanelLayout.setHorizontalGroup(
				volumePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(slider_barraVolumen,
						GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		volumePanelLayout.setVerticalGroup(
				volumePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(slider_barraVolumen,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));

		GroupLayout layout = new GroupLayout(panel_player);
		panel_player.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(panel_volumen, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(panel_detalles, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(panel_detalles, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(panel_volumen,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
	}

	/**
	 * Declara todos los elementos graficos del panel de lista de reproduccion.
	 */
	private void prepareElementosPanelLista() {
		dlm_datosLista = new DefaultListModel<String>();

		list_canciones = new JList<String>();
		list_canciones.setModel(dlm_datosLista);
		list_canciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scroll_lista = new JScrollPane();
		scroll_lista.setViewportView(list_canciones);

		btn_adicionar = new JButton();
		btn_adicionar.setToolTipText(Strings.adicionarCancion);
		btn_adicionar.setIcon(Imagen.imagenes.get(Imagen.BTN_PLUS));
		btn_adicionar.setFocusable(false);

		btn_quitarUno = new JButton();
		btn_quitarUno.setToolTipText(Strings.quitarCancion);
		btn_quitarUno.setIcon(Imagen.imagenes.get(Imagen.BTN_LESS));
		btn_quitarUno.setFocusable(false);

		btn_borrar = new JButton();
		btn_borrar.setToolTipText(Strings.borrarLista);
		btn_borrar.setIcon(Imagen.imagenes.get(Imagen.BTN_CLOSE));
		btn_borrar.setFocusable(false);

		btn_moverArriba = new JButton();
		btn_moverArriba.setToolTipText(Strings.moverArriba);
		btn_moverArriba.setText("↑");
//		btn_moverArriba.setIcon(Imagen.imagenes.get(Imagen.BTN_));
		btn_moverArriba.setFocusable(false);

		btn_moverAbajo = new JButton();
		btn_moverAbajo.setToolTipText(Strings.moverAbajo);
		btn_moverAbajo.setText("↓");
//		btn_moverAbajo.setIcon(Imagen.imagenes.get(Imagen.BTN_));
		btn_moverAbajo.setFocusable(false);
	}

	/**
	 * Posicionar todos los elementos dentro del panel de lista de reproduccion.
	 */
	private void posicioneElementosPanelLista() {
		panel_playlist.setBorder(BorderFactory.createTitledBorder(Strings.listaReproduccion));
		panel_playlist.setMinimumSize(new Dimension(100, 100));
		panel_playlist.setPreferredSize(new Dimension(285, 23));

		GroupLayout layout = new GroupLayout(panel_playlist);
		panel_playlist.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(scroll_lista, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(btn_adicionar)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btn_quitarUno)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btn_borrar)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btn_moverArriba)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btn_moverAbajo)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addComponent(scroll_lista, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
						.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btn_adicionar)
								.addComponent(btn_quitarUno).addComponent(btn_borrar).addComponent(btn_moverArriba)
								.addComponent(btn_moverAbajo))
						.addGap(0, 0, Short.MAX_VALUE)))
				.addContainerGap()));

		panel_playlist.getAccessibleContext().setAccessibleParent(panel_playlist);
	}

}
