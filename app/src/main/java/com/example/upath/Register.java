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
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    private TextInputEditText editNome, editEmail, editConfirmEmail;
    private TextInputEditText editPassword, editConfirmPassword;

    // Layouts para mostrar as mensagens de erro
    private TextInputLayout layoutEmail, layoutConfirmEmail;
    private TextInputLayout layoutPassword, layoutConfirmPassword;

    private MaterialButton botaoCadastrar;

    private static final String BASE_URL = "http://10.0.2.2:8001/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Vincular Campos de Texto (Filhos)
        editNome = findViewById(R.id.nome);
        editEmail = findViewById(R.id.email);
        editConfirmEmail = findViewById(R.id.confirmEmail);
        editPassword = findViewById(R.id.password);
        editConfirmPassword = findViewById(R.id.confirmPassword);

        // 2. Vincular Layouts (Pais - Para mostrar o erro)
        layoutEmail = findViewById(R.id.input_email_layout);
        layoutConfirmEmail = findViewById(R.id.input_confirm_email_layout);
        layoutPassword = findViewById(R.id.input_password_layout);
        layoutConfirmPassword = findViewById(R.id.input_confirm_password_layout);

        botaoCadastrar = findViewById(R.id.botaoCadastrar);

        // 3. Botão
        botaoCadastrar.setOnClickListener(v -> registerUser());
        botaoCadastrar.setEnabled(false);

        // 4. Monitoramento em Tempo Real
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

        // 5. Ajuste visual (EdgeToEdge)
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }
    }

    // --- VALIDAÇÃO COMPLETA ---
    private void checkFields() {
        String nome = safeGetText(editNome);
        String email = safeGetText(editEmail);
        String cEmail = safeGetText(editConfirmEmail);
        String senha = safeGetText(editPassword);
        String cSenha = safeGetText(editConfirmPassword);

        // 1. Validação de E-MAIL
        boolean emailValido = Patterns.EMAIL_ADDRESS.matcher(email).matches();

        if (!email.isEmpty() && !emailValido) {
            layoutEmail.setError("E-mail inválido.");
        } else {
            layoutEmail.setError(null);
        }

        // 2. Confirmação de E-MAIL
        boolean emailsIguais = email.equals(cEmail);
        if (!cEmail.isEmpty() && !emailsIguais) {
            layoutConfirmEmail.setError("Os e-mails não coincidem.");
        } else {
            layoutConfirmEmail.setError(null);
        }

        // 3. Força da SENHA
        String erroSenha = validarForcaSenha(senha);
        if (!senha.isEmpty() && erroSenha != null) {
            layoutPassword.setError(erroSenha);
            layoutPassword.setErrorIconDrawable(null); // Tira o ícone para não cobrir o olho
        } else {
            layoutPassword.setError(null);
        }

        // 4. Confirmação de SENHA
        boolean senhasIguais = senha.equals(cSenha);
        if (!cSenha.isEmpty() && !senhasIguais) {
            layoutConfirmPassword.setError("As senhas não coincidem.");
        } else {
            layoutConfirmPassword.setError(null);
        }

        // --- HABILITAR BOTÃO ---
        boolean tudoCerto = !nome.isEmpty()
                && emailValido
                && emailsIguais
                && (erroSenha == null)
                && senhasIguais
                && !senha.isEmpty();

        botaoCadastrar.setEnabled(tudoCerto);
    }

    private String validarForcaSenha(String senha) {
        if (senha.length() < 8) return "Mínimo de 8 caracteres.";
        if (!Pattern.compile("[0-9]").matcher(senha).find()) return "Precisa ter um número.";
        if (!Pattern.compile("[a-z]").matcher(senha).find()) return "Precisa ter letra minúscula.";
        if (!Pattern.compile("[A-Z]").matcher(senha).find()) return "Precisa ter letra MAIÚSCULA.";
        if (!Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(senha).find()) return "Precisa ter caractere especial.";
        return null;
    }

    private String safeGetText(TextInputEditText editText) {
        return (editText != null && editText.getText() != null) ? editText.getText().toString().trim() : "";
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
            RegisterRequest req = new RegisterRequest(nome, email, confirmEmail, senha, confirmSenha);

            service.cadastrar(req).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    botaoCadastrar.setEnabled(true);
                    botaoCadastrar.setText("Cadastrar");

                    if (response.isSuccessful()) {
                        Toast.makeText(Register.this, "Cadastro realizado!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Register.this, MainActivity.class));
                        finish();
                    } else {
                        if (response.code() == 409) {
                            layoutEmail.setError("E-mail já cadastrado.");
                            Toast.makeText(Register.this, "E-mail já existe.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Register.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    botaoCadastrar.setEnabled(true);
                    botaoCadastrar.setText("Cadastrar");
                    Toast.makeText(Register.this, "Sem conexão.", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            botaoCadastrar.setEnabled(true);
            botaoCadastrar.setText("Cadastrar");
            e.printStackTrace();
        }
    }

    public void goToMainActivity(View view) {
        finish();
    }
}