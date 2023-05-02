package network.user;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maturski.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import models.User;
import network.base;
import network.userService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class userRequests {
    String url;

    public userRequests(){
        url = base.getUlr();
    }

    public void networkRequestUsers(String username, List<User> userList, RecyclerView recyclerView, Context context, ProgressBar progBar, Button loadButton, TextView noleft){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService userService = retrofit.create(userService.class);
        Call<List<User>> response = userService.getAllUsersDel(username);

        response.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                admin.delete.Adapter adapter = new admin.delete.Adapter(response.body(), progBar, retrofit, recyclerView, noleft);
                recyclerView.setAdapter(adapter);
                loadButton.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("TAG", "onFailure: zapelo");
            }
        });
    }

}
