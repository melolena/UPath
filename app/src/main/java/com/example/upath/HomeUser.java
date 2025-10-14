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

        // --- 1. CONFIGURAÇÃO SEGURA DA BOTTOM NAVIGATION ---

        // 1a. Encontra o contêiner (o include)
        View bottomNavInclude = findViewById(R.id.layout_bottom_nav);

        if (bottomNavInclude != null) {
            // 1b. Encontra a BottomNavigationView dentro do contêiner
            BottomNavigationView bottomNavigationView = bottomNavInclude.findViewById(R.id.bottom_navigation);

            if (bottomNavigationView != null) {
                // 1c. Configura a navegação, marcando 'Home' como ativa
                com.example.upath.BottomNavHelper.setupNavigation(bottomNavigationView, R.id.nav_home);
            }
        }

        // --- 2. CONFIGURA O BOTÃO "INICIAR TESTE" ---

        MaterialButton buttonEscolherTeste = findViewById(R.id.buttonEscolherTeste);

        if (buttonEscolherTeste != null) {
            buttonEscolherTeste.setOnClickListener(v -> {
                Intent intent = new Intent(HomeUser.this, ActivityTeste.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        // --- 3. AJUSTE DO EDGE TO EDGE ---
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