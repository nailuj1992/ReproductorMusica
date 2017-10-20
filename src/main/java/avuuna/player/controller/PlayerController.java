package avuuna.player.controller;

import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.*;

import avuuna.player.exception.*;
import avuuna.player.model.*;
import avuuna.player.utils.Utils;
import avuuna.player.view.*;
import javazoom.jl.player.basic.*;

public class PlayerController implements Serializable {
	private static final long serialVersionUID = 232492703123683857L;

	private Player model;
	private GUIPlayer view;

	public PlayerController(BasicPlayer basicPlayer) {
		model = Player.getInstance(basicPlayer);
		view = new GUIPlayer();

		definaAcciones();
	}

	private void definaAcciones() {
		view.openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));

		view.openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				openItemActionPerformed(evt);
			}
		});
	}

	private void openItemActionPerformed(ActionEvent evt) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos MP3", "mp3");
		view.fileChooser.setFileFilter(filter);
		view.fileChooser.setMultiSelectionEnabled(true);
		int seleccion = view.fileChooser.showOpenDialog(view);
		if (seleccion == JFileChooser.APPROVE_OPTION) {
			File[] songs = view.fileChooser.getSelectedFiles();
			if (songs.length > 0) {
				for (File s : songs) {
					if (s.getAbsolutePath().endsWith(".mp3")) {
						try {
							Song song = new Song(s.getAbsolutePath());
							model.addSong(song);
							if (model.getActual() == null) {
								model.setActual(song);
								model.open(model.getActual());
							}
						} catch (BasicPlayerException | PlayerException ex) {
							Utils.log(PlayerException.ERROR_OPENING_SONG, ex);
//							JOptionPane.showMessageDialog(null, ex.getMessage(), PlayerException.ERROR_OPENING_SONG, JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		}
	}

}
