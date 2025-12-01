package com.example.upath;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatService {

    // 🔥 OBRIGATÓRIO para permitir caracteres UTF-8
    @Headers({
            "Content-Type: application/json; charset=utf-8"
    })
    @POST("chat")
    Call<ChatApiResponse> sendMessage(@Body ChatRequest request);


    @GET("resultado")
    Call<ResultResponse> getResult();


    // 🔥 Também precisa UTF-8 aqui
    @Headers({
            "Content-Type: application/json; charset=utf-8"
    })
    @POST("simular")
    Call<SimulationResponse> realizarSimulacao(@Body SimulationRequest request);


    class ChatApiResponse {
        public String reply;

        @com.google.gson.annotations.SerializedName("final")
        public boolean isFinal;
    }
}
