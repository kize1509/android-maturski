package chat.room;

public class chatRoomModel {
    private String member1;
    private String member2;

    public chatRoomModel(String member1, String member2){
        this.member1 = member1;
        this.member2 = member2;
    }

    public String getMember1() {
        return member1;
    }

    public String getMember2() {
        return member2;
    }
}
