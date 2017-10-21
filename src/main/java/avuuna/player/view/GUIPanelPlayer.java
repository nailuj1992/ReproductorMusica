package avuuna.player.view;

import javax.swing.*;

import avuuna.player.utils.*;

public class GUIPanelPlayer extends JPanel {
	private static final long serialVersionUID = -4999856825283362164L;
	
	public JLabel actualSong;
	public JPanel detailsPanel;
	public JButton nextButton;
	public JButton playButton;
	public JButton previousButton;
	public JProgressBar progressBar;
	public JButton stopButton;
	public JSlider volumeControl;
    public JPanel volumePanel;
	
	public GUIPanelPlayer() {
		prepareElementos();
		posicioneElementos();
	}
	
	private void prepareElementos() {
		actualSong = new JLabel();
		actualSong.setText(Strings.cancionActual);

		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(0);
		progressBar.setStringPainted(true);
		progressBar.setString("00:00 of 00:00");

		previousButton = new JButton();
		previousButton.setText(Strings.prev);

		playButton = new JButton();
		playButton.setText(Strings.play);

		stopButton = new JButton();
		stopButton.setText(Strings.stop);

		nextButton = new JButton();
		nextButton.setText(Strings.next);

		volumeControl = new JSlider();
	}
	
	private void posicioneElementos() {
		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		
		detailsPanel = new JPanel();
		detailsPanel.setBorder(BorderFactory.createTitledBorder(Strings.panelReproduccion));
		
		GroupLayout detailsPanelLayout = new GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(
            detailsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(actualSong, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(previousButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(playButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stopButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nextButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
            .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        detailsPanelLayout.setVerticalGroup(
            detailsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addComponent(actualSong)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(detailsPanelLayout.createSequentialGroup()
                        .addGroup(detailsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(playButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                            .addComponent(stopButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                            .addComponent(previousButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                            .addComponent(nextButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        
        volumePanel = new JPanel();
        volumePanel.setBorder(BorderFactory.createTitledBorder(Strings.panelVolumen));
        
        GroupLayout volumePanelLayout = new GroupLayout(volumePanel);
        volumePanel.setLayout(volumePanelLayout);
        volumePanelLayout.setHorizontalGroup(
            volumePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(volumeControl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        volumePanelLayout.setVerticalGroup(
            volumePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(volumeControl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(volumePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(detailsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(detailsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(volumePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
	}

}
