package com.example.upath;

import com.google.gson.annotations.SerializedName;

public class ChatResponse {
    private String reply;

    @SerializedName("final")
    private boolean finalizado;

    public String getReply() { return reply; }
    public boolean isFinal() { return finalizado; }
}
