package com.example.upath;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    private User user; // O objeto usuário que vem junto

    public String getAccessToken() { return accessToken; }
    public User getUser() { return user; }
}