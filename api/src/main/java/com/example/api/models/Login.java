package com.example.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Login implements Serializable {
    @SerializedName("user")
    @Expose
    private LoginRequest user;

    public Login(LoginRequest user) {
        this.user = user;
    }

    public LoginRequest getUser() {
        return user;
    }

    public void setUser(LoginRequest user) {
        this.user = user;
    }
}
