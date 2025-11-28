package com.example.upath;

public class LoginRequest {
    private String email;
    private String senha; // O Python espera "senha", não "password"

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }
}