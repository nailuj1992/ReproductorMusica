package avuuna.player.exception;

public class PlayerException extends Exception {
    private static final long serialVersionUID = -6544648361382072128L;

    public static final String ERROR = "Error";
    public static final String REPEATED_SONG = "Song is already in the playlist.";
    public static final String ERROR_OPENING_SONG = "An error occurred while opening the song.";
    public static final String ERROR_PLAYING_SONG = "An error occurred while playing the song.";
    public static final String ERROR_PAUSING_SONG = "An error occurred while pausing the song.";
    public static final String ERROR_RESUMING_SONG = "An error occurred while resuming the song.";
    public static final String ERROR_STOPPING_SONG = "An error occurred while stopping the song.";
    public static final String ERROR_NEXT_SONG = "An error occurred while skipping to the next song.";
    public static final String ERROR_PREV_SONG = "An error occurred while going back to the previous song.";
    public static final String ERROR_VOLUME_SONG = "An error occurred while changing the volume.";
    public static final String ERROR_REMOVE_SONG = "The selected song could not be removed.";
    public static final String ERROR_REMOVE_SONG_NO_EXISTS = "The song to remove does not exist in the playlist.";
    public static final String ERROR_NO_SONG_SELECTED = "No song is selected.";
    public static final String ERROR_NO_SONGS_LIST = "The playlist is empty.";
    public static final String ERROR_CLEAR_LIST = "An error occurred while clearing the playlist.";

    public PlayerException(String message) {
        super(message);
    }
}
