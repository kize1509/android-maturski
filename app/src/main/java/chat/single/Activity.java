package chat.single;

import androidx.annotation.LongDef;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
    TextView roomName;
    ListView listView;
    private Boolean mBound;
    private SocketIOService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_chat);
        sendButton = findViewById(R.id.send_button);
        typeMessage = findViewById(R.id.message_input);
       // recyclerView = findViewById(R.id.listview_screen);
        listView = findViewById(R.id.listview_screen);
        roomName = findViewById(R.id.roomName);
        String url = base.getUlr();
        String roomNameString = intent.getStringExtra("roomName");
        username = intent.getStringExtra("username");
        room = intent.getStringExtra("room");

        roomName.setText(roomNameString);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = typeMessage.getText().toString();
                sendToChat(message);
                typeMessage.setText("");
            }
        });

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            messageModel message = new messageModel(intent.getStringExtra("message"), intent.getStringExtra("username"), intent.getStringExtra("room"), intent.getStringExtra("messageDateTime"));
            Log.d("TAG", "onReceive: " + room);
            adapter.dataList.add(message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            SocketIOService.LocalBinder binder = (SocketIOService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mBound = false;
        }
    };

    private void sendToChat(String message){
        if(mBound){
            mService.sendMessage(message, username, room);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(Activity.this, SocketIOService.class);
        intent.putExtra("room", room);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        networkRequestMessages(room, sendButton, typeMessage, username);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG", "PREKID: " + room);
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("ChatMessage");
        LocalBroadcastManager.getInstance(Activity.this).registerReceiver(mReceiver, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);

    }


    public void networkRequestMessages(String room, Button sendButton, EditText typeMessage, String username) {
        messageRequestAsyncTask task = new messageRequestAsyncTask(new messageRequestAsyncTask.NetworkRequestListener() {
            @Override
            public void onNetworkRequestComplete(List<messageModel> data) {
                dataList = data;
                adapter = new ScreenAdapter(sendButton, typeMessage, dataList, username);
                listView.setAdapter(adapter);
                for(int i = 0; i < dataList.size(); i++){
                    Log.d("TAG", "sent : " + dataList.get(i).getUsername() + ", message" + dataList.get(i).getMessage());
                }

            }
        }, room);
        task.execute();
    }
}