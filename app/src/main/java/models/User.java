package models;

public class User {
    private String username;
    private String password;
    private String razred;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getRazred() {
        return razred;
    }

    public User(String username, String password, String razred){
        this.username = username;
        this.password = password;
        this.razred = razred;
    }

}
