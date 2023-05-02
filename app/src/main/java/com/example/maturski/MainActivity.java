package com.example.maturski;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import models.User;
import network.base;
import network.userService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import student.Activity;

public class MainActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;
    Button submitButton;

    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        submitButton = findViewById(R.id.loginButton);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usernameFieldValue = usernameField.getText().toString();
                String passwordFieldValue = passwordField.getText().toString();

                if(usernameFieldValue.equals("") || passwordFieldValue.equals("")){
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Polja username i password moraju imati vrednost!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else{
                    String url = base.getUlr();
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
                    userService userService = retrofit.create(userService.class);
                    Call<List<User>> response = userService.getUser(usernameFieldValue, passwordFieldValue);
                    response.enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                            if (response.body() != null) {
                                if (response.body().size() > 0) {

                                    User user = response.body().get(0);

                                    if (user.getRazred().equals("adm")) {

                                        Intent newIntent = new Intent(MainActivity.this, admin.Activity.class);
                                        newIntent.putExtra("activeUser", user.getUsername());
                                        MainActivity.this.startActivity(newIntent);
                                    }
                                    if (user.getRazred().equals("prof")) {
                                        Intent newIntent = new Intent(MainActivity.this, professor.Activity.class);
                                        newIntent.putExtra("activeUser", user.getUsername());
                                        MainActivity.this.startActivity(newIntent);
                                    }
                                    if (!(user.getRazred().equals("adm") || user.getRazred().equals("prof"))) {
                                        Intent newIntent = new Intent(MainActivity.this, Activity.class);
                                        newIntent.putExtra("activeUser", user.getUsername());
                                        newIntent.putExtra("class", user.getRazred());
                                        MainActivity.this.startActivity(newIntent);
                                    } else {
                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Uneto korisnicko ime ili password nije validno!", Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                    }
                                } else {
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Uneto korisnicko ime ili password nije validno!", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {
                            Log.d("TAG", "onFailure: " + t);
                        }
                    });

                }
            }
        });
    }
}