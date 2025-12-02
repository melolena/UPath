// com/example/upath/UpdateProfileRequest.java
package com.example.upath;

public class UpdateProfileRequest {
    private String nome;
    private String senha;
    private String foto_url;

    public UpdateProfileRequest(String nome, String senha, String foto_url) {
        this.nome = nome;
        this.senha = senha;
        this.foto_url = foto_url;
    }

    // getters (se precisarem)
    public String getNome() { return nome; }
    public String getSenha() { return senha; }
    public String getFoto_url() { return foto_url; }
}
