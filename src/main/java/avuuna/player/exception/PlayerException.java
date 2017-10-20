package avuuna.player.exception;

/**
 *
 * @author Avuuna, la Luz del Alba
 */
public class PlayerException extends Exception {
	private static final long serialVersionUID = -6544648361382072128L;
	
	public static final String REPEATED_SONG = "Repeated song in the list.";

    public PlayerException(String message) {
        super(message);
    }
}
