package eu.thkox.messaging_app.data.model;

import androidx.annotation.Nullable;

public class User {
    String email;
    String nickname;
    String id;
    // Image URL of the user not implemented yet
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return this.id.equals(user.id);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
