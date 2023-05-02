package network.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maturski.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import chat.all.Adapter;
import chat.room.chatRoomModel;
import chat.room.roomCreationResponse;
import chat.room.roomObject;
import chat.single.Activity;
import chat.single.message.messageModel;
import network.base;
import network.userService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class roomRequests {

    String url;
    public roomRequests(){
        url = base.getUlr();
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
                    newInten.putExtra("roomName", chatRoomModel.getMember2());
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
