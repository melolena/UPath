package com.example.upath;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ChatService {

    @POST("/chat")
    Call<ChatApiResponse> sendMessage(@Body ChatRequest request);

    @GET("/resultado")
    Call<ResultResponse> getResult();

    // Essa classe interna PODE ficar aqui, pois é específica da resposta do chat
    class ChatApiResponse {
        public String reply;
        @SerializedName("final")
        public boolean isFinal;
    }
}