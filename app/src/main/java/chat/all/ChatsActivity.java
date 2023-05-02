package chat.all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.maturski.R;
import network.chat.messageRequestAsyncTask;
import network.chat.roomRequests;

public class ChatsActivity extends AppCompatActivity {

    Toolbar toolbar;
    String username;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent oldIntent = getIntent();
        username = oldIntent.getStringExtra("activeUser");
        setContentView(R.layout.activity_all_chats_window);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        recyclerView = findViewById(R.id.Recycler);
         roomRequests roomreq = new roomRequests();
        roomreq.networkRequestAllRooms(username, recyclerView, this);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_add){
            Intent newIntent = new Intent(ChatsActivity.this, AddChatActivity.class);
            newIntent.putExtra("username", username);
            ChatsActivity.this.startActivity(newIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}