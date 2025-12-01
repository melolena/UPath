package com.example.upath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail;
    private TextInputEditText editPassword;
    private Button buttonLogin;

    // URL DO BACKEND
    private static final String BASE_URL = "http://10.0.2.2:8001/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mapeamento das Views
        editEmail = findViewById(R.id.edit_email);
        TextInputLayout inputPasswordLayout = findViewById(R.id.input_password_layout);
        editPassword = findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.botao_logar);

        // Desativa o botão até os campos terem valores válidos
        buttonLogin.setEnabled(false);

        // Validação de campos
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { checkFields(); }
            @Override public void afterTextChanged(Editable s) {}
        };

        editEmail.addTextChangedListener(watcher);
        editPassword.addTextChangedListener(watcher);
    }

    private void checkFields() {
        String email = editEmail.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();

        boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean filled = !email.isEmpty() && !pass.isEmpty();

        buttonLogin.setEnabled(validEmail && filled);

        if (!email.isEmpty() && !validEmail) {
            editEmail.setError("E-mail inválido");
        } else {
            editEmail.setError(null);
        }
    }

    // LOGIN
    public void goToHome(View view) {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Feedback visual
        buttonLogin.setEnabled(false);
        buttonLogin.setText("Entrando...");

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthService service = retrofit.create(AuthService.class);
        LoginRequest loginData = new LoginRequest(email, password);

        service.fazerLogin(loginData).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                buttonLogin.setEnabled(true);
                buttonLogin.setText("Logar");

                if (response.isSuccessful() && response.body() != null) {

                    String token = response.body().getAccessToken();
                    String nome = response.body().getUser().getNome();
                    String emailUser = response.body().getUser().getEmail();

                    Toast.makeText(MainActivity.this, "Bem-vindo, " + nome + "!", Toast.LENGTH_SHORT).show();

                    // 🔥 SALVA DADOS DO USUÁRIO (A PARTE MAIS IMPORTANTE!)
                    SharedPreferences prefs = getSharedPreferences("UPATH_PREFS", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString("ACCESS_TOKEN", token);
                    editor.putString("USER_NAME", nome);
                    editor.putString("USER_EMAIL", emailUser);

                    // FUTURAMENTE SE TIVER FOTO:
                    // editor.putString("USER_PHOTO", response.body().getUser().getFoto());

                    editor.apply();

                    // Abre a HomeUser
                    Intent intent = new Intent(MainActivity.this, HomeUser.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(MainActivity.this, "E-mail ou senha incorretos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                buttonLogin.setEnabled(true);
                buttonLogin.setText("Logar");

                Toast.makeText(MainActivity.this, "Erro de conexão com o servidor!", Toast.LENGTH_LONG).show();
                Log.e("LOGIN_ERROR", "Erro: " + t.getMessage());
            }
        });
    }

    public void goToRegister(View view) {
        startActivity(new Intent(this, Register.class));
    }

    public void goToRetriever(View view) {
        startActivity(new Intent(this, Retrieve.class));
    }
}
