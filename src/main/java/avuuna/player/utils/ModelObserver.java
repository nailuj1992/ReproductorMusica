package avuuna.player.utils;

/**
 * Interface for the Observer role in the Observer pattern.
 * Implementations react to state changes published by a {@link ModelSubject}.
 */
public interface ModelObserver {

    void update();
}
