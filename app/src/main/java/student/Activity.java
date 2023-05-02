package student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.maturski.R;

import student.schedule.ScheduleActivity;

public class Activity extends AppCompatActivity {

    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        cardView = findViewById(R.id.Schedule);
        String razred = getIntent().getStringExtra("class");

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity.this, ScheduleActivity.class);
                intent.putExtra("class", razred);
                Activity.this.startActivity(intent);
            }
        });
    }


}