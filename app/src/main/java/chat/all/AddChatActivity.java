package chat.all;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maturski.R;
import models.User;
import network.base;
import network.userService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import chat.room.chatRoomModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import network.chat.roomRequests;

public class AddChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    roomRequests roomRequests;
    List<User> users;
    ArrayList<String> mylist;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent oldIntent = getIntent();
        String username = oldIntent.getStringExtra("username");
        setContentView(R.layout.activity_add_new_chat);
        listView = findViewById(R.id.listView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        mylist = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        Thread thread = new Thread() {
            public void run() {
                roomRequests = new roomRequests();
                String url = base.getUlr();
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
                        for (int i = 0; i<response.body().size(); i++){
                            mylist.add(response.body().get(i).getUsername());
                        }

                        if(mylist.size() == 0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddChatActivity.this, "There is no users in the database!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            setSupportActionBar(toolbar);
                            listView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.d("TAG", "zapelo: " + t);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddChatActivity.this, "Unable to connect to the database!", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });

            }
        };

        final Handler handler = new Handler();
        handler.post(thread);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = ((TextView) view).getText().toString();
                Log.d("TAG", "onItemClick: " + selected);
                chatRoomModel newChatRoomModel = new chatRoomModel(username, selected);

                Thread thread = new Thread() {
                    public void run() {
                        roomRequests.networkRequestChatRoom(newChatRoomModel, AddChatActivity.this, username);
                    }
                };

                final Handler handler = new Handler();
                handler.post(thread);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                    if(mylist.contains(s)){
                        adapter.getFilter().filter(s);
                    }else{
                        Toast.makeText(AddChatActivity.this, "Not found", Toast.LENGTH_LONG).show();
                    }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void populateTheList(){
        for (int i = 0; i<users.size(); i++){
            mylist.add(users.get(i).getUsername());
        }
    }

    private class NetworkRequestTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String url = String.valueOf(R.string.url);
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            userService userService = retrofit.create(userService.class);
            Call<List<User>> response = userService.getAllUsers(strings[0]);

            response.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    users.addAll(response.body());
                    for (int i = 0; i<response.body().size(); i++){
                        mylist.add(response.body().get(i).getUsername());
                    }
                    Log.d("TAG", "onResponse: " + response.body().size());
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.d("TAG", "onFailure: " + "zapelo bas fest");
                }
            });
            return mylist;
        }
    }
}