package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Register extends AppCompatActivity {

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

        // 1. Vincular as views
        editEmail = findViewById(R.id.email);
        editConfirmEmail = findViewById(R.id.confirmEmail);

        TextInputLayout inputPasswordLayout = findViewById(R.id.input_password_layout);
        editPassword = inputPasswordLayout.findViewById(R.id.password);

        TextInputLayout inputConfirmPasswordLayout = findViewById(R.id.input_confirm_password_layout);
        editConfirmPassword = inputConfirmPasswordLayout.findViewById(R.id.confirmPassword);

        botaoCadastrar = findViewById(R.id.botaoCadastrar);

        // 2. Estado inicial desabilitado
        botaoCadastrar.setEnabled(false);
        botaoCadastrar.setClickable(true);
        botaoCadastrar.setFocusable(true);
        botaoCadastrar.refreshDrawableState();

        // 3. Monitorar mudanças nos campos
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFieldsForEnableButton();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        editEmail.addTextChangedListener(watcher);
        editConfirmEmail.addTextChangedListener(watcher);
        editPassword.addTextChangedListener(watcher);
        editConfirmPassword.addTextChangedListener(watcher);

        // Ajuste de padding para o modo EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para verificar e habilitar/desabilitar o botão
    private void checkFieldsForEnableButton() {
        String emailText = editEmail.getText().toString().trim();
        String confirmEmailText = editConfirmEmail.getText().toString().trim();
        String passwordText = editPassword.getText().toString().trim();
        String confirmPasswordText = editConfirmPassword.getText().toString().trim();

        boolean allFieldsFilled = !emailText.isEmpty()
                && !confirmEmailText.isEmpty()
                && !passwordText.isEmpty()
                && !confirmPasswordText.isEmpty();

        boolean isEmailFormatValid = Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
        boolean isPasswordLengthValid = passwordText.length() >= 8;

        // Só habilita se todos os requisitos forem atendidos
        boolean canEnableButton = allFieldsFilled && isEmailFormatValid && isPasswordLengthValid;

        botaoCadastrar.setEnabled(canEnableButton);
        botaoCadastrar.refreshDrawableState();
        botaoCadastrar.invalidate();
    }

    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
