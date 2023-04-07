package chat.room;

public class
roomObject {
    private String id;
    private String other_chat_member_username;
    public roomObject(String username, String room){
        this.id = room;
        this.other_chat_member_username = username;
    }

    public String getRoom() {
        return id;
    }

    public String getUsername() {
        return other_chat_member_username;
    }
}
