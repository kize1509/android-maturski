package network;

import models.User;

import java.util.List;

import chat.room.chatRoomModel;
import chat.room.roomCreationResponse;
import chat.room.roomObject;
import chat.single.message.messageModel;
import network.models.response;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface userService {

        @GET("/data/{username}&{password}")
        Call<List<User>> getUser(@Path("username") String username, @Path("password") String password);

        @POST("/data/new")
        Call<response> addUser(@Body User newUser);

        @GET("/data/chatusers/{username}")
        Call<List<User>> getAllUsersChat(@Path("username") String username);

        @GET("/data/delusers/{username}")
        Call<List<User>> getAllUsersDel(@Path("username") String username);

        @DELETE("/data/del{username}")
        Call<response> deleteUser(@Path("username") String username);

        @GET("/data/messages/{room}")
        Call<List<messageModel>> getMessages(@Path("room") String room);

        @POST("/data/newChat")
        Call<roomCreationResponse> createANewChat(@Body chatRoomModel newChatRoom);

        @GET("/data/allRooms/{username}")
        Call<List<roomObject>> getAllRooms(@Path("username") String username);

        @Multipart
        @POST("data/schedule/{data}/{role}")
        Call<response> newSchedule(@Path("role") String role, @Path("data") String data, @Part MultipartBody.Part requestBody);

        @GET("data/schedule/{data}")
        Call<ResponseBody> getSchedule(@Path("data") String data);
}