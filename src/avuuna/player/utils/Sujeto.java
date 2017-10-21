package avuuna.player.utils;

import java.io.*;
import java.util.*;

/**
 * 
 * @author Avuuna, la Luz del Alba
 *
 */
public abstract class Sujeto implements Serializable {
	private static final long serialVersionUID = 6373917034012716471L;

	private final ArrayList<Observador> observers;

	public Sujeto() {
		observers = new ArrayList<Observador>();
	}

	public void addObserver(Observador o) {
		observers.add(o);
	}

	public void removeObserver(Observador o) {
		observers.remove(o);
	}

	public void notifyObservers() {
		for (Observador o : observers) {
			o.update();
		}
	}
}
