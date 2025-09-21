package com.example.upath;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail;
    private TextInputEditText editPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.button_login);

        // Desativa o botão inicialmente
        buttonLogin.setEnabled(false);

        // Define a cor inicial do botão para a cor inativa
        buttonLogin.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray_inactive));
        buttonLogin.setTextColor(ContextCompat.getColor(this, R.color.white));
        buttonLogin.setBackgroundResource(R.color.white);

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

        if (!email.isEmpty() && !password.isEmpty()) {
            // Campos preenchidos: ative o botão e mude a cor
            buttonLogin.setEnabled(true);
            buttonLogin.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green_active));
        } else {
            // Campos vazios: desative o botão e retorne à cor original
            buttonLogin.setEnabled(false);
            buttonLogin.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray_inactive));
        }
    }
}