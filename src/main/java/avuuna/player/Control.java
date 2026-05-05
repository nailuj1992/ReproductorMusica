package avuuna.player;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import javax.swing.*;

/**
 * Prevents multiple simultaneous instances of the application by using a temp file as a lock.
 * @see <a href="http://www.jc-mouse.net/java/evitar-ejecutar-un-programa-java-mas-de-una-vez">Original reference</a>
 */
public class Control {

    private static Control instance = null;

    private static File tmpFile;

    private static final int UPDATE_INTERVAL_SECONDS = 20;

    public static Control getInstance(String projectName) {
        if (instance == null) {
            instance = new Control(projectName);
        }
        return instance;
    }

    private Control(String projectName) {
        projectName = projectName.trim().replace(" ", "");
        tmpFile = new File(System.getProperty("java.io.tmpdir"), projectName + ".tmp");
    }

    /** Returns {@code true} if the app may start; {@code false} if another instance is already running. */
    public boolean check() {
        if (tmpFile.exists()) {
            long timestamp = readTimestamp();
            long elapsed = elapsedSeconds(timestamp);
            if (elapsed < UPDATE_INTERVAL_SECONDS) {
                JOptionPane.showMessageDialog(null, "Error: Application is already running.");
                return false;
            } else {
                scheduleTask();
                return true;
            }
        } else {
            createTmpFile();
            scheduleTask();
            return true;
        }
    }

    public long readTimestamp() {
        String line = "0";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
            while (reader.ready()) {
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return Long.valueOf(line).longValue();
    }

    public void scheduleTask() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::createTmpFile,
                1000, UPDATE_INTERVAL_SECONDS * 1000L, TimeUnit.MILLISECONDS);
    }

    public void createTmpFile() {
        Date date = new Date();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile));
            writer.write(String.valueOf(date.getTime()));
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /** Returns elapsed time in seconds since {@code timestamp} (in milliseconds). */
    public long elapsedSeconds(long timestamp) {
        long currentTime = new Date().getTime();
        return (currentTime - timestamp) / 1000;
    }

    public void shutdown() {
        deleteTmpFile();
        System.exit(0);
    }

    public void deleteTmpFile() {
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
    }
}
