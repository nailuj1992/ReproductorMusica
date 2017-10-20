package avuuna.player.test;

import avuuna.player.exception.*;
import avuuna.player.model.*;
import javazoom.jl.player.basic.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author pegasusmax
 */
public class PlayerTest {

    Player player;
    Song song1, song2, song3;
    int sleep = 500;

    @Before
    public void setUp() {
        BasicPlayer basicPlayer = new BasicPlayer();
        player = new Player(basicPlayer);
        player.getPlayer().addBasicPlayerListener(player);
        song1 = new Song("src/main/resources/music/14 - Vale Healing.mp3");
        song2 = new Song("src/main/resources/music/06 - Nemesis.mp3");
        song3 = new Song("src/main/resources/music/03 - Times Change.mp3");
    }

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

    @Test
    public void playNextSong() {
        try {
            player.addSong(song1);
            player.addSong(song2);
            player.addSong(song3);
            player.setActual(player.getSongs().get(0));
            player.open(player.getActual());
            player.play();//1
            Thread.sleep(sleep);
            player.next();//2
            Thread.sleep(sleep);
            player.next();//3
            Thread.sleep(sleep);
            player.next();//1
            Thread.sleep(sleep);
        } catch (PlayerException | BasicPlayerException | InterruptedException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void playPreviousSong() {
        try {
            player.addSong(song1);
            player.addSong(song2);
            player.addSong(song3);
            player.setActual(player.getSongs().get(0));
            player.open(player.getActual());
            player.play();//1
            Thread.sleep(sleep);
            player.previous();//3
            Thread.sleep(sleep);
            player.previous();//2
            Thread.sleep(sleep);
            player.previous();//1
            Thread.sleep(sleep);
        } catch (PlayerException | BasicPlayerException | InterruptedException ex) {
            fail(ex.getMessage());
        }
    }
}
