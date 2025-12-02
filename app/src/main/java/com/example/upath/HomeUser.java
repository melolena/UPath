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

        // Configuração inicial do Header
        ProfileHeader.setup(this);

        configurarBottomNav();
        configurarBotaoTeste();
        configurarBotaoSimulacao();
        configurarInsets();
    }

    // Garante que o Header atualize se você voltar da tela de edição de perfil
    @Override
    protected void onResume() {
        super.onResume();
        ProfileHeader.setup(this);
    }

    private void configurarBotaoTeste() {
        MaterialButton btnTeste = findViewById(R.id.buttonEscolherTeste);
        if (btnTeste != null) {
            btnTeste.setOnClickListener(v -> {
                Intent i = new Intent(HomeUser.this, ActivityTeste.class);
                startActivity(i);
            });
        }
    }

    private void configurarBotaoSimulacao() {
        MaterialButton btnSimulacao = findViewById(R.id.buttonSimulacao);
        if (btnSimulacao != null) {
            btnSimulacao.setOnClickListener(v -> {
                Intent i = new Intent(HomeUser.this, ActivitySimulation.class);
                startActivity(i);
            });
        }
    }

    private void configurarBottomNav() {
        try {
            View bottomNavInclude = findViewById(R.id.layout_bottom_nav);
            if (bottomNavInclude != null) {
                BottomNavigationView nav = bottomNavInclude.findViewById(R.id.bottom_navigation);
                BottomNavHelper.setupNavigation(this, nav, R.id.nav_home);
            }
        } catch (Exception e) {
            e.printStackTrace();
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