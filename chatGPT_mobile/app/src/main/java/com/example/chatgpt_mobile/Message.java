package com.example.chatgpt_mobile;

public class Message {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT = "bot";
    String message;
    String sentBy;

    public Message(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }
}
