package eu.thkox.messaging_app;

import java.util.List;

public class Chat {
    User user;
    List<Message> messages;

    public Chat(User user, List<Message> messages) {
        this.user = user;
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
