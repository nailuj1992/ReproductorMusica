package avuuna.player.model;

import javazoom.jl.player.basic.BasicPlayer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * BasicPlayer subclass that fixes "Resetting to invalid mark" for MP3 files
 * with large ID3v2 tags (album art, lyrics, etc.).
 *
 * mp3spi's TAudioFileReader marks the input stream with a limit of ~11 KB to
 * scan for the first MP3 frame. With a regular BufferedInputStream, the mark
 * is silently invalidated as soon as the buffer fills past 11 KB while the
 * format detector is still searching past a large ID3 tag. Increasing the
 * buffer doesn't help — the invalidation rule (`buffer.length >= marklimit`)
 * fires even sooner.
 *
 * The reliable fix is ByteArrayInputStream, which supports unlimited
 * mark/reset (the entire byte array is always available, readlimit is
 * ignored). Seek still works because m_dataSource remains the original File:
 * when BasicPlayer.skipBytes() seeks, it re-calls initAudioInputStream()
 * which dispatches back here and creates a fresh stream from byte 0.
 */
class BufferedBasicPlayer extends BasicPlayer {

    @Override
    protected void initAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        byte[] data = Files.readAllBytes(file.toPath());
        m_audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
        m_audioFileFormat = AudioSystem.getAudioFileFormat(file);
    }
}
