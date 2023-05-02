package admin.schedule;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maturski.R;

import network.models.response;
import network.schedule.schedulePostRequestAsyncTask;

public class ScheduleActivity extends AppCompatActivity{

    Button buttonProf;
    Button buttonStudent;
    EditText profEmail;
    EditText className;
    String scheduleData;
    String role = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        buttonProf = findViewById(R.id.buttonProfessor);
        buttonStudent = findViewById(R.id.buttonStudent);
        profEmail = findViewById(R.id.profEmail);
        className = findViewById(R.id.className);

        buttonProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 scheduleData = profEmail.getText().toString();
                if (scheduleData.equals("")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ScheduleActivity.this, "field can't be empty", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Intent profIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    profIntent.setType("image/*");
                    role = "prof";
                    startActivityForResult(profIntent, 200);
                }
            }
        });

        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleData = className.getText().toString();
                if (scheduleData.equals("")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ScheduleActivity.this, "field can't be empty", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Intent studIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    studIntent.setType("image/*");
                    role = "stud";
                    startActivityForResult(studIntent, 200);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        profEmail.setText("");
        className.setText("");

        if (requestCode == 200 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d("TAG", "ROLE: " + data.getExtras());
            // Get the image URI from the data
            Uri imageUri = data.getData();

            String imagePath = getRealPathFromUri(admin.schedule.ScheduleActivity.this, imageUri);

            compressRequest(imagePath, scheduleData, role);

        }
    }

    public String getRealPathFromUri(Context context, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            return uri.getPath();
        } else {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
    }

    public void networkRequestSchedule(byte[] compressedImage, String email, String role, String path){
        schedulePostRequestAsyncTask task = new schedulePostRequestAsyncTask(new schedulePostRequestAsyncTask.NetworkRequestListener() {
            @Override
            public void onNetworkRequestComplete(response data) {
                Log.d("TAG", "onNetworkRequestComplete: " + data.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScheduleActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, compressedImage, email, role, path);
        task.execute();
    }

    public void compressRequest(String path, String email, String role){
        ImageCompressionTask imageCompressionTask = new ImageCompressionTask(new ImageCompressionTask.ImageCompressionListener()  {
            @Override
            public void onImageCompressed(byte[] compressedImage) {
                networkRequestSchedule(compressedImage, email, role, path);
            }
        }, path);
        imageCompressionTask.execute();
    }

}