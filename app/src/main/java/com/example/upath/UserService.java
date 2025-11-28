package com.example.upath;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserService {

    // --- ROTA: HOME (Cards e Boas-vindas) ---
    @GET("/api/v1/user/home")
    Call<ApiResponse<HomeData>> getHome(@Header("Authorization") String token);

    // --- ROTA: PERFIL (Dados do Usuário) ---
    @GET("/api/v1/user/profile")
    Call<ApiResponse<ProfileData>> getProfile(@Header("Authorization") String token);

    // --- ATUALIZAR PERFIL ---
    @PUT("/api/v1/user/profile")
    Call<ApiResponse<SimpleMessage>> updateProfile(@Header("Authorization") String token, @Body ProfileData dados);

    // --- TROCAR SENHA ---
    @PUT("/api/v1/user/password")
    Call<ApiResponse<SimpleMessage>> changePassword(@Header("Authorization") String token, @Body PasswordRequest dados);

    // --- LOGOUT ---
    @POST("/api/v1/user/logout")
    Call<ApiResponse<SimpleMessage>> logout(@Header("Authorization") String token);


    // ==========================================
    // CLASSES DE MODELO (Estruturas de Dados)
    // ==========================================

    // Resposta Genérica do seu Backend (success + data)
    class ApiResponse<T> {
        public boolean success;
        public T data;
        public String error;
    }

    // Dados da Home
    class HomeData {
        public String nome;
        public String imagem;
        public List<CardItem> cards;
    }

    // Um Card da Home
    class CardItem {
        public String titulo;
        public String descricao;
        public String imagem;
    }

    // Dados do Perfil
    class ProfileData {
        public int id_usuario;
        public String nome;
        public String email;
        public String foto_url;
        public String ultimo_login;

        // Construtor vazio para update parcial
        public ProfileData() {}
        public ProfileData(String nome, String email) {
            this.nome = nome;
            this.email = email;
        }
    }

    // Pedido de troca de senha
    class PasswordRequest {
        public String nova_senha;
        public PasswordRequest(String s) { this.nova_senha = s; }
    }

    // Mensagem simples {"message": "..."}
    class SimpleMessage {
        public String message;
    }
}