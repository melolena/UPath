package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail;
    private TextInputEditText editPassword;
    private Button buttonLogin;

    // URL do seu Backend Python (FastAPI)
    // Se for emulador: http://10.0.2.2:8000/
    // Se for celular físico: http://SEU_IP_DO_PC:8000/
    private static final String BASE_URL = "http://10.0.2.2:8001/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Mapeamento das Views
        editEmail = findViewById(R.id.edit_email);
        TextInputLayout inputPasswordLayout = findViewById(R.id.input_password_layout);
        editPassword = findViewById(R.id.edit_password); // Assumindo que o ID direto no XML é esse
        buttonLogin = findViewById(R.id.botao_logar);

        // Desativa o botão inicialmente
        buttonLogin.setEnabled(false);

        // 2. Monitoramento de Digitação
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { checkFields(); }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        editEmail.addTextChangedListener(textWatcher);
        editPassword.addTextChangedListener(textWatcher);
    }

    private boolean checkFields() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        boolean isEmailFormatValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean isFilled = !email.isEmpty() && !password.isEmpty();

        buttonLogin.setEnabled(isFilled && isEmailFormatValid);

        if (!email.isEmpty() && !isEmailFormatValid) {
            editEmail.setError("E-mail inválido");
        } else {
            editEmail.setError(null);
        }

        return isFilled;
    }

    // --- AQUI COMEÇA A INTEGRAÇÃO REAL ---
    public void goToHome(View view) {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Feedback visual (Bloqueia botão e muda texto)
        buttonLogin.setEnabled(false);
        buttonLogin.setText("Entrando...");

        // 1. Configura Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthService service = retrofit.create(AuthService.class);

        // 2. Prepara os dados (LoginRequest que criamos antes)
        LoginRequest loginData = new LoginRequest(email, password);

        // 3. Faz a chamada ao servidor
        service.fazerLogin(loginData).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                // Restaura o botão
                buttonLogin.setEnabled(true);
                buttonLogin.setText("Logar");

                if (response.isSuccessful() && response.body() != null) {
                    // SUCESSO! O servidor aceitou o login
                    String token = response.body().getAccessToken();
                    String nome = response.body().getUser().getNome(); // Pega nome do usuário

                    Toast.makeText(MainActivity.this, "Bem-vindo, " + nome + "!", Toast.LENGTH_SHORT).show();

                    // Salva o token (Importante para o futuro)
                    getSharedPreferences("UPATH_PREFS", MODE_PRIVATE)
                            .edit()
                            .putString("ACCESS_TOKEN", token)
                            .apply();

                    // Vai para a Home
                    Intent intent = new Intent(MainActivity.this, HomeUser.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    // ERRO (Senha errada ou usuário não existe)
                    Toast.makeText(MainActivity.this, "E-mail ou senha incorretos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // ERRO DE CONEXÃO (Servidor desligado ou IP errado)
                buttonLogin.setEnabled(true);
                buttonLogin.setText("Logar");

                Toast.makeText(MainActivity.this, "Falha na conexão. O servidor está rodando?", Toast.LENGTH_LONG).show();
                Log.e("LOGIN_ERROR", "Erro: " + t.getMessage());
            }
        });
    }

    public void goToRegister (View view) {
        startActivity(new Intent(this, Register.class));
    }

    public void goToRetriever (View view) {
        startActivity(new Intent(this, Retrieve.class));
    }
}