package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View; // Importar View é crucial para o android:onClick
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail;
    private TextInputEditText editPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assumindo que os IDs no XML agora são 'inputEmail', 'inputPassword' e 'botao-logar' (da refatoração anterior)
        // Se você não usou a refatoração anterior, ajuste os R.id. aqui.
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.botao_logar);

        // Desativa o botão inicialmente (o selector cuida da cor cinza)
        buttonLogin.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editEmail.addTextChangedListener(textWatcher);
        editPassword.addTextChangedListener(textWatcher);
    }

    private void checkFields() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Verifica se o e-mail não está vazio e tem formato válido
        boolean isEmailValid = !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();

        // Verifica se a senha não está vazia
        boolean isPasswordFilled = !password.isEmpty();

        // Habilita o botão apenas se os dois campos forem válidos
        buttonLogin.setEnabled(isEmailValid && isPasswordFilled);

        // Mostra erro no campo de e-mail se o formato estiver incorreto
        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Formato de e-mail inválido");
        } else {
            editEmail.setError(null);
        }
    }

    /**
     * Método chamado quando o TextView "link-cadastro" é clicado via android:onClick="go-to-register".
     * Inicia a tela de registro.
     * * Nota: Embora seja boa prática usar notação camelCase em Java (ex: goToRegister),
     * estamos usando 'go_to_register' para corresponder ao valor no XML ('go-to-register' não é válido em Java).
     */
    public void goToRegister (View view) {
        // Crie um Intent para ir para a RegisterActivity
        // Certifique-se de que a RegisterActivity existe e está declarada no AndroidManifest.xml
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        // Opcional: chame finish() se não quiser que o usuário volte para a tela de Login
        // finish();
    }
}


