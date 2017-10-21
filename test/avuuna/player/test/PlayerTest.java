package avuuna.player.test;

import avuuna.player.exception.*;
import avuuna.player.model.*;
import javazoom.jl.player.basic.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Clase encargada de realizar las pruebas de unidad del reproductor.
 * @author Avuuna, la Luz del Alba
 * 
 */
public class PlayerTest {

	private Reproductor player;
	private Cancion song1, song2, song3;
	private int sleep;

	/**
	 * Inicializa todo antes de cada prueba.
	 */
	@Before
	public void setUp() {
		player = Reproductor.getInstance();

		song1 = new Cancion("resources/music/14 - Vale Healing.mp3");
		song2 = new Cancion("resources/music/06 - Nemesis.mp3");
		song3 = new Cancion("resources/music/03 - Times Change.mp3");

		sleep = 500;
	}

	/**
	 * Limpia todo y lo deja todo listo para la siguiente prueba.
	 */
	@After
	public void tearDown() {
		try {
			player.clearList();
			song1 = null;
			song2 = null;
			song3 = null;
		} catch (BasicPlayerException ex) {
			fail(ex.getMessage());
		}
	}

	/**
	 * Metodo en el que se prueba si se puede reproducir una cancion.
	 */
	@Test
	public void playSingleSong() {
		try {
			player.setActual(song1);
			player.open(player.getActual());
			player.play();
			Thread.sleep(sleep);
		} catch (BasicPlayerException | InterruptedException ex) {
			fail(ex.getMessage());
		}
	}

	/**
	 * Metodo en el que se prueba si se pueden reproducir dos canciones.
	 */
	@Test
	public void playTwoSongs() {
		try {
			player.addSong(song1);
			player.addSong(song2);
			player.setActual(player.getSongs().get(0));
			player.open(player.getActual());
			player.play();
		} catch (PlayerException | BasicPlayerException ex) {
			fail(ex.getMessage());
		}
	}

	/**
	 * Metodo que verifica que no se pueda adicionar una cancion reprtida a la lista.
	 */
	@Test
	public void noAddRepeatedSong() {
		try {
			player.addSong(song1);
			player.addSong(song2);
			player.addSong(song1);
		} catch (PlayerException ex) {
			assertTrue(ex.getMessage().equals(PlayerException.REPEATED_SONG));
		}
	}

	/**
	 * Metodo que valida si se puede saltar a la siguiente cancion.
	 */
	@Test
	public void playNextSong() {
		try {
			player.addSong(song1);
			player.addSong(song2);
			player.addSong(song3);
			player.setActual(player.getSongs().get(0));
			player.open(player.getActual());
			player.play();// 1
			Thread.sleep(sleep);
			player.next();// 2
			Thread.sleep(sleep);
			player.next();// 3
			Thread.sleep(sleep);
			player.next();// 1
			Thread.sleep(sleep);
		} catch (PlayerException | BasicPlayerException | InterruptedException ex) {
			fail(ex.getMessage());
		}
	}

	/**
	 * Metodo que valida si se puede saltar a la cancion anterior.
	 */
	@Test
	public void playPreviousSong() {
		try {
			player.addSong(song1);
			player.addSong(song2);
			player.addSong(song3);
			player.setActual(player.getSongs().get(0));
			player.open(player.getActual());
			player.play();// 1
			Thread.sleep(sleep);
			player.previous();// 3
			Thread.sleep(sleep);
			player.previous();// 2
			Thread.sleep(sleep);
			player.previous();// 1
			Thread.sleep(sleep);
		} catch (PlayerException | BasicPlayerException | InterruptedException ex) {
			fail(ex.getMessage());
		}
	}
}
