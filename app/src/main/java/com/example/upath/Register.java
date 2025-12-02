package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    private TextInputEditText editNome, editEmail, editConfirmEmail;
    private TextInputEditText editPassword, editConfirmPassword;
    private MaterialButton botaoCadastrar;

    // CORREÇÃO AQUI: Deixei apenas a raiz. O restante (api/v1/auth/register) está no AuthService.
    private static final String BASE_URL = "http://10.0.2.2:8001/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Vincular os componentes
        editNome = findViewById(R.id.nome);
        editEmail = findViewById(R.id.email);
        editConfirmEmail = findViewById(R.id.confirmEmail);
        editPassword = findViewById(R.id.password);
        editConfirmPassword = findViewById(R.id.confirmPassword);
        botaoCadastrar = findViewById(R.id.botaoCadastrar);

        // 2. Configurar o clique do botão
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        botaoCadastrar.setEnabled(false);

        // 3. Monitorar digitação
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

        // 4. Ajuste visual (segurança contra crash se ID mudar)
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }
    }

    private void checkFields() {
        String nome = safeGetText(editNome);
        String email = safeGetText(editEmail);
        String cEmail = safeGetText(editConfirmEmail);
        String pass = safeGetText(editPassword);
        String cPass = safeGetText(editConfirmPassword);

        boolean ok = !nome.isEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && email.equals(cEmail)
                && pass.length() >= 8
                && pass.equals(cPass);

        botaoCadastrar.setEnabled(ok);
    }

    private String safeGetText(TextInputEditText editText) {
        if (editText != null && editText.getText() != null) {
            return editText.getText().toString().trim();
        }
        return "";
    }

    public void registerUser() {
        String nome = safeGetText(editNome);
        String email = safeGetText(editEmail);
        String confirmEmail = safeGetText(editConfirmEmail);
        String senha = safeGetText(editPassword);
        String confirmSenha = safeGetText(editConfirmPassword);

        botaoCadastrar.setEnabled(false);
        botaoCadastrar.setText("Cadastrando...");

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AuthService service = retrofit.create(AuthService.class);

            RegisterRequest req = new RegisterRequest(
                    nome, email, confirmEmail, senha, confirmSenha
            );

            service.cadastrar(req).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    botaoCadastrar.setEnabled(true);
                    botaoCadastrar.setText("Cadastrar");

                    if (response.isSuccessful()) {
                        Toast.makeText(Register.this, "Sucesso! Faça login.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Register.this, MainActivity.class));
                        finish();
                    } else {
                        // Se der erro, mostra o código (ex: 400, 500)
                        Toast.makeText(Register.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    botaoCadastrar.setEnabled(true);
                    botaoCadastrar.setText("Cadastrar");
                    Toast.makeText(Register.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            botaoCadastrar.setEnabled(true);
            botaoCadastrar.setText("Cadastrar");
            Toast.makeText(this, "Erro interno: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void goToMainActivity(View view) {
        finish();
    }
}