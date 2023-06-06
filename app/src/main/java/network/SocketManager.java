package network;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {
    private static Socket socket;

    public static Socket getSocket() {
        if (socket == null) {
            String url = base.getUlr();
            try {
                socket = IO.socket(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            socket.connect();
        }
        return socket;
    }

    public static void disconnectSocket() {
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }
    }
}
