package com.example.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpRequest {
    @SerializedName("user")
    @Expose
    private User user;

    public SignUpRequest(User user) {
        this.user=user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
