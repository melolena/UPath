package com.example.upath;

public class RegisterRequest {
    public String nome;
    public String email;
    public String confirmEmail;
    public String senha;
    public String confirmSenha;

    public RegisterRequest(String nome, String email, String confirmEmail, String senha, String confirmSenha) {
        this.nome = nome;
        this.email = email;
        this.confirmEmail = confirmEmail;
        this.senha = senha;
        this.confirmSenha = confirmSenha;
    }
}
