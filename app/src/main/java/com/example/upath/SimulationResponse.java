package com.example.upath;

public class SimulationResponse {
    private boolean aprovado;
    private String resultado;
    private String mensagem;

    // Getters para ler a resposta
    public boolean isAprovado() {
        return aprovado;
    }

    public String getResultado() {
        return resultado;
    }

    public String getMensagem() {
        return mensagem;
    }
}