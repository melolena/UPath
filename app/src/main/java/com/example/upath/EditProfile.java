package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfile extends AppCompatActivity {

    private TextInputEditText editName, editPassword;
    private MaterialButton botaoCadastrar, botaoCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Referências dos campos e botões
        editName = findViewById(R.id.input_name);
        editPassword = findViewById(R.id.input_password);
        botaoCadastrar = findViewById(R.id.button_confirm);
        botaoCancelar = findViewById(R.id.button_cancel);

        // Define o tint selector e desativa o botão inicialmente
        botaoCadastrar.setEnabled(false);


        // Observa alterações nos campos
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarCampos();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editName.addTextChangedListener(watcher);
        editPassword.addTextChangedListener(watcher);
    }

    // Verifica se os campos estão preenchidos
    private void validarCampos() {
        String nome = editName.getText().toString().trim();
        String senha = editPassword.getText().toString().trim();

        boolean camposPreenchidos = !nome.isEmpty() && !senha.isEmpty();
        botaoCadastrar.setEnabled(camposPreenchidos);
    }

    public void registerUser(View view) {
        if (!botaoCadastrar.isEnabled()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_LONG).show();
            return;
        }

        String nome = editName.getText().toString().trim();
        String senha = editPassword.getText().toString().trim();

        Toast.makeText(this, "Cadastro atualizado com sucesso!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
