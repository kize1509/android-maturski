package chat.single.message;

import java.time.LocalDateTime;

public class messageModel {
    private String message;
    private String username;
    private String chatRoom;
    private LocalDateTime messageDateTime;

    public messageModel(String message, String sender, String room, LocalDateTime messageDateTime){
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

    public LocalDateTime getMessageDateTime() {
        return messageDateTime;
    }
}
