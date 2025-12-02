package com.example.upath;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UserService {

    // CORREÇÃO: A rota deve bater com o Python (/api/v1/auth/update-me)
    @Multipart
    @PUT("auth/update-me")
    Call<UpdateProfileResponse> updateProfile(
            @Header("Authorization") String token,
            @Part("nome") RequestBody nome,
            @Part("senha") RequestBody senha,
            @Part MultipartBody.Part foto
    );
}