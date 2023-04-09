package network.chat;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chat.single.message.messageModel;
import network.base;
import network.userService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class messageRequestAsyncTask extends AsyncTask<String, Void, List<messageModel>> {

    private static final String TAG = "NetworkRequestAsyncTask";
    private final NetworkRequestListener listener; // Listener to handle callbacks
    private final String room; // Room parameter for the network request

    public messageRequestAsyncTask(NetworkRequestListener listener, String room) {
        this.listener = listener;
        this.room = room;
    }

    @Override
    protected List<messageModel> doInBackground(String... params) {
        List<messageModel> data = new ArrayList<>();
        String url = base.getUlr();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService userService = retrofit.create(userService.class);
        Call<List<messageModel>> response = userService.getMessages(room);

        try {
            Response<List<messageModel>> retrofitResponse = response.execute();
            if (retrofitResponse.isSuccessful()) {
                Log.d(TAG, "onResponsemessreq: " + retrofitResponse.body().size());
                for (int i = 0; i < retrofitResponse.body().size(); i++) {
                    if (retrofitResponse.body().get(i).getChatRoom().equals(room)) {
                        data.add(retrofitResponse.body().get(i));
                        Log.d(TAG, "onResponse: " + retrofitResponse.body().get(i).getMessageDateTime());
                    }
                }
            } else {
                Log.d(TAG, "onFailure: " + retrofitResponse.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(List<messageModel> data) {
        super.onPostExecute(data);
        if (listener != null) {
            listener.onNetworkRequestComplete(data);
        }
    }

    public interface NetworkRequestListener {
        void onNetworkRequestComplete(List<messageModel> data);
    }
}