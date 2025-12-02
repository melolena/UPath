package com.example.upath;

public class Membro {
    private String nome;
    private String cargo;
    private int foto;

    public Membro(String nome, String cargo, int foto) {
        this.nome = nome;
        this.cargo = cargo;
        this.foto = foto;
    }

    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public int getFoto() { return foto; }
}
