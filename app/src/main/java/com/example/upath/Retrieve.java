package com.example.upath;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class Retrieve extends AppCompatActivity {

    // Views de entrada
    private EditText editEmailRecuperacao;
    private MaterialButton buttonEnviarEmail;
    private TextView textInstrucao, textLabelEmail;

    // View de sucesso
    private TextView textSucesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_retrieve);

        // 1. Inicializar todas as Views
        editEmailRecuperacao = findViewById(R.id.email);
        buttonEnviarEmail = findViewById(R.id.buttonEnviarEmail);
        textInstrucao = findViewById(R.id.textInstrucao);
        textLabelEmail = findViewById(R.id.textLabelEmail);
        textSucesso = findViewById(R.id.textSucesso);

        // 2. Estado inicial
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkEmailField() {
        String emailText = editEmailRecuperacao.getText().toString().trim();
        boolean isEmailFilled = !emailText.isEmpty();
        boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
        buttonEnviarEmail.setEnabled(isEmailFilled && isEmailValid);
    }

    public void RecuperarConta(View view) {
        String emailText = editEmailRecuperacao.getText().toString().trim();

        if (!buttonEnviarEmail.isEnabled()) {
            return;
        }

        // SIMULAÇÃO DE ENVIO
        buttonEnviarEmail.setText("Enviando...");
        buttonEnviarEmail.setEnabled(false);

        // Simula delay de rede (1s) e depois troca a tela
        buttonEnviarEmail.postDelayed(() -> {
            // ESCONDE os campos de entrada
            editEmailRecuperacao.setVisibility(View.GONE);
            buttonEnviarEmail.setVisibility(View.GONE);
            textInstrucao.setVisibility(View.GONE);
            textLabelEmail.setVisibility(View.GONE);

            // MOSTRA a mensagem de sucesso igual a imagem
            textSucesso.setVisibility(View.VISIBLE);

            // Opcional: Feedback extra via Toast
            Toast.makeText(Retrieve.this, "E-mail enviado com sucesso!", Toast.LENGTH_SHORT).show();
        }, 1000);
    }

    public void goToMainActivity(View view) {
        finish();
    }
}