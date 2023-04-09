package chat.single;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.maturski.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import chat.single.message.SocketIOService;
import chat.single.message.messageModel;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import network.SocketManager;
import network.base;
import network.chat.*;

public class Activity extends AppCompatActivity {

    Button sendButton;
    EditText typeMessage;
    RecyclerView recyclerView;
    List<messageModel> dataList = new ArrayList<messageModel>();
    String username;
    String room;
    ScreenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_chat);
        sendButton = findViewById(R.id.send_button);
        typeMessage = findViewById(R.id.message_input);
        recyclerView = findViewById(R.id.message_list);
        String url = base.getUlr();
        username = intent.getStringExtra("username");
        room = intent.getStringExtra("room");
        Intent intentService = new Intent(this, SocketIOService.class);
        intentService.putExtra("room", room);
        startService(intentService);

        sendToService(username, SocketManager.getSocket());
        // Register a broadcast receiver to receive the message



        networkRequestMessages(room, recyclerView, sendButton, typeMessage, username);
       //recyclerView.setLayoutManager(new LinearLayoutManager(this));
       //ScreenAdapter chatScreenAdapter = new ScreenAdapter(sendButton, socket, typeMessage, dataList, username);
       //runChat(socket, chatScreenAdapter, username);
       // recyclerView.setAdapter(chatScreenAdapter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update UI with the received message
            Log.d("TAG", "message: " + intent.getStringExtra("message"));
            Log.d("TAG", "username: " + intent.getStringExtra("username"));
            Log.d("TAG", "room: " + intent.getStringExtra("room"));
            Log.d("TAG", "messageDateTime: " + intent.getStringExtra("messageDateTime"));
            messageModel messageModel = new messageModel(intent.getStringExtra("message"), intent.getStringExtra("username"), intent.getStringExtra("room"), intent.getStringExtra("messageDateTime"));
            dataList.add(messageModel);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver, new IntentFilter("com.example.myapp.ACTION_UPDATE_UI"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void sendToService(String username, Socket socket){
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject data = new JSONObject();
                        String message = typeMessage.getText().toString();
                        String dateTime = "";
                        try {
                            data.put("message", message);
                            data.put("username", username);
                            data.put("room", room);
                            data.put("messageDateTime", dateTime);
                            Log.d("TAG", "onClick: " + room);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        typeMessage.setText("");
                        socket.emit("message", data);
                    }
                });

                        /*socket.on("returnMessage", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        JSONObject data = (JSONObject) args[0];
                                        try {
                                            if(data.getString("room").equals(room)) {
                                                Log.d("TAG", "run: its da room" );
                                                messageModel message = null;
                                                try {
                                                    message = new messageModel(data.getString("message"), data.getString("username"), data.getString("room"), data.getString("messageDateTime"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Log.d("sender", "run: " + message.getUsername());
                                                dataList.add(message);
                                                adapter.notifyDataSetChanged();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });*/

    }

    public void networkRequestMessages(String room, RecyclerView recyclerView, Button sendButton, EditText typeMessage, String username) {
        messageRequestAsyncTask task = new messageRequestAsyncTask(new messageRequestAsyncTask.NetworkRequestListener() {
            @Override
            public void onNetworkRequestComplete(List<messageModel> data) {
                dataList = data;
                recyclerView.setLayoutManager(new LinearLayoutManager(Activity.this));
                adapter = new ScreenAdapter(sendButton, typeMessage, dataList, username);
                recyclerView.setAdapter(adapter);
                Log.d("TAG", "onNetworkRequestComplete: " + data.size());

            }
        }, room);
        task.execute();
    }
}