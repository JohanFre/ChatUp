package com.example.chatup;

public class Chat {


    String message, username;

    public Chat(String message, String username) {
        this.message = message;
        this.username = username;
    }

    public Chat(){
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
