package com.example.upath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail;
    private TextInputEditText editPassword;
    private MaterialButton buttonLogin;

    // URL base correta
    private static final String BASE_URL = "http://10.0.2.2:8001/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.botao_logar);

        buttonLogin.setEnabled(false);

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        editEmail.addTextChangedListener(watcher);
        editPassword.addTextChangedListener(watcher);
    }

    private void checkFields() {
        String email = editEmail.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();

        boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean filled = !email.isEmpty() && !pass.isEmpty();

        buttonLogin.setEnabled(validEmail && filled);

        if (validEmail) {
            editEmail.setError(null);
        }
    }

    public void goToHome(View view) {
        String email = editEmail.getText().toString().trim();
        String senha = editPassword.getText().toString().trim();

        buttonLogin.setEnabled(false);
        buttonLogin.setText("Entrando...");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthService api = retrofit.create(AuthService.class);

        LoginRequest req = new LoginRequest(email, senha);

        api.fazerLogin(req).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                buttonLogin.setEnabled(true);
                buttonLogin.setText("Logar");

                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse res = response.body();

                    SharedPreferences prefs = getSharedPreferences("UPATH_PREFS", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    if (res.getAccessToken() != null) {
                        editor.putString("ACCESS_TOKEN", res.getAccessToken());
                    }

                    if (res.getUser() != null) {
                        editor.putString("USER_NAME", res.getUser().getNome());
                        editor.putString("USER_EMAIL", res.getUser().getEmail());

                        // --- CORREÇÃO: Limpa a foto anterior se o usuário novo não tiver foto ---
                        if (res.getUser().getFotoUrl() != null) {
                            editor.putString("USER_PHOTO", res.getUser().getFotoUrl());
                        } else {
                            editor.remove("USER_PHOTO"); // Garante o ícone padrão
                        }
                    }
                    editor.apply();

                    Toast.makeText(MainActivity.this, "Bem-vindo!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, HomeUser.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    if (response.code() == 401) {
                        Toast.makeText(MainActivity.this, "Senha ou E-mail incorretos.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                buttonLogin.setEnabled(true);
                buttonLogin.setText("Logar");
                Toast.makeText(MainActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToRegister(View view) {
        startActivity(new Intent(this, Register.class));
    }

    public void goToRetriever(View view) {
        startActivity(new Intent(this, Retrieve.class));
    }
}