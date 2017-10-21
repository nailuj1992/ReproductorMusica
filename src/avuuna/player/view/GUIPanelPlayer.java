package avuuna.player.view;

import javax.swing.*;

import avuuna.player.utils.*;

public class GUIPanelPlayer extends JPanel {
	private static final long serialVersionUID = -4999856825283362164L;

	public JPanel panel_detalles;
	public JLabel lbl_cancionActual;
	public JButton btn_play, btn_stop;
	public JButton btn_previous, btn_next;
	public JProgressBar bar_progreso;

	public JPanel panel_volumen;
	public JSlider slider_barraVolumen;

	public GUIPanelPlayer() {
		prepareElementos();
		posicioneElementos();
	}

	private void prepareElementos() {
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

		slider_barraVolumen = new JSlider();
		slider_barraVolumen.setFocusable(false);
	}

	private void posicioneElementos() {
		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

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

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
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

}
