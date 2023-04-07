package chat.room;

public class roomCreationResponse {
    String message;
    String id;

    public roomCreationResponse(String message, String id){
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }
}
