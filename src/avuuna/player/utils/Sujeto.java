package avuuna.player.utils;

import java.io.*;
import java.util.*;

/**
 * Clase perteneciente al patron <i>Observador-Observado</b>, en donde esta es el <i>Observado</i>.
 * @author Avuuna, la Luz del Alba
 *
 */
public abstract class Sujeto implements Serializable {
	private static final long serialVersionUID = 6373917034012716471L;

	private final ArrayList<Observador> observers;

	protected Sujeto() {
		observers = new ArrayList<Observador>();
	}

	/**
	 * Adiciona un observador.
	 * @param o
	 */
	public void addObserver(Observador o) {
		observers.add(o);
	}

	/**
	 * Quita un observador.
	 * @param o
	 */
	public void removeObserver(Observador o) {
		observers.remove(o);
	}

	/**
	 * Notifica a todos los observadores de los ultimos cambios.
	 */
	public void notifyObservers() {
		for (Observador o : observers) {
			o.update();
		}
	}
}
