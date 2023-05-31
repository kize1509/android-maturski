/*
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
    EditText typeMessage;
    List<messageModel> dataList;
    String username;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;


    public ScreenAdapter(Button sendButton, EditText typeMessage, List<messageModel> dataList, String username) {
        this.sendButton = sendButton;
        this.typeMessage = typeMessage;
        this.dataList = dataList;
        this.username = username;
    }

    public int getItemViewType(int position) {

        if (dataList.get(position).getUsername().equals(username)) {
            Log.d("TAG", "getItemViewType: usao");
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

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
        Boolean flag;
        Log.d("TAG", "SVAKAPOZ: " + position);
        if (dataList.get(position) != null) {
            messageModel message = dataList.get(position);
            String[] both = formatDateNTime(message.getMessageDateTime());
            Log.d("TAG", "PORUKA: " + message.getMessage() + " POZICIJA: " + position + " VREME: " + message.getMessageDateTime());
            if (position > 0) {
                String previousMessageDate = dataList.get(position - 1).getMessageDateTime();
                String previousDate = formatDateNTime(previousMessageDate)[0];
                if (previousDate.equals(both[0])) {
                    flag = true;
                }else {
                    flag = false;
                }
                switch (holder.getItemViewType()) {
                    case VIEW_TYPE_MESSAGE_SENT:

                    ((ViewHolderMe) holder).bind(message, flag, both[0], both[1]);

                       break;
                    case VIEW_TYPE_MESSAGE_RECEIVED:
                        ((ViewHolderOther) holder).bind(message, flag, both[0], both[1]);
                }
            }

            }
        }

        public String[] formatDateNTime (String dateTime){
            String[] array = dateTime.split("T");
            Log.d("TAG", "formatDateNTime: " + array[0]);
            String date = array[0];
            String time = array[1];
            String[] yearMonthDay = date.split("-");
            String year = yearMonthDay[0];
            String month = yearMonthDay[1];
            String day = yearMonthDay[2];
            String[] format = {year, month, day};
            switch (month) {
                case "01":
                    format[1] = "January";
                    break;
                case "02":
                    format[1] = "February";
                    break;
                case "03":
                    format[1] = "March";
                    break;
                case "04":
                    format[1] = "April";
                    break;
                case "05":
                    format[1] = "May";
                    break;
                case "06":
                    format[1] = "June";
                    break;
                case "07":
                    format[1] = "July";
                    break;
                case "08":
                    format[1] = "August";
                    break;
                case "09":
                    format[1] = "September";
                    break;
                case "10":
                    format[1] = "October";
                    break;
                case "11":
                    format[1] = "November";
                    break;
                case "12":
                    format[1] = "December";
                    break;

            }
            String[] hoursMinutes = time.split(":");
            String hours = hoursMinutes[0];
            String minutes = hoursMinutes[1];
            String formattedTime = hours + ":" + minutes;
            String formattedDate = format[1] + " " + format[2] + ", " + format[0];
            String[] both = {formattedDate, formattedTime};
            return both;
        }
        @Override
        public int getItemCount () {
            return dataList.size();
        }

    public class ViewHolderMe extends RecyclerView.ViewHolder {
        TextView messageTextMe, timeTextMe, dateText;

        public ViewHolderMe(@NonNull View itemView) {
            super(itemView);
            messageTextMe = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeTextMe = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
            dateText = itemView.findViewById(R.id.text_gchat_date_me);

        }

        void bind(messageModel chatMessage, boolean flag, String date, String time) {
            if(flag){
                dateText.setText("");
            }else{
                dateText.setText(date);
            }
            messageTextMe.setText(chatMessage.getMessage());
            timeTextMe.setText(time);
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

        void bind(messageModel chatMessage, boolean flag, String date, String time){
            messageText.setText(chatMessage.getMessage());
            nameText.setText(chatMessage.getUsername());
            timeText.setText(time);
            if(flag){
                dateText.setText("");
            }else{
                dateText.setText(date);
            }
            messageText.setText(chatMessage.getMessage());
            timeText.setText(time);
            nameText.setText(chatMessage.getUsername());
        }
    }
}
*/

package chat.single;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maturski.R;

import java.util.List;

import chat.single.message.messageModel;

public class ScreenAdapter extends BaseAdapter {

    Button sendButton;
    EditText typeMessage;
    List<messageModel> dataList;
    String username;

    public static final int VIEW_TYPE_MESSAGE_SENT = 1;
    public static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public ScreenAdapter(Button sendButton, EditText typeMessage, List<messageModel> dataList, String username) {
        this.sendButton = sendButton;
        this.typeMessage = typeMessage;
        this.dataList = dataList;
        this.username = username;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position).getUsername().equals(username)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int viewType = getItemViewType(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            if (viewType == VIEW_TYPE_MESSAGE_SENT) {
                convertView = inflater.inflate(R.layout.message_me, parent, false);
                viewHolder.messageText = convertView.findViewById(R.id.text_gchat_message_me);
                viewHolder.timeText = convertView.findViewById(R.id.text_gchat_timestamp_me);
                viewHolder.dateText = convertView.findViewById(R.id.text_gchat_date_me);
            } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
                convertView = inflater.inflate(R.layout.message_other, parent, false);
                viewHolder.messageText = convertView.findViewById(R.id.text_gchat_message_other);
                viewHolder.timeText = convertView.findViewById(R.id.text_gchat_timestamp_other);
                viewHolder.nameText = convertView.findViewById(R.id.text_gchat_user_other);
                viewHolder.dateText = convertView.findViewById(R.id.text_gchat_date_other);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(dataList.get(position) != null){
        messageModel message = dataList.get(position);
        String[] both = formatDateNTime(message.getMessageDateTime());

        viewHolder.messageText.setText(message.getMessage());
        viewHolder.timeText.setText(both[1]);

        if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            viewHolder.nameText.setText(message.getUsername());
        }

        // Set or hide the date based on the flag
        boolean flag = shouldShowDate(position, both[0]);
        if (flag) {
            viewHolder.dateText.setText("");
        } else {
            viewHolder.dateText.setText(both[0]);
        }
    }
        return convertView;
    }

    public boolean shouldShowDate(int position, String currentDate) {
        if (position == 0) {
            return false; // Show the date for the first message
        }

        String previousMessageDate = dataList.get(position - 1).getMessageDateTime();
        String previousDate = formatDateNTime(previousMessageDate)[0];

        return currentDate.equals(previousDate);
    }

    public String[] formatDateNTime(String dateTime) {
            String[] array = dateTime.split("T");
            Log.d("TAG", "formatDateNTime: " + array[0]);
            String date = array[0];
            String time = array[1];
            String[] yearMonthDay = date.split("-");
            String year = yearMonthDay[0];
            String month = yearMonthDay[1];
            String day = yearMonthDay[2];
            String[] format = {year, month, day};
            switch (month) {
                case "01":
                    format[1] = "January";
                    break;
                case "02":
                    format[1] = "February";
                    break;
                case "03":
                    format[1] = "March";
                    break;
                case "04":
                    format[1] = "April";
                    break;
                case "05":
                    format[1] = "May";
                    break;
                case "06":
                    format[1] = "June";
                    break;
                case "07":
                    format[1] = "July";
                    break;
                case "08":
                    format[1] = "August";
                    break;
                case "09":
                    format[1] = "September";
                    break;
                case "10":
                    format[1] = "October";
                    break;
                case "11":
                    format[1] = "November";
                    break;
                case "12":
                    format[1] = "December";
                    break;

            }
            String[] hoursMinutes = time.split(":");
            String hours = hoursMinutes[0];
            String minutes = hoursMinutes[1];
            String formattedTime = hours + ":" + minutes;
            String formattedDate = format[1] + " " + format[2] + ", " + format[0];
            String[] both = {formattedDate, formattedTime};
            return both;
        }

    private static class ViewHolder {
        TextView messageText;
        TextView timeText;
        TextView nameText;
        TextView dateText;
    }
}
