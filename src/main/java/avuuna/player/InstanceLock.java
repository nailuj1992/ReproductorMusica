package avuuna.player;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Single-instance guard using a loopback ServerSocket as a lock.
 * The OS releases the port automatically when the process exits or crashes,
 * so there is no stale-lock problem.
 */
public class InstanceLock {

    private static final int PORT = 49152;

    private ServerSocket socket;

    public boolean tryAcquire() {
        try {
            socket = new ServerSocket(PORT, 0, InetAddress.getLoopbackAddress());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void release() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
        System.exit(0);
    }
}
