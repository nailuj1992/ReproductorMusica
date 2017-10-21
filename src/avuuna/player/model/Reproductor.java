package avuuna.player.model;

import avuuna.player.exception.*;
import avuuna.player.utils.*;

import java.io.*;
import java.util.*;
import javazoom.jl.player.basic.*;

/**
 * Esta clase provee las funcionalidades necesarias para que el reproductor de musica sea posible.<br><br>
 * Esta clase hace parte del patron <i>Observador-Observado</b>, en donde esta es el <i>Observado</i>.
 * @author Avuuna, la Luz del Alba
 * 
 */
public class Reproductor extends Sujeto implements BasicPlayerListener, Serializable {
	private static final long serialVersionUID = -6648685344329730558L;

	private static Reproductor reproductor = null;

	private int actualEvent;
	private long progressTime;
	private double progressBytes;
	private final BasicPlayer player;
	private Cancion actual;
	private List<Cancion> songs;

	/**
	 * Obtiene el reproductor actual.
	 * @return Obtiene una unica instancia del reproductor (<b>Patron Singleton</b>).
	 */
	public static Reproductor getInstance() {
		if (reproductor == null) {
			BasicPlayer basicPlayer = new BasicPlayer();
			reproductor = new Reproductor(basicPlayer);
		}
		return reproductor;
	}

	private Reproductor(BasicPlayer player) {
		this.player = player;
		setSongs(new ArrayList<Cancion>());

		player.addBasicPlayerListener(this);
		setController(player);
	}

	/**
	 * Obtiene la cancion que se esta reproduciendo.
	 * @return
	 */
	public Cancion getActual() {
		return actual;
	}

	/**
	 * Cambia la cancion que se esta reproduciendo.
	 * @param actual
	 */
	public void setActual(Cancion actual) {
		this.actual = actual;
	}

	/**
	 * Obtiene el estado actual del reproductor, es decir si está reproduciendo, pausado, 
	 * detenido, terminado o abierto una cancion.<br><br>
	 * Estados posibles:
	 * <li><code>BasicPlayerEvent.UNKNOWN</code> -> Desconocido</li>
	 * <li><code>BasicPlayerEvent.OPENING</code> -> Abriendo</li>
	 * <li><code>BasicPlayerEvent.OPENED</code> -> Abierto</li>
	 * <li><code>BasicPlayerEvent.PLAYING</code> -> Reproduciendo</li>
	 * <li><code>BasicPlayerEvent.STOPPED</code> -> Detenido</li>
	 * <li><code>BasicPlayerEvent.PAUSED</code> -> Pausado</li>
	 * <li><code>BasicPlayerEvent.RESUMED</code> -> Reanudado</li>
	 * <li><code>BasicPlayerEvent.SEEKING</code> -> Buscando</li>
	 * <li><code>BasicPlayerEvent.SEEKED</code> -> Encontrado</li>
	 * <li><code>BasicPlayerEvent.EOM</code> -> Fin de Musica</li>
	 * <li><code>BasicPlayerEvent.PAN</code> -> Equalizador</li>
	 * <li><code>BasicPlayerEvent.GAIN</code> -> Volumen</li>
	 * @return
	 */
	public int getActualEvent() {
		return actualEvent;
	}

	/**
	 * Obtiene el tiempo que lleva la cancion reproduciendose.
	 * @return
	 */
	public long getProgressTime() {
		return progressTime;
	}

	private void setProgressTime(long progressTime) {
		this.progressTime = progressTime;
	}

	/**
	 * Obtiene los bytes que han transcurrido mientras la cancion se ha estado reproduciendo.
	 * @return
	 */
	public double getProgressBytes() {
		return progressBytes;
	}

	private void setProgressBytes(double progressBytes) {
		this.progressBytes = progressBytes;
	}

	/**
	 * Adiciona una cancion al reproductor y notifica a los observadores del cambio.
	 * @param song Archivo de la cancion a adicionar.
	 * @throws PlayerException Cuando ya existe la cancion en la lista.
	 */
	public void addSong(Cancion song) throws PlayerException {
		if (isSongInList(song)) {
			throw new PlayerException(PlayerException.REPEATED_SONG);
		}
		songs.add(song);
		notifyObservers();
	}

	/**
	 * Quita una cancion del reproductor.
	 * @param song Archivo de la cancion a quitar.
	 * @throws PlayerException Cuando la cancion a quitar no existe en la lista.
	 */
	public void removeSong(Cancion song) throws PlayerException {
		boolean quito = songs.remove(song);
		notifyObservers();
		if (!quito) {
			throw new PlayerException(PlayerException.ERROR_REMOVE_SONG);
		}
	}

	/**
	 * Verifica si una cancion esta en la lista de reproduccion.
	 * @param song Archivo de la cancion.
	 * @return Devuelve <b>true</b> si la cancion existe dentro de la lista. <b>false</b> D.L.C.
	 */
	public boolean isSongInList(Cancion song) {
		boolean found = false;
		for (int i = 0; i < songs.size() && !found; i++) {
			if (songs.get(i).equals(song)) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Detiene la musica y purga toda la lista de reproduccion.
	 * @throws BasicPlayerException Excepcion lanzada por <code>stop()</code>.
	 */
	public void clearList() throws BasicPlayerException {
		songs.clear();
		stop();
		setActual(null);
		notifyObservers();
	}

	/**
	 * Obtiene la lista de reproduccion.
	 * @return Una lista con todas las canciones que se van a reproducir.
	 */
	public List<Cancion> getSongs() {
		return songs;
	}

	private void setSongs(List<Cancion> songs) {
		this.songs = songs;
	}

	/**
	 * Obtiene una cancion de la lista, dado su nombre.
	 * @param name Nombre de la cancion.
	 * @return La cancion <b>SI</b> esta en la lista. Devuelve <b>null</b> si no la encontro.
	 */
	public Cancion getSong(String name) {
		Cancion resp = null;
		for (Cancion song : songs) {
			if (name.equals(song.getName())) {
				resp = song;
				break;
			}
		}
		return resp;
	}

	@Override
	public void opened(Object stream, @SuppressWarnings("rawtypes") Map properties) {
		// Utils.display("opened : " + properties.toString());
		if (properties.containsKey("duration")) {
			actual.setDuration(Long.parseLong(properties.get("duration").toString()));
		}
		if (properties.containsKey("audio.length.bytes")) {
			actual.setBytesLength(Double.parseDouble(properties.get("audio.length.bytes").toString()));
		}
	}

	@Override
	public void progress(int bytesread, long microseconds, byte[] pcmdata, @SuppressWarnings("rawtypes") Map properties) {
		// Utils.display("progress : " + properties.toString());
		if (properties.containsKey("mp3.position.microseconds")) {
			this.setProgressTime(Long.parseLong(properties.get("mp3.position.microseconds").toString()));
		}
		if (properties.containsKey("mp3.position.byte")) {
			this.setProgressBytes(Double.parseDouble(properties.get("mp3.position.byte").toString()));
		}
	}

	@Override
	public void stateUpdated(BasicPlayerEvent event) {
		// Utils.display("stateUpdated : " + event.toString());
		if (event.getCode() == BasicPlayerEvent.EOM) {
			try {
				int current = songs.indexOf(actual);
				if (current == songs.size() - 1) {
					stop();
				} else if (current < songs.size() - 1) {
					actual = songs.get(current + 1);
					open(actual);
					play();
				}
			} catch (BasicPlayerException ex) {
				Utils.log(Reproductor.class.getName(), ex);
			}
		}
	}

	/**
	 * Abre una cancion.
	 * @param song Archivo de la cancion a abrir.
	 * @throws BasicPlayerException Error al abrir la cancion.
	 */
	public void open(Cancion song) throws BasicPlayerException {
		player.open(song);
		actualEvent = BasicPlayerEvent.OPENED;
		notifyObservers();
	}

	/**
	 * Reproduce la cancion actual abierta.
	 * @throws BasicPlayerException Error al reproducir la cancion.
	 */
	public void play() throws BasicPlayerException {
		player.play();
		actualEvent = BasicPlayerEvent.PLAYING;
		notifyObservers();
	}

	/**
	 * Pausa la cancion actual abierta.
	 * @throws BasicPlayerException Error al pausar la cancion.
	 */
	public void pause() throws BasicPlayerException {
		player.pause();
		actualEvent = BasicPlayerEvent.PAUSED;
		notifyObservers();
	}

	/**
	 * Reanuda la cancion actual abierta y pausada.
	 * @throws BasicPlayerException Error al reanudar la cancion.
	 */
	public void resume() throws BasicPlayerException {
		player.resume();
		actualEvent = BasicPlayerEvent.RESUMED;
		notifyObservers();
	}

	/**
	 * Detiene la cancion abierta.
	 * @throws BasicPlayerException Error al detener la cancion.
	 */
	public void stop() throws BasicPlayerException {
		player.stop();
		actualEvent = BasicPlayerEvent.STOPPED;
		this.setProgressTime(0);
		this.setProgressBytes(0);
		notifyObservers();
	}

	/**
	 * Busca la cancion siguiente de la lista y la abre. Si es la ultima cancion, abre la primera.
	 * @throws BasicPlayerException Excepcion lanzada por <code>open()</code>.
	 */
	public void next() throws BasicPlayerException {
		if (songs.size() > 0 && actual != null) {
			stop();
			int index = songs.indexOf(actual);
			if (index == songs.size() - 1) {
				actual = songs.get(0);
			} else {
				actual = songs.get(index + 1);
			}
			open(actual);
		}
	}

	/**
	 * Busca la cancion anterior de la lista y la abre. Si es la primera cancion, abre la ultima.
	 * @throws BasicPlayerException Excepcion lanzada por <code>open()</code>.
	 */
	public void previous() throws BasicPlayerException {
		if (songs.size() > 0 && actual != null) {
			stop();
			int index = songs.indexOf(actual);
			if (index == 0) {
				actual = songs.get(songs.size() - 1);
			} else {
				actual = songs.get(index - 1);
			}
			open(actual);
		}
	}

	/**
	 * Cambia el volumen al reproductor.
	 * @param volume Volumen a cambiar.
	 * @throws BasicPlayerException Error al cambiar el volumen.
	 */
	public void setVolume(double volume) throws BasicPlayerException {
		player.setGain(volume);
	}

	@Override
	public void setController(BasicController controller) {
		Utils.display("setController : " + controller);
	}
}
