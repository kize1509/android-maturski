package chat.single.message;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import network.SocketManager;
import network.base;

public class SocketIOService extends Service {

    String room = "";

    @Override
    public void onCreate() {
        super.onCreate();

        Socket socket = SocketManager.getSocket();

        socket.on("returnMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            if(data.getString("room").equals(room)) {
                                Log.d("TAG", "run: its da room" );
                                messageModel message = null;
                                try {
                                    Log.d("TAG", "call: " + data.getString("messageDateTime"));
                                    message = new messageModel(data.getString("message"), data.getString("username"), data.getString("room"), data.getString("messageDateTime"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("sender", "run: " + message.getUsername());

                                // Send a broadcast message
                                Intent intent = new Intent("com.example.myapp.ACTION_UPDATE_UI");
                                intent.putExtra("message", data.getString("message"));
                                intent.putExtra("username", data.getString("username"));
                                intent.putExtra("room", data.getString("room"));
                                intent.putExtra("messageDateTime", data.getString("messageDateTime"));

                                LocalBroadcastManager.getInstance(SocketIOService.this).sendBroadcast(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Handle any incoming intents or commands here, if needed
        if(intent != null){
            room = intent.getStringExtra("room");
        }
        Log.d("TAG", "SOBA: " + room);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Disconnect the Socket.IO client when the Service is destroyed
        SocketManager.disconnectSocket();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Return null as we don't need to bind to this Service
        return null;
    }
}
