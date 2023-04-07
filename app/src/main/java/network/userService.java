package network;

import models.User;

import java.util.List;

import chat.room.chatRoomModel;
import chat.room.roomCreationResponse;
import chat.room.roomObject;
import chat.single.message.messageModel;
import network.models.response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface userService {

        @GET("/data/{username}&{password}")
        Call<List<User>> getUser(@Path("username") String username, @Path("password") String password);

        @POST("/data/new")
        Call<response> addUser(@Body User newUser);

        @GET("/data/{username}")
        Call<List<User>> getAllUsers(@Path("username") String username);

        @DELETE("/data/del{username}")
        Call<response> deleteUser(@Path("username") String username);

        @GET("/data/messages/{room}")
        Call<List<messageModel>> getMessages(@Path("room") String room);

        @POST("/data/newChat")
        Call<roomCreationResponse> createANewChat(@Body chatRoomModel newChatRoom);

        @GET("/data/allRooms/{username}")
        Call<List<roomObject>> getAllRooms(@Path("username") String username);
}