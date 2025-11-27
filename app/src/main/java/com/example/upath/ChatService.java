package com.example.upath;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ChatService {

    // --- ROTAS DO CHAT (ActivityTeste) ---
    @POST("/chat")
    Call<ChatApiResponse> sendMessage(@Body ChatRequest request);

    @GET("/resultado")
    Call<ResultResponse> getResult();

    // --- NOVA ROTA DA SIMULAÇÃO (ActivitySimulation) ---
    // Esta é a linha que faltava para o erro sumir!
    @POST("/simular")
    Call<SimulationResponse> realizarSimulacao(@Body SimulationRequest request);


    // --- CLASSE INTERNA (Resposta do Chat) ---
    class ChatApiResponse {
        public String reply;

        @SerializedName("final") // Mapeia o campo JSON "final" para "isFinal"
        public boolean isFinal;
    }
}