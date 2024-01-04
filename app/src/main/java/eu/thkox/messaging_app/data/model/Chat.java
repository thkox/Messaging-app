package eu.thkox.messaging_app.data.model;

import java.util.List;

public class Chat {
    List<User> users;
    List<Message> messages;

    public Chat(List<User> users, List<Message> messages) {
        this.users = users;
        this.messages = messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
