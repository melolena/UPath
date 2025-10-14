package com.example.upath;

import android.os.Bundle;
import android.view.View; // Importar View
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView; // Adicionar import

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Encontra o layout principal para os insets
        View mainLayout = findViewById(R.id.main);

        // --- 1. AJUSTE DE INSETS (COM CHECAGEM DE NULO) ---
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // --- 2. CONFIGURAÇÃO DA BOTTOM NAVIGATION (COM CHECAGEM DE NULO) ---

        // Encontra o contêiner (include)
        View bottomNavInclude = findViewById(R.id.layout_bottom_nav);

        if (bottomNavInclude != null) {
            // Encontra a barra de navegação dentro do include
            BottomNavigationView bottomNavigationView = bottomNavInclude.findViewById(R.id.bottom_navigation);

            if (bottomNavigationView != null) {
                // Configura a navegação, marcando 'Profile' como ativo.
                com.example.upath.BottomNavHelper.setupNavigation(bottomNavigationView, R.id.nav_profile);
            }
        }
    }
}