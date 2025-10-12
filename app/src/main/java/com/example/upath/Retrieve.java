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

public class Retrieve extends AppCompatActivity {

    private EditText editEmailRecuperacao;
    private MaterialButton buttonEnviarEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_retrieve);

        // Vincular as views
        editEmailRecuperacao = findViewById(R.id.email);
        buttonEnviarEmail = findViewById(R.id.buttonEnviarEmail);

        // Estado inicial do botão desabilitado
        buttonEnviarEmail.setEnabled(false);

        // Verifica o e-mail em tempo real
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

        // Ajuste para modo EdgeToEdge
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

    /**
     * Método executado ao clicar no botão "Enviar".
     * Vinculado no XML via android:onClick="RecuperarConta"
     */
    public void RecuperarConta(View view) {
        String emailText = editEmailRecuperacao.getText().toString().trim();

        // Impede o envio se o botão estiver desabilitado
        if (!buttonEnviarEmail.isEnabled()) {
            Toast.makeText(this, "Informe um e-mail válido para recuperar sua conta.", Toast.LENGTH_LONG).show();
            return;
        }

        // Simula o envio do e-mail de recuperação
        Toast.makeText(this, "E-mail de recuperação enviado para: " + emailText, Toast.LENGTH_LONG).show();

        // Aguarda brevemente e redireciona para a tela de login
        buttonEnviarEmail.postDelayed(() -> {
            Intent intent = new Intent(Retrieve.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Fecha a tela de recuperação
        }, 1500); // 1,5 segundos de delay para o usuário ler o Toast
    }

    /**
     * Método para retornar à tela principal (login) se clicar em "voltar".
     */
    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
