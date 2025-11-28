package com.example.upath;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    // Verifique no seu main.py se existe um prefixo "/api/v1".
    // Se tiver, mude para "/api/v1/auth/login"
    @POST("/api/v1/auth/login")
    Call<LoginResponse> fazerLogin(@Body LoginRequest request);

    // Futuramente você usará o registro também:
    // @POST("/auth/register")
    // Call<RegisterResponse> registrar(@Body RegisterRequest request);
    @POST("/api/v1/auth/register")
    Call<LoginResponse> cadastrar(@Body RegisterRequest request);
}