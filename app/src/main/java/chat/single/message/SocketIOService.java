package chat.single.message;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.maturski.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import network.SocketManager;
import network.base;

public class SocketIOService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private Socket mSocket;
    private String room;
    private Thread socketThread;
    public class LocalBinder extends Binder {
        public SocketIOService getService() {
            return SocketIOService.this;
        }
    }

    public void sendMessage(String message, String username, String roomSend) {

        JSONObject data = new JSONObject();
        String dateTime = "";
        try {
            data.put("message", message);
            data.put("username", username);
            data.put("room", roomSend);
            data.put("messageDateTime", dateTime);
            Log.d("TAG", "onClick room: " + room);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("message", data);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        socketThread = new Thread(new SocketThread());
        socketThread.start();

    }

    private void sendMessageToComponents(messageModel message) {
        Intent intent = new Intent("ChatMessage");
        intent.putExtra("message", message.getMessage());
        intent.putExtra("messageDateTime", message.getMessageDateTime());
        intent.putExtra("room", message.getChatRoom());
        intent.putExtra("username", message.getUsername());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.close();
            mSocket = null;
        }

        if (socketThread != null) {
            socketThread.interrupt();
            socketThread = null;
        }
    }

    public void setRoom(String room){
        this.room = room;
    }

    private class SocketThread implements Runnable{

        @Override
        public void run() {
            mSocket = SocketManager.getSocket();

            while (!mSocket.connected()) {
                try {
                    Thread.sleep(1000);
                    Log.i("Socket", "Waiting to connect");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Log.i("Socket", String.valueOf(mSocket.connected()));

            mSocket.on("returnMessage", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("its da", "Socket primio poruku");
                    JSONObject data = (JSONObject) args[0];

                    try {
                        if(data.getString("room").equals(room)) {
                            Log.d("TAG", "run: its da room" );
                            Log.d("CHAT", "written by: " + data.getString("username"));
                            messageModel message = null;
                            message = new messageModel(data.getString("message"), data.getString("username"), data.getString("room"), data.getString("messageDateTime"));

                            Log.d("sender", "run: " + message.getMessageDateTime());

                            sendMessageToComponents(message);
                        }else{
                            Log.d("TAG", "call: not da room");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}