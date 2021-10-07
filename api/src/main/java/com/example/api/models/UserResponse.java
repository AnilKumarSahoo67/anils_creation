package com.example.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserResponse implements Serializable {
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("bio")
    @Expose
    public String bio ;
    @SerializedName("image")
    @Expose
    public String image;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
