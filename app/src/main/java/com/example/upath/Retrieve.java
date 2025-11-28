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

// Se no futuro tiver backend, importe Retrofit aqui
// import retrofit2.Call;
// import retrofit2.Callback;
// import retrofit2.Response;

public class Retrieve extends AppCompatActivity {

    private EditText editEmailRecuperacao;
    private MaterialButton buttonEnviarEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_retrieve);

        // 1. Inicializar Views
        editEmailRecuperacao = findViewById(R.id.email);
        buttonEnviarEmail = findViewById(R.id.buttonEnviarEmail);

        // 2. Estado inicial do botão (Desabilitado)
        buttonEnviarEmail.setEnabled(false);

        // 3. Monitoramento do campo de e-mail
        editEmailRecuperacao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEmailField();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 4. Ajuste Visual (EdgeToEdge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Verifica se o e-mail é válido para habilitar o botão
    private void checkEmailField() {
        String emailText = editEmailRecuperacao.getText().toString().trim();
        boolean isEmailFilled = !emailText.isEmpty();
        boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(emailText).matches();

        buttonEnviarEmail.setEnabled(isEmailFilled && isEmailValid);

        // Feedback visual de erro (opcional)
        if (isEmailFilled && !isEmailValid) {
            editEmailRecuperacao.setError("E-mail inválido");
        } else {
            editEmailRecuperacao.setError(null);
        }
    }

    /**
     * Executado ao clicar no botão "Enviar" (Vinculado via android:onClick="RecuperarConta" no XML)
     */
    public void RecuperarConta(View view) {
        String emailText = editEmailRecuperacao.getText().toString().trim();

        if (!buttonEnviarEmail.isEnabled()) {
            return;
        }

        // --- FUTURA INTEGRAÇÃO COM BACKEND ---
        // Quando seu Python tiver a rota (ex: /auth/forgot-password), o código seria:
        /*
        AuthService service = ... (Retrofit config)
        service.recuperarSenha(new RecoveryRequest(emailText)).enqueue(new Callback... {
            onResponse -> "Email enviado com sucesso!"
            onFailure -> "Erro ao enviar"
        });
        */

        // --- POR ENQUANTO: SIMULAÇÃO ---

        // Feedback visual
        buttonEnviarEmail.setEnabled(false);
        buttonEnviarEmail.setText("Enviando...");

        // Simula um tempo de rede (1.5 segundos)
        buttonEnviarEmail.postDelayed(() -> {
            Toast.makeText(Retrieve.this, "Se o e-mail existir, você receberá um link.", Toast.LENGTH_LONG).show();

            // Volta para o Login
            Intent intent = new Intent(Retrieve.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }, 1500);
    }

    /**
     * Botão Voltar
     */
    public void goToMainActivity(View view) {
        finish(); // Apenas fecha a tela atual, revelando o Login que está embaixo
    }
}