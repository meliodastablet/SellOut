package com.mcakiroglu.sellout.models;

public class Message {

    private String fromID;
    private String message;
    private String timestamp;
    private String read;
    private String toID;

    public Message(String fromID, String message, String timestamp, String read, String toID) {
        this.fromID = fromID;
        this.message = message;
        this.timestamp = timestamp;
        this.read = read;
        this.toID = toID;
    }

    public Message(){};

    public String getFromID() {
        return fromID;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getRead() {
        return read;
    }

    public String getToID() {
        return toID;
    }

}
