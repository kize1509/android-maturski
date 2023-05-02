package admin.add;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maturski.R;
import models.User;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.models.response;
import network.userService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import network.base;

public class AddActivity extends AppCompatActivity {

    Button confirmButton;
    EditText usernameFIeld;
    EditText passwordFIeld;
    EditText classField;
    CoordinatorLayout coordinatorLayout;
    Button showDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        confirmButton = findViewById(R.id.confirm);
        usernameFIeld = findViewById(R.id.emailField);
        passwordFIeld = findViewById(R.id.passField);
        classField = findViewById(R.id.classField);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        showDialog = findViewById(R.id.showDialog);

        String url = base.getUlr();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();        userService userService = retrofit.create(userService.class);

                showDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new Dialog(AddActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.conditions_popup);

                        // Set the text in the pop-up
                        TextView popupTextView = dialog.findViewById(R.id.popupTextView);
                        // Show the dialog
                        dialog.show();
                    }
                });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = usernameFIeld.getText().toString();
                String password = passwordFIeld.getText().toString();
                String razred = classField.getText().toString();

                if(email.equals("") || password.equals("") || razred.equals("")){
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Polja username i password moraju imati vrednost!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else{
                    User user = new User(email, password, razred);

                    Call<response> response = userService.addUser(user);

                    response.enqueue(new Callback<network.models.response>() {
                        @Override
                        public void onResponse(Call<network.models.response> call, Response<network.models.response> response) {
                            Log.d("response", "onResponse: " + response.body());
                            network.models.response responseMessage = response.body();
                            if (responseMessage.getMessage().equals("Successfully appended the database!")){
                                usernameFIeld.setText("");
                                passwordFIeld.setText("");
                                classField.setText("");
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, responseMessage.getMessage(), Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }else{
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, responseMessage.getMessage(), Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<network.models.response> call, Throwable t) {
                            Log.d("fail", "onFailure: " + t);
                        }
                    });

                }
            }
        });
    }
}