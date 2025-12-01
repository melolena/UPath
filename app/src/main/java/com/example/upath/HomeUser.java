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

        // HEADER
        ProfileHeader.setup(this);

        // BOTTOM NAV
        configurarBottomNav();

        // BOTÕES
        configurarBotaoTeste();
        configurarBotaoSimulacao();

        // INSETS
        configurarInsets();
    }

    private void configurarBotaoTeste() {
        MaterialButton btnTeste = findViewById(R.id.buttonEscolherTeste);
        btnTeste.setOnClickListener(v -> {
            Intent i = new Intent(HomeUser.this, ActivityTeste.class);
            startActivity(i);
        });
    }

    private void configurarBotaoSimulacao() {
        MaterialButton btnSimulacao = findViewById(R.id.buttonSimulacao);
        btnSimulacao.setOnClickListener(v -> {
            Intent i = new Intent(HomeUser.this, ActivitySimulation.class);
            startActivity(i);
        });
    }

    private void configurarBottomNav() {
        try {
            View bottomNavInclude = findViewById(R.id.layout_bottom_nav);
            BottomNavigationView nav = bottomNavInclude.findViewById(R.id.bottom_navigation);
            BottomNavHelper.setupNavigation(this, nav, R.id.nav_home);
        } catch (Exception ignored) {}
    }

    private void configurarInsets() {
        View mainLayout = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
