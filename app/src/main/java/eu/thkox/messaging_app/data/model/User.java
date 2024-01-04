package eu.thkox.messaging_app.data.model;

public class User {

    String email;
    String nickname;
    String id;

    String imageURL;


    public User() {
    }
    public User(String email, String nickname, String id, String imageURL) {
        this.email = email;
        this.nickname = nickname;
        this.id = id;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


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

}
