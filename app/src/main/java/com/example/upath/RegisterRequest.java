package com.example.upath;

public class RegisterRequest {
    private String nome;
    private String email;
    private String confirmEmail; // <--- Faltava isso
    private String senha;
    private String confirmSenha; // <--- Faltava isso

    public RegisterRequest(String nome, String email, String confirmEmail, String senha, String confirmSenha) {
        this.nome = nome;
        this.email = email;
        this.confirmEmail = confirmEmail;
        this.senha = senha;
        this.confirmSenha = confirmSenha;
    }
}