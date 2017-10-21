package avuuna.player.exception;

/**
 *
 * @author Avuuna, la Luz del Alba
 */
public class PlayerException extends Exception {
	private static final long serialVersionUID = -6544648361382072128L;

	public static final String ERROR = "Error";
	public static final String REPEATED_SONG = "Canción repetida en la lista.";
	public static final String ERROR_OPENING_SONG = "Ha ocurrido un error al momento de abrir la canción.";
	public static final String ERROR_PLAYING_SONG = "Ha ocurrido un error al momento de reproducir la canción.";
	public static final String ERROR_PAUSING_SONG = "Ha ocurrido un error al momento de pausar la canción.";
	public static final String ERROR_RESUMING_SONG = "Ha ocurrido un error al momento de reanudar la canción.";
	public static final String ERROR_STOPPING_SONG = "Ha ocurrido un error al momento de parar la canción.";
	public static final String ERROR_NEXT_SONG = "Ha ocurrido un error al momento de seguir con la siguiente canción.";
	public static final String ERROR_PREV_SONG = "Ha ocurrido un error al momento de devolverse a la canción anterior.";
	public static final String ERROR_VOLUME_SONG = "Ha ocurrido un error al momento de cambiar el volumen.";
	public static final String ERROR_CLEAR_LIST = "Ha ocurrido un error al momento de borrar la lista de reproducción.";

    public PlayerException(String message) {
        super(message);
    }
}
