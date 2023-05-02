package network.schedule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import admin.schedule.ImageCompressionTask;
import chat.single.message.messageModel;
import network.base;
import network.models.response;
import network.userService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class schedulePostRequestAsyncTask extends AsyncTask<String, Void, response> {
    private final NetworkRequestListener listener;
    private static final String TAG = "scheduleRequestTask";
    private String path;
    private String data;
    private String role;
    private byte[] compressedImage;
    public schedulePostRequestAsyncTask(NetworkRequestListener listener, byte[] compressedImage, String data, String role, String path){
        this.listener = listener;
        this.path = path;
        this.data = data;
        this.role = role;
        this.compressedImage = compressedImage;
    }

    @Override
    protected response doInBackground(String... strings) {
        File imgFile = new File(path);
        String url = base.getUlr();
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImage);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imgFile.getName(), requestBody);

        userService userService = retrofit.create(userService.class);
        Call<response> call = userService.newSchedule(role, data, imagePart);

        // Execute the network request synchronously and wait for the response
        String msg = "not complete";
            try {
                Response<response> response = call.execute();
                if (response.isSuccessful()) {
                    return response.body();
                } else {
                    // Handle error response here
                    Log.d(TAG, "doInBackground: Error response: " + response.errorBody().string());
                    msg = response.errorBody().string();
                    return new response("Error response: " + response.errorBody().string());
                }
            } catch (IOException e) {
                // Handle network error here
                Log.d(TAG, "doInBackground: Network error: " + e.getMessage());
                return new response("Network error: " + e.getMessage());
            }
    }


    @Override
    protected void onPostExecute(response response) {
        super.onPostExecute(response);
        if (listener != null) {
            listener.onNetworkRequestComplete(response);
        }
    }

    public interface NetworkRequestListener {
        void onNetworkRequestComplete(response data);
    }
}
