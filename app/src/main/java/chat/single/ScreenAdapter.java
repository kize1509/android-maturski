package chat.single;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maturski.R;

import chat.single.message.messageModel;
import io.socket.client.Socket;

import java.time.LocalDateTime;
import java.util.List;

public class ScreenAdapter extends RecyclerView.Adapter {

    Button sendButton;
    Socket socket;
    EditText typeMessage;
    List<messageModel> dataList;
    String username;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;


    public ScreenAdapter(Button sendButton, Socket socket, EditText typeMessage, List<messageModel> dataList, String username) {
        this.sendButton = sendButton;
        this.socket = socket;
        this.typeMessage = typeMessage;
        this.dataList = dataList;
        this.username = username;
    }

    public int getItemViewType(int position) {
        messageModel message = dataList.get(position);

        if (message.getUsername().equals(username)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_me, parent, false);
            return new ViewHolderMe(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_other, parent, false);
            return new ViewHolderOther(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Boolean flag = false;
       // DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        messageModel message = null;
        if(dataList.get(position)!= null) {
            message = dataList.get(position);
            if (position > 0) {
                LocalDateTime previousMessageDate = dataList.get(position - 1).getMessageDateTime();
                if (previousMessageDate == message.getMessageDateTime()) {
                    flag = true;
                }
            }
        }
        //String formattedString = message.getMessageDateTime().format(CUSTOM_FORMATTER);
       // String nonFormattedDate = formattedString.split(" ")[0];

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((ViewHolderMe) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ViewHolderOther) holder).bind(message);
        }
    }

    public String formatDate(String date){
        String [] array = date.split("-", 10);
        String day = array[2];
        String month = array[1];
        Log.d("month", "formatDate: " + month);
        String formatedDate = month + ", " + day;
        switch (month){
            case "0":
                    formatedDate = "January" + ", " + day;
                    break;
            case "1":
                formatedDate = "February" + ", " + day;
                break;
            case "2":
                formatedDate = "March" + ", " + day;
                break;
            case "3":
                formatedDate = "April" + ", " + day;
                break;
            case "4":
                formatedDate = "May" + ", " + day;
                break;
            case "5":
                formatedDate = "June" + ", " + day;
                break;
            case "6":
                formatedDate = "July" + ", " + day;
                break;
            case "7":
                formatedDate = "August" + ", " + day;
                break;
            case "8":
                formatedDate = "September" + ", " + day;
                break;
            case "9":
                formatedDate = "October" + ", " + day;
                break;
            case "10":
                formatedDate = "November" + ", " + day;
                break;
            case "12":
                formatedDate = "December" + ", " + day;
                break;

        }
        Log.d("TAG", "formatDate: " + formatedDate);
        return formatedDate;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolderMe extends RecyclerView.ViewHolder {
        TextView messageText, timeText, dateText;

        public ViewHolderMe(@NonNull View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
            dateText = itemView.findViewById(R.id.text_gchat_date_me);
        }

        void bind(messageModel chatMessage) {
            messageText.setText(chatMessage.getMessage());
        }
    }

    public class ViewHolderOther extends RecyclerView.ViewHolder{

        TextView messageText,
        timeText, nameText, dateText;

        public ViewHolderOther(@NonNull View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
            dateText = itemView.findViewById(R.id.text_gchat_date_other);
        }

        void bind(messageModel chatMessage){
            messageText.setText(chatMessage.getMessage());
            nameText.setText(chatMessage.getUsername());
        }
    }
}
