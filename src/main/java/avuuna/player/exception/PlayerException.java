package avuuna.player.exception;

/**
 *
 * @author Avuuna, la Luz del Alba
 */
public class PlayerException extends Exception {
	private static final long serialVersionUID = -6544648361382072128L;
	
	public static final String REPEATED_SONG = "Canción repetida en la lista.";
	public static final String ERROR_OPENING_SONG = "Ha ocurrido un error al momento de abrir la canción.";

    public PlayerException(String message) {
        super(message);
    }
}
