package com.example.upath;

public class ChatResponse {
    private String reply;
    private boolean finalizado; // Mapeia o campo "final" do JSON

    // O Gson as vezes precisa de getters ou anotações, mas assim costuma funcionar
    public String getReply() { return reply; }
    public boolean isFinal() { return finalizado; }
}