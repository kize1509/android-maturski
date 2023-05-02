package network.schedule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import network.base;
import network.userService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class scheduleGetRequestAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private final scheduleGetRequestAsyncTask.NetworkRequestListener listener;
    private static final String TAG = "scheduleGetRequestTask";
    private String data;
    public scheduleGetRequestAsyncTask(scheduleGetRequestAsyncTask.NetworkRequestListener listener, String data){
        this.listener = listener;
        this.data = data;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = base.getUlr();
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService userService = retrofit.create(userService.class);
        Call<ResponseBody> call = userService.getSchedule(data);

        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                return bitmap;
            } else {
                // Handle error response here
                Log.d(TAG, "doInBackground: Error response: " + response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            // Handle network error here
            Log.d(TAG, "doInBackground: Network error: " + e.getMessage());
            return null;
        }
    }


    @Override
    protected void onPostExecute(Bitmap response) {
        super.onPostExecute(response);
        if (listener != null) {
            listener.onNetworkRequestComplete(response);
        }
    }

    public interface NetworkRequestListener {
        void onNetworkRequestComplete(Bitmap data);
    }
}

