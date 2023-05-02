package chat.all;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maturski.R;
import chat.room.roomObject;

import java.util.List;

import chat.single.Activity;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<roomObject> dataList;
    Context context;
    String username;

    public Adapter(List<roomObject> dataList, Context context, String username){
        this.dataList = dataList;
        this.context = context;
        this.username = username;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_name_box, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (dataList.get(position).getUsername() != null){
            Log.d("TAG", "onBindViewHolder: " + dataList.get(position).getUsername());
            holder.tv.setText(dataList.get(position).getUsername().toString());
            Log.d("TAG", "onBindViewHolder: " + dataList.get(position).getUsername());
        }
        holder.textParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(context, Activity.class);
                newIntent.putExtra("username", username);
                newIntent.putExtra("room", dataList.get(position).getRoom());
                newIntent.putExtra("roomName", dataList.get(position).getUsername());
                context.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout textParent;
        TextView tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.chatName);
            textParent = itemView.findViewById(R.id.chatNameHolder);

        }
    }
}
