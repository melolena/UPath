package com.example.upath;


import com.example.upath.ApiClient;
import com.example.upath.FileUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    private TextInputEditText inputName, inputPassword;
    private MaterialButton buttonConfirm, buttonCancel;
    private ImageView profileImg;

    private Uri selectedImage = null;
    private UserService userService;

    ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            selectedImage = uri;
                            Glide.with(this).load(uri).into(profileImg);
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImg = findViewById(R.id.profile_image);
        inputName = findViewById(R.id.input_name);
        inputPassword = findViewById(R.id.input_password);
        buttonConfirm = findViewById(R.id.button_confirm);
        buttonCancel = findViewById(R.id.button_cancel);

        // Carregar dados salvos
        var prefs = getSharedPreferences("UPATH_PREFS", MODE_PRIVATE);
        inputName.setText(prefs.getString("USER_NAME", ""));
        String foto = prefs.getString("USER_PHOTO", null);
        if (foto != null) Glide.with(this).load(foto).into(profileImg);

        findViewById(R.id.btn_change_photo).setOnClickListener(v -> pickImage.launch("image/*"));
        buttonCancel.setOnClickListener(v -> finish());

        userService = ApiClient.getClient().create(UserService.class);

        buttonConfirm.setOnClickListener(v -> salvarAlteracoes());
    }


    private void salvarAlteracoes() {

        String nome = inputName.getText().toString().trim();
        String senha = inputPassword.getText().toString().trim();

        if (nome.isEmpty()) {
            Toast.makeText(this, "Digite seu nome.", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody nomeReq =
                RequestBody.create(MediaType.parse("text/plain"), nome);

        MultipartBody.Part fotoPart = null;

        if (selectedImage != null) {
            File file = FileUtils.getFile(this, selectedImage);

            RequestBody reqFile = RequestBody.create(
                    MediaType.parse(getContentResolver().getType(selectedImage)),
                    file
            );

            fotoPart = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);
        }

        RequestBody senhaReq =
                senha.isEmpty()
                        ? null
                        : RequestBody.create(MediaType.parse("text/plain"), senha);

        userService.updateProfile(nomeReq, senhaReq, fotoPart)
                .enqueue(new Callback<UpdateProfileResponse>() {
                    @Override
                    public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            var data = response.body().data;

                            var prefs = getSharedPreferences("UPATH_PREFS", MODE_PRIVATE).edit();
                            prefs.putString("USER_NAME", data.nome);
                            prefs.putString("USER_EMAIL", data.email);
                            prefs.putString("USER_PHOTO", data.fotoUrl);
                            prefs.apply();

                            Toast.makeText(EditProfile.this, "Perfil atualizado!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(EditProfile.this, "Erro ao atualizar.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                        Toast.makeText(EditProfile.this, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
