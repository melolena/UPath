package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast; // Importado para exibir a mensagem

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Register extends AppCompatActivity {

    // Declaração de todas as Views do formulário
    private EditText editNome;
    private EditText editEmail;
    private EditText editConfirmEmail;
    private TextInputEditText editPassword;
    private TextInputEditText editConfirmPassword;
    private MaterialButton botaoCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // 1. Encontrar e atribuir as views pelos seus IDs
        editNome = findViewById(R.id.nome);
        editEmail = findViewById(R.id.email);
        editConfirmEmail = findViewById(R.id.confirmEmail);

        // Encontrar os TextInputEditText dentro dos seus TextInputLayouts
        TextInputLayout inputPasswordLayout = findViewById(R.id.input_password_layout);
        editPassword = inputPasswordLayout.findViewById(R.id.password);

        TextInputLayout inputConfirmPasswordLayout = findViewById(R.id.input_confirm_password_layout);
        editConfirmPassword = inputConfirmPasswordLayout.findViewById(R.id.confirmPassword);

        botaoCadastrar = findViewById(R.id.botaoCadastrar);

        // 2. Desabilitar o botão no início
        botaoCadastrar.setEnabled(false);

        // 3. Crie e aplique o TextWatcher a todos os campos relevantes
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFieldsForEnableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editNome.addTextChangedListener(watcher);
        editEmail.addTextChangedListener(watcher);
        editConfirmEmail.addTextChangedListener(watcher);
        editPassword.addTextChangedListener(watcher);
        editConfirmPassword.addTextChangedListener(watcher);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Lógica para habilitar/desabilitar o botão de cadastro em tempo real.
     */
    private void checkFieldsForEnableButton() {
        String nomeText = editNome.getText().toString().trim();
        String emailText = editEmail.getText().toString().trim();
        String confirmEmailText = editConfirmEmail.getText().toString().trim();
        String passwordText = editPassword.getText().toString().trim();
        String confirmPasswordText = editConfirmPassword.getText().toString().trim();

        // 1. Verificar se o NOME está preenchido
        boolean isNameFilled = nomeText.length() > 0;

        // 2. Verificar se E-MAIL e CONFIRMAR E-MAIL estão preenchidos
        boolean emailFieldsFilled = emailText.length() > 0 && confirmEmailText.length() > 0;

        // 3. Verificar se SENHA e REPETIR SENHA estão preenchidos
        boolean passwordFieldsFilled = passwordText.length() > 0 && confirmPasswordText.length() > 0;

        // 4. Validações adicionais (Formato de E-mail e Tamanho Mínimo da Senha)
        boolean isEmailFormatValid = Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
        boolean isPasswordLengthValid = passwordText.length() >= 8;

        // Habilita o botão APENAS se todos os requisitos de PREENCHIMENTO e FORMATO forem atendidos
        boolean canEnableButton = isNameFilled &&
                emailFieldsFilled &&
                passwordFieldsFilled &&
                isEmailFormatValid &&
                isPasswordLengthValid;

        botaoCadastrar.setEnabled(canEnableButton);
    }

    /**
     * Executado quando o botão "Cadastrar" é pressionado.
     */
    public void registerUser(View view) {
        // Se o botão não estiver habilitado, a lógica não deve avançar.
        if (!botaoCadastrar.isEnabled()) {
            Toast.makeText(this, "Preencha todos os campos e valide os formatos.", Toast.LENGTH_LONG).show();
            return;
        }

        String emailText = editEmail.getText().toString().trim();
        String confirmEmailText = editConfirmEmail.getText().toString().trim();
        String passwordText = editPassword.getText().toString().trim();
        String confirmPasswordText = editConfirmPassword.getText().toString().trim();

        // 1. Validação crítica: E-mails devem ser iguais
        if (!emailText.equals(confirmEmailText)) {
            Toast.makeText(this, "Erro: Os e-mails não correspondem.", Toast.LENGTH_LONG).show();
            return;
        }

        // 2. Validação crítica: Senhas devem ser iguais
        if (!passwordText.equals(confirmPasswordText)) {
            Toast.makeText(this, "Erro: As senhas não correspondem.", Toast.LENGTH_LONG).show();
            return;
        }

        // Se passar por todas as validações: Cadastro realizado com sucesso

        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();

        // Redireciona para a MainActivity
        Intent intent = new Intent(this, MainActivity.class);

        // Limpa a pilha de atividades para que o usuário não possa voltar
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finaliza a tela de Cadastro

    }

    /**
     * Executado quando a seta de voltar é pressionada.
     */
    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
