package avuuna.player.utils;

/**
 * Interface for the Subject role in the Observer pattern.
 * Implementations manage a list of {@link ModelObserver}s and notify them of state changes.
 */
public interface ModelSubject {

    void addObserver(ModelObserver o);

    void removeObserver(ModelObserver o);

    void notifyObservers();
}
