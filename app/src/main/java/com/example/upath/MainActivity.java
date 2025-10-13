package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast; // Importar Toast para mensagens ao usuário

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout; // Adicionar se for necessário acessar a senha através do TextInputLayout

public class MainActivity extends AppCompatActivity {

    private EditText editEmail;
    private TextInputEditText editPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Garanta que este é o seu layout de login

        // 1. Mapeamento das Views
        editEmail = findViewById(R.id.edit_email);

        // Para acessar o TextInputEditText, é mais seguro procurar o TextInputLayout primeiro,
        // mas assumindo que o ID edit_password está no TextInputEditText:
        // Se houver erro, mude para: TextInputLayout inputLayout = findViewById(R.id.input_password_layout); editPassword = inputLayout.findViewById(R.id.edit_password);
        TextInputLayout inputPasswordLayout = findViewById(R.id.input_password_layout);
        editPassword = inputPasswordLayout.findViewById(R.id.edit_password);

        buttonLogin = findViewById(R.id.botao_logar);

        // Desativa o botão inicialmente
        buttonLogin.setEnabled(false);

        // 2. TextWatcher para monitorar a digitação
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

    /**
     * Verifica se os campos de e-mail e senha são válidos e habilita/desabilita o botão.
     * @return true se o login é válido e pronto para navegação, false caso contrário.
     */
    private boolean checkFields() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // 1. Validações
        boolean isEmailFormatValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean isEmailFilled = !email.isEmpty();
        boolean isPasswordFilled = !password.isEmpty();

        // 2. Lógica do botão
        boolean canEnableButton = isEmailFilled && isPasswordFilled && isEmailFormatValid;
        buttonLogin.setEnabled(canEnableButton);

        // 3. Feedback visual (Opcional, mas útil)
        if (isEmailFilled && !isEmailFormatValid) {
            editEmail.setError("Formato de e-mail inválido");
        } else {
            editEmail.setError(null);
        }

        return isEmailFilled && isPasswordFilled; // Retorna true se ambos estiverem preenchidos (validação final ocorre no clique)
    }

    /**
     * Chamado pelo android:onClick do botão "Logar".
     * Redireciona para a HomeUser se as credenciais forem válidas.
     */
    public void goToHome(View view) {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        boolean isEmailFormatValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean isPasswordFilled = !password.isEmpty();

        // Verifica a validade final (incluindo formato de e-mail)
        if (isEmailFormatValid && isPasswordFilled) {

            // Simulação de login bem-sucedido (Aqui você faria a chamada ao Firebase/API)

            // Navega para a tela principal
            Intent intent = new Intent(this, HomeUser.class);

            // Limpa o histórico de atividades para que o botão Voltar não retorne para o Login
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

            // Mensagem de sucesso (Opcional)
            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

        } else {
            // Caso raro onde o botão foi clicado, mas a validação falhou
            Toast.makeText(this, "Preencha os campos corretamente.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Redireciona para a tela de Cadastro.
     */
    public void goToRegister (View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    /**
     * Redireciona para a tela de Recuperação de Senha.
     */
    public void goToRetriever (View view) {
        Intent intent = new Intent(this, Retrieve.class);
        startActivity(intent);
    }
}
