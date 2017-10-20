package avuuna.player.utils;

/**
 *
 * @author pegasusmax
 */
import java.util.*;

public abstract class Sujeto {

    private final ArrayList<Observador> observers;

    public Sujeto() {
        observers = new ArrayList();
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
