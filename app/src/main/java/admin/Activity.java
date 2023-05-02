package admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.maturski.R;

import admin.add.AddActivity;
import admin.schedule.ScheduleActivity;
import chat.all.ChatsActivity;
import admin.delete.DeleteActivity;

public class Activity extends AppCompatActivity {
    CardView cardViewAddUser;
    CardView cardViewDelUser;
    CardView cardViewChat;
    CardView cardViewSchedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Intent intent = getIntent();

        cardViewAddUser = findViewById(R.id.addUserCard);
        String username = intent.getStringExtra("activeUser");
        cardViewAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(Activity.this, AddActivity.class);
                Activity.this.startActivity(newIntent);
            }
        });

        cardViewDelUser = findViewById(R.id.deleteUserCard);
        cardViewDelUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(Activity.this, DeleteActivity.class);
                newIntent.putExtra("activeUser", username);
                Activity.this.startActivity(newIntent);
            }
        });

        cardViewChat = findViewById(R.id.chatCard);
        cardViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(Activity.this, ChatsActivity.class);
                newIntent.putExtra("activeUser", username);
                Activity.this.startActivity(newIntent);
            }
        });

        cardViewSchedule = findViewById(R.id.cardSchedule);
        cardViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(Activity.this, ScheduleActivity.class);
                Activity.this.startActivity(newIntent);
            }
        });
    }
}