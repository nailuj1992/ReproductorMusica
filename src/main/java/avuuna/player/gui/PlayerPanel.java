package avuuna.player.gui;

import avuuna.player.exception.PlayerException;
import avuuna.player.model.*;
import avuuna.player.utils.*;

import javax.swing.*;
import javazoom.jl.player.basic.*;

/**
 *
 * @author Avuuna, la Luz del Alba
 * 
 */
public class PlayerPanel extends JPanel implements Observador {
	private static final long serialVersionUID = -1546047517929221938L;
	
	public static final String panelReproduccion = "Panel de Reproducción";
	public static final String panelVolumen = "Panel de Volumen";
	public static final String cancionActual = "Canción actual: ";
	public static final String play = "Play";
	public static final String pause = "Pause";
	public static final String stop = "Stop";
	public static final String next = "Next";
	public static final String prev = "Prev";
	
	private final Player player;
    private boolean running = false;
    private final Thread progressThread;

    public PlayerPanel(final Player player) {
        this.player = player;
        initComponents();
        progressBar.setMinimum(0);
        progressBar.setMaximum(0);
        progressBar.setStringPainted(true);
        progressBar.setString("00:00 of 00:00");
        progressThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (player.getActual() != null) {
                        progressBar.setValue((int) player.getProgressBytes());
                        try {
                            progressBar.setString(Utils.formatTime(player.getProgressTime()) + " of " + Utils.formatTime(player.getActual().getDuration()));
                        } catch (Exception ex) {
                            Utils.display("Exception found -> " + ex.getMessage());
                        }
                    } else {
                        progressBar.setValue(0);
                        progressBar.setString("00:00 of 00:00");
                    }
                }
            }
        };
        progressThread.start();
    }

    private void playSong() {
        try {
            player.play();
        } catch (BasicPlayerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR_PLAYING_SONG, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pauseSong() {
        try {
            player.pause();
        } catch (BasicPlayerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR_PAUSING_SONG, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resumeSong() {
        try {
            player.resume();
        } catch (BasicPlayerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR_RESUMING_SONG, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopSong() {
        try {
            player.stop();
        } catch (BasicPlayerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR_STOPPING_SONG, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void nextSong() {
        try {
            player.next();
        } catch (BasicPlayerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR_NEXT_SONG, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void previousSong() {
        try {
            player.previous();
        } catch (BasicPlayerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR_PREV_SONG, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setVolume() {
        try {
            player.getPlayer().setGain((double) volumeControl.getValue() / 100);
        } catch (BasicPlayerException ex) {
//            JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR_VOLUME_SONG, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update() {
        if (player.getActual() != null) {
            switch (player.getActualEvent()) {
                case BasicPlayerEvent.RESUMED:
                    playButton.setText(pause);
                    break;
                case BasicPlayerEvent.PAUSED:
                    playButton.setText(play);
                    break;
                case BasicPlayerEvent.PLAYING:
                    playButton.setText(pause);
                    running = true;
                    break;
                case BasicPlayerEvent.STOPPED:
                    playButton.setText(play);
                    running = false;
                    break;
                case BasicPlayerEvent.OPENED:
                    actualSong.setText(cancionActual + player.getActual().getName());
                    progressBar.setMaximum((int) player.getActual().getBytesLength());
                    progressBar.setString("00:00 of " + Utils.formatTime(player.getActual().getDuration()));
                    if (!running && player.getActual() != null) {
                        playSong();
                    }
                    setVolume();
                    break;
            }
        } else {
            actualSong.setText(cancionActual);
            progressBar.setMaximum(0);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        detailsPanel = new javax.swing.JPanel();
        actualSong = new javax.swing.JLabel();
        playButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        nextButton = new javax.swing.JButton();
        previousButton = new javax.swing.JButton();
        volumePanel = new javax.swing.JPanel();
        volumeControl = new javax.swing.JSlider();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        detailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(panelReproduccion));

        actualSong.setText(cancionActual);

        playButton.setText(play);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        stopButton.setText(stop);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        nextButton.setText(next);
        nextButton.setActionCommand("");
        nextButton.setMaximumSize(new java.awt.Dimension(44, 29));
        nextButton.setMinimumSize(new java.awt.Dimension(44, 29));
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        previousButton.setText(prev);
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout detailsPanelLayout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(actualSong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(previousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
            .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        detailsPanelLayout.setVerticalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addComponent(actualSong)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailsPanelLayout.createSequentialGroup()
                        .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(previousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        volumePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(panelVolumen));

        volumeControl.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                volumeControlStateChanged(evt);
            }
        });

        javax.swing.GroupLayout volumePanelLayout = new javax.swing.GroupLayout(volumePanel);
        volumePanel.setLayout(volumePanelLayout);
        volumePanelLayout.setHorizontalGroup(
            volumePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(volumeControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        volumePanelLayout.setVerticalGroup(
            volumePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(volumeControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(volumePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(volumePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
		switch (playButton.getText()) {
		case play:
			if (!running && player.getActual() != null) {
				playSong();
			} else if (running) {
				resumeSong();
			}
			break;
		case pause:
			pauseSong();
			break;
		}
    }//GEN-LAST:event_playButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        stopSong();
    }//GEN-LAST:event_stopButtonActionPerformed

    private void volumeControlStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_volumeControlStateChanged
        setVolume();
    }//GEN-LAST:event_volumeControlStateChanged

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        previousSong();
    }//GEN-LAST:event_previousButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        nextSong();
    }//GEN-LAST:event_nextButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel actualSong;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton playButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton stopButton;
    private javax.swing.JSlider volumeControl;
    private javax.swing.JPanel volumePanel;
    // End of variables declaration//GEN-END:variables
}
