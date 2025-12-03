package com.example.upath;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    // Lançador para abrir a galeria
    ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            selectedImage = uri;
                            // Mostra a imagem na tela imediatamente para o usuário ver o que escolheu
                            Glide.with(this).load(uri).into(profileImg);
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // 1. Configurar Toolbar (Seta de voltar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // 2. Vincular Views
        profileImg = findViewById(R.id.profile_image);
        inputName = findViewById(R.id.input_name);
        inputPassword = findViewById(R.id.input_password);
        buttonConfirm = findViewById(R.id.button_confirm);
        buttonCancel = findViewById(R.id.button_cancel);

        // 3. Carregar dados atuais
        var prefs = getSharedPreferences("UPATH_PREFS", MODE_PRIVATE);
        inputName.setText(prefs.getString("USER_NAME", ""));

        String foto = prefs.getString("USER_PHOTO", null);
        if (foto != null) {
            Glide.with(this).load(foto).into(profileImg);
        }

        // 4. Ações dos botões
        findViewById(R.id.btn_change_photo).setOnClickListener(v -> pickImage.launch("image/*"));

        buttonCancel.setOnClickListener(v -> finish());

        buttonConfirm.setOnClickListener(v -> salvarAlteracoes());

        // 5. Iniciar Retrofit
        userService = ApiClient.getClient().create(UserService.class);
    }

    private void salvarAlteracoes() {
        // Pega o texto de forma segura
        String nome = (inputName.getText() != null) ? inputName.getText().toString().trim() : "";
        String senha = (inputPassword.getText() != null) ? inputPassword.getText().toString().trim() : "";

        if (nome.isEmpty()) {
            Toast.makeText(this, "O nome não pode ser vazio.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Trava o botão para não clicar duas vezes
        buttonConfirm.setEnabled(false);
        buttonConfirm.setText("Salvando...");

        // Preparar dados de Texto
        RequestBody nomeReq = RequestBody.create(MediaType.parse("text/plain"), nome);

        // Se a senha estiver vazia, manda vazio mesmo (o backend ignora)
        RequestBody senhaReq = RequestBody.create(MediaType.parse("text/plain"), senha);

        // Preparar a Foto (Multipart)
        MultipartBody.Part fotoPart = null;

        if (selectedImage != null) {
            try {
                // Converte a Uri da galeria para um Arquivo real usando a classe FileUtils
                File file = FileUtils.getFile(this, selectedImage);

                RequestBody reqFile = RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );

                fotoPart = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao processar imagem", Toast.LENGTH_SHORT).show();
                buttonConfirm.setEnabled(true);
                buttonConfirm.setText("Salvar");
                return;
            }
        }

        // Recuperar Token
        var prefs = getSharedPreferences("UPATH_PREFS", MODE_PRIVATE);
        String token = "Bearer " + prefs.getString("ACCESS_TOKEN", "");

        // Chamada API
        userService.updateProfile(token, nomeReq, senhaReq, fotoPart)
                .enqueue(new Callback<UpdateProfileResponse>() {
                    @Override
                    public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                        buttonConfirm.setEnabled(true);
                        buttonConfirm.setText("Salvar");

                        if (response.isSuccessful() && response.body() != null) {
                            var data = response.body().data;

                            // ATUALIZA A MEMÓRIA DO APP (Isso é crucial)
                            var editor = prefs.edit();

                            if (data.nome != null) editor.putString("USER_NAME", data.nome);
                            if (data.email != null) editor.putString("USER_EMAIL", data.email);

                            // Se veio foto nova, salva. Se veio null (não mudou), mantém a antiga.
                            if (data.fotoUrl != null) {
                                editor.putString("USER_PHOTO", data.fotoUrl);
                            }

                            editor.apply();

                            Toast.makeText(EditProfile.this, "Perfil atualizado!", Toast.LENGTH_SHORT).show();

                            // Fecha a tela. O onResume() da tela anterior vai atualizar a foto lá.
                            finish();
                        } else {
                            Toast.makeText(EditProfile.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                        buttonConfirm.setEnabled(true);
                        buttonConfirm.setText("Salvar");
                        Toast.makeText(EditProfile.this, "Erro de conexão.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}