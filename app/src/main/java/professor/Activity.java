package professor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.maturski.R;

import chat.all.ChatsActivity;
import professor.schedule.ScheduleActivity;

public class Activity extends AppCompatActivity {

    CardView chatCard;
    CardView scheduleCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);
        chatCard = findViewById(R.id.chat);
        scheduleCard = findViewById(R.id.schedule);
        Intent oldIntent = getIntent();
        String username = oldIntent.getStringExtra("activeUser");

        chatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(professor.Activity.this, ChatsActivity.class);
                newIntent.putExtra("activeUser", username);
                professor.Activity.this.startActivity(newIntent);
            }
        });
        scheduleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(professor.Activity.this, ScheduleActivity.class);
                newIntent.putExtra("activeUser", username);
                professor.Activity.this.startActivity(newIntent);
            }
        });
    }
}