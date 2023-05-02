package network.models;

import com.google.gson.annotations.SerializedName;

public class response {

    @SerializedName("message")
    private String message;

    public response(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
