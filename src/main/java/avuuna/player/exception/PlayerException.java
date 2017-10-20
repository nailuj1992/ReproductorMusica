package avuuna.player.exception;

/**
 *
 * @author pegasusmax
 */
public class PlayerException extends Exception {

    public static final String REPEATED_SONG = "Repeated song in the list.";

    public PlayerException(String message) {
        super(message);
    }
}
