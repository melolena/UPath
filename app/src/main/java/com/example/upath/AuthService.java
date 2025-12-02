package com.example.upath;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthService {

    // LOGIN
    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/login")
    Call<LoginResponse> fazerLogin(@Body LoginRequest request);

    // REGISTER
    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/register")
    Call<LoginResponse> cadastrar(@Body RegisterRequest request);
}
