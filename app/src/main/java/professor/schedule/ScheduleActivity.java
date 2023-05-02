package professor.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maturski.R;

import java.io.ByteArrayOutputStream;

import network.schedule.decompressImageAsyncTask;
import network.schedule.scheduleGetRequestAsyncTask;

public class ScheduleActivity extends AppCompatActivity {

    ImageView imageView;
    TextView nameSpace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_prof);
        imageView = findViewById(R.id.image);
        nameSpace = findViewById(R.id.name);
        String username = getIntent().getStringExtra("activeUser");

                getSchedule(username);
                nameSpace.setText(username);
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