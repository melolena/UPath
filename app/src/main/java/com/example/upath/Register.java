package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    private EditText editNome, editEmail, editConfirmEmail;
    private TextInputEditText editPassword, editConfirmPassword;
    private MaterialButton botaoCadastrar;

    // URL do Backend
    // Lembre-se: Use 10.0.2.2 para emulador ou o IP da máquina para celular físico
    private static final String BASE_URL = "http://10.0.2.2:8001/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // 1. Inicializar Views
        editNome = findViewById(R.id.nome);
        editEmail = findViewById(R.id.email);
        editConfirmEmail = findViewById(R.id.confirmEmail);

        TextInputLayout inputPass = findViewById(R.id.input_password_layout);
        editPassword = inputPass.findViewById(R.id.password);

        TextInputLayout inputConfirmPass = findViewById(R.id.input_confirm_password_layout);
        editConfirmPassword = inputConfirmPass.findViewById(R.id.confirmPassword);

        botaoCadastrar = findViewById(R.id.botaoCadastrar);
        botaoCadastrar.setEnabled(false);

        // 2. TextWatcher para habilitar botão
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { checkFields(); }
            @Override public void afterTextChanged(Editable s) {}
        };

        editNome.addTextChangedListener(watcher);
        editEmail.addTextChangedListener(watcher);
        editConfirmEmail.addTextChangedListener(watcher);
        editPassword.addTextChangedListener(watcher);
        editConfirmPassword.addTextChangedListener(watcher);

        // 3. Layout Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkFields() {
        String nome = editNome.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String confEmail = editConfirmEmail.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();
        String confPass = editConfirmPassword.getText().toString().trim();

        boolean isValid = !nome.isEmpty() &&
                !email.isEmpty() &&
                !confEmail.isEmpty() &&
                !pass.isEmpty() &&
                !confPass.isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                pass.length() >= 8;

        botaoCadastrar.setEnabled(isValid);
    }

    public void registerUser(View view) {
        String nome = editNome.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String confEmail = editConfirmEmail.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();
        String confPass = editConfirmPassword.getText().toString().trim();

        if (!email.equals(confEmail)) {
            Toast.makeText(this, "Os e-mails não coincidem!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(confPass)) {
            Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- CHAMADA PARA O SERVIDOR ---
        botaoCadastrar.setEnabled(false);
        botaoCadastrar.setText("Cadastrando...");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthService service = retrofit.create(AuthService.class);

        // --- CORREÇÃO AQUI ---
        // Agora passamos os 5 parâmetros que o Python espera (incluindo as confirmações)
        RegisterRequest request = new RegisterRequest(nome, email, confEmail, pass, confPass);

        service.cadastrar(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                botaoCadastrar.setEnabled(true);
                botaoCadastrar.setText("Cadastrar");

                if (response.isSuccessful()) {
                    Toast.makeText(Register.this, "Cadastro realizado! Faça login.", Toast.LENGTH_LONG).show();

                    // Vai para a tela de Login
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // Trata erro (ex: email já existe ou validação falhou)
                    if (response.code() == 422) {
                        Toast.makeText(Register.this, "Erro de validação (422). Verifique os dados.", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 409) {
                        Toast.makeText(Register.this, "E-mail já cadastrado.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, "Erro no cadastro: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                botaoCadastrar.setEnabled(true);
                botaoCadastrar.setText("Cadastrar");
                Toast.makeText(Register.this, "Erro de conexão.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToMainActivity(View view) {
        finish();
    }
}