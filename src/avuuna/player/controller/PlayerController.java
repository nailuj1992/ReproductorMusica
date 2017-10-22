package avuuna.player.controller;

import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;

import avuuna.player.exception.*;
import avuuna.player.model.*;
import avuuna.player.utils.*;
import avuuna.player.view.*;
import javazoom.jl.player.basic.*;

/**
 * Esta clase es el controlador, punto clave del patron MVC (Model - View - Controller).<br><br>
 * Esta clase tambien hace parte del patron <i>Observador-Observado</i>, en donde esta es el <i>Observador</i>.
 * @author Avuunita
 *
 */
public class PlayerController implements Serializable, Observador {
	private static final long serialVersionUID = 232492703123683857L;

	public Reproductor model;
	public GUIPlayer view;
	
	private boolean running;

	public PlayerController() {
		model = Reproductor.getInstance();
		view = new GUIPlayer();

		definaAcciones();
	}
	
	public final void iniciar() {
		running = false;
		model.addObserver(this);

		accionarThread();

		view.initView();
	}

	/**
	 * Declara e inicia el hilo (<code>Thread</code>) encargado de actualizar el estado de la barra de progreso de 
	 * reproduccion de la cancion.
	 */
	private void accionarThread() {
		Thread progressThread = new Thread() {

			@Override
			public void run() {
				while (true) {
					if (model.getActual() != null) {
						view.panel_player.bar_progreso.setValue((int) model.getProgressBytes());
						try {
							view.panel_player.bar_progreso.setString(Utils.formatTime(model.getProgressTime()) + Strings.DE + Utils.formatTime(model.getActual().getDuration()));
						} catch (Exception ex) {
							Utils.display("Exception found -> " + ex.getMessage());
						}
					} else {
						view.panel_player.bar_progreso.setValue(0);
						view.panel_player.bar_progreso.setString(Strings.TIEMPO_CERO);
					}
				}
			}
		};
		progressThread.start();
	}

	/**
	 * Definiendo todas las acciones que tendran los componentes de la vista, enlazandolas con el modelo.
	 */
	private void definaAcciones() {
		view.openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));

		view.openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				accionAbrirCancion();
			}
		});
		
		view.panel_player.btn_play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	switch (view.panel_player.btn_play.getToolTipText()) {
        		case Strings.play:
        			if (!running && model.getActual() != null) {
        				playSong();
        			} else if (running) {
        				resumeSong();
        			}
        			break;
        		case Strings.pause:
        			pauseSong();
        			break;
            	default:
            		break;
        		}
            }
        });
		
		view.panel_player.btn_stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	stopSong();
            }
        });
		
		view.panel_player.btn_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                nextSong();
            }
        });
		
		view.panel_player.btn_previous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	previousSong();
            }
        });
		
		view.panel_player.slider_barraVolumen.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
            	setVolume();
            }
        });
		
		view.panel_playlist.list_canciones.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            	if (evt.getClickCount() == 2) {// Doble clic
                    String selected = (String) view.panel_playlist.list_canciones.getSelectedValue();
                    if (!selected.contains(Strings.ACTUAL)) {
                        openSong(selected);
                    }
                }
            }
        });
		
		view.panel_playlist.btn_adicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	accionAbrirCancion();
            }
		});
		
		view.panel_playlist.btn_quitarUno.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getSongs().size() > 0) {
					String selected = view.panel_playlist.list_canciones.getSelectedValue();
					if (selected != null) {
						int seleccion = JOptionPane.showConfirmDialog(view, Strings.confirmarQuitarCancion,
								Strings.CONFIRMAR, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
						if (seleccion == JOptionPane.YES_OPTION) {
							selected = selected.replace(Strings.ACTUAL, "");
							removeSong(selected);
						}
					} else {
						JOptionPane.showMessageDialog(null, PlayerException.ERROR_NO_SONG_SELECTED,
								PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, PlayerException.ERROR_NO_SONGS_LIST, PlayerException.ERROR,
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		view.panel_playlist.btn_borrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (model.getSongs().size() > 0) {
					if (!view.panel_playlist.dlm_datosLista.isEmpty()) {
						int seleccion = JOptionPane.showConfirmDialog(view, Strings.confirmarBorrarLista,
								Strings.CONFIRMAR, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
						if (seleccion == JOptionPane.YES_OPTION) {
							clearPlaylist();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, PlayerException.ERROR_NO_SONGS_LIST, PlayerException.ERROR,
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * Permite buscar una cancion (formato <b>MP3</b>) en el equipo, para posteriormente abrirla.
	 */
	private void accionAbrirCancion() {
		int seleccion = view.fileChooser.showOpenDialog(view);
		if (seleccion == JFileChooser.APPROVE_OPTION) {
			File[] songs = view.fileChooser.getSelectedFiles();
			if (songs.length > 0) {
				for (File s : songs) {
					if (s.getAbsolutePath().endsWith(".mp3")) {
						try {
							Cancion song = new Cancion(s.getAbsolutePath());
							model.addSong(song);
							if (model.getActual() == null) {
								model.setActual(song);
								model.open(model.getActual());
							}
						} catch (BasicPlayerException | PlayerException ex) {
							Utils.log(PlayerException.ERROR_OPENING_SONG, ex);
							if (songs.length == 1) {
								Utils.log(PlayerException.ERROR_OPENING_SONG, ex);
								JOptionPane.showMessageDialog(null, PlayerException.ERROR_OPENING_SONG, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Permite reproducir la cancion actual.
	 */
    private void playSong() {
        try {
            model.play();
        } catch (BasicPlayerException ex) {
			Utils.log(PlayerException.ERROR_PLAYING_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_PLAYING_SONG, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite pausar la cancion actual.
     */
    private void pauseSong() {
        try {
        	model.pause();
        } catch (BasicPlayerException ex) {
			Utils.log(PlayerException.ERROR_PAUSING_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_PAUSING_SONG, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite reanudar la cancion pausada actual.
     */
    private void resumeSong() {
        try {
        	model.resume();
        } catch (BasicPlayerException ex) {
			Utils.log(PlayerException.ERROR_RESUMING_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_RESUMING_SONG, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite detener la cancion actual.
     */
    private void stopSong() {
        try {
        	model.stop();
        } catch (BasicPlayerException ex) {
			Utils.log(PlayerException.ERROR_STOPPING_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_STOPPING_SONG, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite pasar a la siguiente cancion.
     */
    private void nextSong() {
        try {
        	model.next();
        } catch (BasicPlayerException ex) {
			Utils.log(PlayerException.ERROR_NEXT_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_NEXT_SONG, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite pasar a la cancion anterior.
     */
    private void previousSong() {
        try {
        	model.previous();
        } catch (BasicPlayerException ex) {
			Utils.log(PlayerException.ERROR_PREV_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_PREV_SONG, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite cambiar el volumen al reproductor.
     */
    private void setVolume() {
        try {
        	model.setVolume((double) view.panel_player.slider_barraVolumen.getValue() / 100);
        } catch (BasicPlayerException ex) {
			Utils.log(PlayerException.ERROR_VOLUME_SONG, ex);
//            JOptionPane.showMessageDialog(null, PlayerException.ERROR_VOLUME_SONG, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite limpiar toda la lista de reproduccion.
     */
    private void clearPlaylist() {
        try {
            model.clearList();
        } catch (BasicPlayerException ex) {
			Utils.log(PlayerException.ERROR_CLEAR_LIST, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_CLEAR_LIST, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite abrir una cancion de la lista.
     * @param selected Cancion seleccionada de la lista.
     */
    private void openSong(String selected) {
        try {
            Cancion song = model.getSong(selected);
            model.setActual(song);
            model.stop();
            model.open(song);
        } catch (BasicPlayerException ex) {
			Utils.log(PlayerException.ERROR_OPENING_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_OPENING_SONG, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removeSong(String selected) {
    	try {
    		Cancion song = model.getSong(selected);
    		model.removeSong(song);
        } catch (BasicPlayerException ex) {
			Utils.log(PlayerException.ERROR_REMOVE_SONG, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_REMOVE_SONG, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
        } catch (PlayerException ex) {
			Utils.log(PlayerException.ERROR_REMOVE_SONG_NO_EXISTS, ex);
            JOptionPane.showMessageDialog(null, PlayerException.ERROR_REMOVE_SONG_NO_EXISTS, PlayerException.ERROR, JOptionPane.ERROR_MESSAGE);
		}
    }

	@Override
	public void update() {
		if (model.getActual() != null) {
			switch (model.getActualEvent()) {
			case BasicPlayerEvent.RESUMED:
				view.panel_player.btn_play.setToolTipText(Strings.pause);
				view.panel_player.btn_play.setIcon(Imagen.imagenes.get(Imagen.BTN_PAUSE));
				break;
			case BasicPlayerEvent.PAUSED:
				view.panel_player.btn_play.setToolTipText(Strings.play);
				view.panel_player.btn_play.setIcon(Imagen.imagenes.get(Imagen.BTN_PLAY));
				break;
			case BasicPlayerEvent.PLAYING:
				view.panel_player.btn_play.setToolTipText(Strings.pause);
				view.panel_player.btn_play.setIcon(Imagen.imagenes.get(Imagen.BTN_PAUSE));
				running = true;
				break;
			case BasicPlayerEvent.STOPPED:
				view.panel_player.btn_play.setToolTipText(Strings.play);
				view.panel_player.btn_play.setIcon(Imagen.imagenes.get(Imagen.BTN_PLAY));
				running = false;
				break;
			case BasicPlayerEvent.OPENED:
				view.panel_player.lbl_cancionActual.setText(Strings.cancionActual + model.getActual().getName());
				view.panel_player.bar_progreso.setMaximum((int) model.getActual().getBytesLength());
				view.panel_player.bar_progreso.setString(Strings.CERO + Strings.DE + Utils.formatTime(model.getActual().getDuration()));
				if (!running && model.getActual() != null) {
					playSong();
				}
				setVolume();
				break;
			default:
				break;
			}
        } else {
        	view.panel_player.lbl_cancionActual.setText(Strings.cancionActual);
        	view.panel_player.bar_progreso.setMaximum(0);
        }
		view.panel_player.repaint();
		
		view.panel_playlist.dlm_datosLista.clear();
        for (Cancion song : model.getSongs()) {
            String resp = song.getName();
            if (model.getActual() != null && model.getActual().equals(song)) {
                resp = Strings.ACTUAL + resp;
            }
            view.panel_playlist.dlm_datosLista.addElement(resp);
        }
        view.panel_playlist.repaint();
	}

}
