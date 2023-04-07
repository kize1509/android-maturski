package admin.delete;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maturski.R;
import network.models.response;
import models.User;
import network.base;
import network.userService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    base baseInstance;
    List<User> userList;
    ProgressBar pb;
    Retrofit retrofit;

    public Adapter(List<User> result, ProgressBar pb, Retrofit retrofit) {
        userList = result;
        this.pb = pb;
        this.retrofit = retrofit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    public void updateDataSetAfterDel(int position, String username){
        int size = userList.size();
        for (int i = 0; i < size; i++){
            if(userList.get(i).getUsername().equals(username)){
                userList.remove(i);
                break;
            }
        }
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(userList.get(position) != null){
            User user1 = userList.get(position);
            holder.tv.setText(user1.getUsername());
            holder.cv.setVisibility(View.VISIBLE);
        }
        holder.db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                String username = holder.tv.getText().toString();
                Thread thread = new Thread() {
                    public void run() {
                        new NetworkRequestTask().execute(username);
                    }
                };
                updateDataSetAfterDel(position, username);
                final Handler handler = new Handler();
                handler.post(thread);


            }
        });
    }

    @Override
    public int getItemCount() {
        return  userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        CardView cv;
        Button db;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.one);
            cv = itemView.findViewById(R.id.card);
            db = itemView.findViewById(R.id.delButton);
        }
    }

    private class NetworkRequestTask extends AsyncTask<String, Void, String> {

        String message;

        @Override
        protected String doInBackground(String... params) {
            baseInstance = new base();
            userService userService = retrofit.create(userService.class);
            Call<response> response = userService.deleteUser(params[0]);

            final long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            while (elapsedTime < 3000) {
                // animation code here
                elapsedTime = System.currentTimeMillis() - startTime;
                pb.setProgress((int)elapsedTime);
            }
            response.enqueue(new Callback<network.models.response>() {
                @Override
                public void onResponse(Call<network.models.response> call, Response<network.models.response> response) {
                    message = response.body().getMessage();
                    Log.d("TAG", "onResponse: " + message);
                }

                @Override
                public void onFailure(Call<network.models.response> call, Throwable t) {
                    Log.d("TAG", "onFailure: zapelo");
                }
            });
            return message;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("onPostExecute", "onPostExecute: "  + result);
            pb.setVisibility(View.GONE);
        }
    }
}
