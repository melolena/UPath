package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class NewPassword extends AppCompatActivity {

    private EditText inputNewPassword, inputConfirmPassword;
    private MaterialButton buttonSalvarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        inputNewPassword = findViewById(R.id.inputNewPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        buttonSalvarSenha = findViewById(R.id.buttonSalvarSenha);

        // botão começa desabilitado
        buttonSalvarSenha.setEnabled(false);
        buttonSalvarSenha.setBackgroundTintList(
                getColorStateList(R.color.button_disabled_tint)
        );

        // Watcher igual ao login
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarSenhas();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        inputNewPassword.addTextChangedListener(watcher);
        inputConfirmPassword.addTextChangedListener(watcher);
    }

    private void validarSenhas() {
        String senha = inputNewPassword.getText().toString().trim();
        String confirmar = inputConfirmPassword.getText().toString().trim();

        boolean camposValidos = !senha.isEmpty()
                && !confirmar.isEmpty()
                && senha.equals(confirmar);

        if (camposValidos) {
            buttonSalvarSenha.setEnabled(true);
            buttonSalvarSenha.setBackgroundTintList(
                    getColorStateList(R.color.button_cadastro_tint) // verde
            );
        } else {
            buttonSalvarSenha.setEnabled(false);
            buttonSalvarSenha.setBackgroundTintList(
                    getColorStateList(R.color.button_disabled_tint) // cinza
            );
        }
    }

    public void SalvarSenha(android.view.View view) {
        if (!buttonSalvarSenha.isEnabled()) return;

        Toast.makeText(this, "Senha redefinida com sucesso!", Toast.LENGTH_LONG).show();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
