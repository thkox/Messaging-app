package eu.thkox.messaging_app;

public class User {

    String email;
    String nickname;
    String id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String email, String nickname, String id) {
        this.email = email;
        this.nickname = nickname;
        this.id = id;
    }
}
