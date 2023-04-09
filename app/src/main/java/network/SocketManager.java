package network;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {
    private static Socket socket;

    public static Socket getSocket() {
        if (socket == null) {
            // Initialize and configure the Socket.IO client here
            String url = base.getUlr();
            try {
                socket = IO.socket(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            // Connect to the Socket.IO server
            socket.connect();
        }
        return socket;
    }

    public static void disconnectSocket() {
        if (socket != null) {
            // Disconnect the Socket.IO client
            socket.disconnect();
            socket = null;
        }
    }
}
