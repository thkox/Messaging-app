package eu.thkox.messaging_app.data.model;

import eu.thkox.messaging_app.data.model.User;

public class Message {

    int timestamp;
    String senderId;
    String receiverId;
    String text;

    public Message() {
    }
    public Message(int timestamp, String senderId, String receiverId, String text) {
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
    }
    public int getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
    public String getSenderId() {
        return senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public String getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
