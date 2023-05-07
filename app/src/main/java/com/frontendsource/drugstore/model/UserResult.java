package com.frontendsource.drugstore.model;

import com.google.gson.annotations.SerializedName;

public class UserResult {
    @SerializedName("data")
    User user;
    @SerializedName("message")
    String message;
    @SerializedName("status")
    int status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
