package network;

import chat.all.Adapter;
import chat.room.chatRoomModel;
import chat.room.roomCreationResponse;
import chat.room.roomObject;
import chat.single.Activity;
import chat.single.message.messageModel;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class base {
    private String url = "https://cc56-93-87-123-158.eu.ngrok.io";

    public String getUrl() {
        return url;
    }

    public void networkRequestUsers(String url, String username, List<User> userList, RecyclerView recyclerView, Context context, ProgressBar progBar, Button loadButton){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService userService = retrofit.create(userService.class);
        Call<List<User>> response = userService.getAllUsers(username);

        response.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                admin.delete.Adapter adapter = new admin.delete.Adapter(response.body(), progBar, retrofit);
                recyclerView.setAdapter(adapter);
                loadButton.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("TAG", "onFailure: zapelo");
            }
        });
    }
    public List<messageModel> networkRequestMessages(String room){
        List<messageModel> data = new ArrayList<>();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService userService = retrofit.create(userService.class);
        Call<List<messageModel>> response = userService.getMessages(room);

        response.enqueue(new Callback<List<messageModel>>() {
            @Override
            public void onResponse(Call<List<messageModel>> call, Response<List<messageModel>> response) {
                for (int i = 0; i<response.body().size(); i++){
                    if (response.body().get(i).getChatRoom().equals(room)) {
                        data.add(response.body().get(i));
                        Log.d("TAG", "onResponse: " + response.body().get(i).getMessageDateTime());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<messageModel>> call, Throwable t) {
                Log.d("TAG", "onFailure: neuspesno iscitavanje poruka");
            }
        });
        return data;
    }

    public void networkRequestChatRoom(chatRoomModel chatRoomModel, Context context, String username){
        List<messageModel> data = new ArrayList<>();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService userService = retrofit.create(userService.class);
        Call<roomCreationResponse> response = userService.createANewChat(chatRoomModel);
        response.enqueue(new Callback<roomCreationResponse>() {
            @Override
            public void onResponse(Call<roomCreationResponse> call, Response<roomCreationResponse> response) {
                roomCreationResponse data = response.body();
                Log.d("TAG", "onResponse: " + response.body());
                String responseMessage = data.getMessage();
                String id = data.getId();

                if (responseMessage.equals("failed")){}else{
                        Intent newInten = new Intent(context, Activity.class);
                        newInten.putExtra("message", responseMessage);
                        newInten.putExtra("username", chatRoomModel.getMember1());
                        newInten.putExtra("room", id);

                        context.startActivity(newInten);
                }
            }

            @Override
            public void onFailure(Call<roomCreationResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t);
            }
        });
    }

   public void networkRequestAllRooms(String username, RecyclerView recyclerView, Context context){
        List<roomObject> dataRooms = new ArrayList<>();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService userService = retrofit.create(userService.class);
        Call<List<roomObject>> response = userService.getAllRooms(username);
        response.enqueue(new Callback<List<roomObject>>() {
            @Override
            public void onResponse(Call<List<roomObject>> call, Response<List<roomObject>> response) {
                if (response.body() != null){
                    Log.d("TAG", "onResponse: " + response.body());
                    for (int i = 0; i<response.body().size(); i++){
                        dataRooms.add(response.body().get(i));
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    Adapter allChatsScreenAdapter = new Adapter(response.body(), context, username);
                    recyclerView.setAdapter(allChatsScreenAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<roomObject>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t);
            }
        });
    }
}
