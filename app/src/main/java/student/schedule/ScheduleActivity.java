package student.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maturski.R;

import network.schedule.decompressImageAsyncTask;
import network.schedule.scheduleGetRequestAsyncTask;

public class ScheduleActivity extends AppCompatActivity {
    ImageView imageView;
    TextView nameSpace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_student);
        imageView = findViewById(R.id.image);
        nameSpace = findViewById(R.id.name);
        String razred = getIntent().getStringExtra("class");
        nameSpace.setText(razred);

        getSchedule(razred);
    }

    public void getSchedule(String username){
        scheduleGetRequestAsyncTask scheduleGetRequestAsyncTask = new scheduleGetRequestAsyncTask(new scheduleGetRequestAsyncTask.NetworkRequestListener() {
            @Override
            public void onNetworkRequestComplete(Bitmap data) {
                decompressImage(data);
            }
        }, username);
        scheduleGetRequestAsyncTask.execute();
    }

    public void decompressImage(Bitmap compressed){
        decompressImageAsyncTask decompressImageAsyncTask = new decompressImageAsyncTask(new decompressImageAsyncTask.ImageDeCompressionListener() {
            @Override
            public void onImageCompressed(Bitmap compressedImage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(compressedImage);
                    }
                });
            }
        }, compressed);
        decompressImageAsyncTask.execute();
    }
}