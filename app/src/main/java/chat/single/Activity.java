package chat.single;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.maturski.R;
import network.base;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import chat.single.message.messageModel;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Activity extends AppCompatActivity {

    Button sendButton;
    EditText typeMessage;
    RecyclerView recyclerView;
    base base;
    List<messageModel> dataList = new ArrayList<messageModel>();
    String username;
    String room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_chat);
        sendButton = findViewById(R.id.send_button);
        typeMessage = findViewById(R.id.message_input);
        recyclerView = findViewById(R.id.message_list);
        base = new base();
        String url = base.getUrl();
        username = intent.getStringExtra("username");
        room = intent.getStringExtra("room");
            URI uri = URI.create(url);
            Socket socket = null;
        try {
            socket = IO.socket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();

        dataList = base.networkRequestMessages(room);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScreenAdapter chatScreenAdapter = new ScreenAdapter(sendButton, socket, typeMessage, dataList, username);
        recyclerView.setAdapter(chatScreenAdapter);
        runChat(socket, chatScreenAdapter, username);

    }
    private void runChat(Socket socket, ScreenAdapter adapter, String username){
       new Thread(){
           public  void run(){

                sendButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        JSONObject data = new JSONObject();
                        String message = typeMessage.getText().toString();
                        LocalDateTime dateTime = LocalDateTime.now();
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

                        socket.on("returnMessage", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                runOnUiThread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void run() {
                                        JSONObject data = (JSONObject) args[0];
                                        messageModel message = null;
                                        try {
                                            DateTimeFormatter form = DateTimeFormatter.ISO_DATE_TIME;
                                            LocalDateTime date = LocalDateTime.parse(data.getString("messageDateTime"), form);
                                            message = new messageModel(data.getString("message"), data.getString("username"), data.getString("room"), date);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("sender", "run: " + message.getUsername());
                                            dataList.add(message);
                                            adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        });
            }
       }.start();
    }
}