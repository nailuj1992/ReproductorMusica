package avuuna.player.view;

import java.awt.*;

import javax.swing.*;

import avuuna.player.utils.*;

/**
 * Panel donde se encuentra la lista de reproduccion.
 * @author Avuunita
 *
 */
public class GUIPanelPlaylist extends JPanel {
	private static final long serialVersionUID = -6374452046140600636L;

	public JButton btn_adicionar, btn_quitarUno, btn_borrar;

	public JScrollPane scroll_lista;
	public JList<String> list_canciones;
	public DefaultListModel<String> dlm_datosLista;

	public GUIPanelPlaylist() {
		prepareElementos();
		posicioneElementos();
	}

	/**
	 * Declara todos los elementos graficos.
	 */
	private void prepareElementos() {
		this.dlm_datosLista = new DefaultListModel<String>();

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
	}

	/**
	 * Posicionar todos los elementos dentro del panel.
	 */
	private void posicioneElementos() {
		setBorder(BorderFactory.createTitledBorder(Strings.listaReproduccion));
		setMinimumSize(new Dimension(100, 100));
		setPreferredSize(new Dimension(260, 23));

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(scroll_lista, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup().addGap(50, 50, 50).addComponent(btn_adicionar)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btn_quitarUno)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btn_borrar)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addComponent(scroll_lista, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
						.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btn_adicionar)
								.addComponent(btn_quitarUno).addComponent(btn_borrar))
						.addGap(0, 0, Short.MAX_VALUE)))
				.addContainerGap()));

		getAccessibleContext().setAccessibleParent(this);
	}

}
