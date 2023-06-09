package admin.delete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.maturski.R;
import models.User;

import java.util.List;
import network.user.userRequests;

public class DeleteActivity extends AppCompatActivity {
    List<User> userList;
    Button loadButton;
    ProgressBar progBar;
    String username;
    RecyclerView recyclerView;
    userRequests ur;
    TextView nothingLeft;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        Intent intent = getIntent();
        username = intent.getStringExtra("activeUser");
        ur = new userRequests();
        recyclerView = findViewById(R.id.Recycler);
        loadButton = findViewById(R.id.button);
        progBar = findViewById(R.id.indeterminateBar);
        nothingLeft = findViewById(R.id.noleft);
        Log.d("USERNAME", "onCreate: " + username);
        
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ur.networkRequestUsers(username, userList, recyclerView, DeleteActivity.this, progBar, loadButton, nothingLeft);
            }
        });
    }

}