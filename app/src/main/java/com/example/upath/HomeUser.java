package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class HomeUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_user);

        // --- 1. CONFIGURAÇÃO DA BOTTOM NAVIGATION ---
        configurarBottomNav();

        // --- 2. CONFIGURA O BOTÃO "INICIAR TESTE" ---
        configurarBotaoTeste();

        // --- 3. AJUSTE DO EDGE TO EDGE ---
        configurarInsets();
    }

    private void configurarBottomNav() {
        try {
            View bottomNavInclude = findViewById(R.id.layout_bottom_nav);
            if (bottomNavInclude != null) {
                BottomNavigationView bottomNavigationView = bottomNavInclude.findViewById(R.id.bottom_navigation);
                if (bottomNavigationView != null) {
                    // Configura a navegação, marcando 'Home' como ativa
                    // Lembre-se: O helper deve estar configurado para aceitar (BottomNavigationView, int)
                    BottomNavHelper.setupNavigation(this, bottomNavigationView, R.id.nav_home);
                }
            }
        } catch (Exception e) {
            android.util.Log.e("HomeUser", "Erro no menu: " + e.getMessage());
        }
    }

    private void configurarBotaoTeste() {
        MaterialButton buttonEscolherTeste = findViewById(R.id.buttonEscolherTeste);
        if (buttonEscolherTeste != null) {
            buttonEscolherTeste.setOnClickListener(v -> {
                Intent intent = new Intent(HomeUser.this, ActivityTeste.class);
                // Flags para limpar a pilha e evitar voltar para a Home com "Voltar"
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Opcional: finish() se não quiser que o usuário volte para a Home ao apertar "Voltar" do celular
                // finish();
            });
        }
    }

    private void configurarInsets() {
        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }
}