package chat.single.message;

import java.time.LocalDateTime;

public class messageModel {
    private String message;
    private String username;
    private String chatRoom;
    private String messageDateTime;

    public messageModel(String message, String sender, String room, String messageDateTime){
        this.message = message;
        this.username = sender;
        this.chatRoom = room;
        this.messageDateTime = messageDateTime;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }


    public String getChatRoom() {
        return chatRoom;
    }

    public String getMessageDateTime() {
        return messageDateTime;
    }
}
