// com/example/upath/AuthService.java
package com.example.upath;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Header;

public interface AuthService {

    @POST("/api/v1/auth/login")
    Call<LoginResponse> fazerLogin(@Body LoginRequest request);

    @POST("/api/v1/auth/register")
    Call<LoginResponse> cadastrar(@Body RegisterRequest request);

    // NOVO: atualizar perfil (precisa enviar token no header Authorization: Bearer <token>)
    @PUT("/api/v1/auth/update")
    Call<UpdateProfileOut> atualizarPerfil(@Header("Authorization") String bearerToken, @Body UpdateProfileRequest request);
}
