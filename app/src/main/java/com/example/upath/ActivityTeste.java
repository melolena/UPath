package com.example.upath;

import android.os.Bundle;
import android.view.View; // Importar View
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityTeste extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teste);

        // Encontra o layout principal (o ConstraintLayout com id="@id/main")
        View mainLayout = findViewById(R.id.main);

        // --- 1. AJUSTE DE INSETS/SYSTEM BARS (EdgeToEdge) ---
        // Este bloco deve sempre vir primeiro.
        if (mainLayout != null) { // Adicionar checagem de nulo para evitar crash
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }


        // --- 2. CONFIGURAÇÃO DA BOTTOM NAVIGATION ---

        // Encontra o contêiner (include) e, em seguida, a barra de navegação.
        // Adicionamos checagem de nulo para evitar crash caso um ID esteja errado.
        View bottomNavInclude = findViewById(R.id.layout_bottom_nav);

        if (bottomNavInclude != null) {
            BottomNavigationView bottomNavigationView = bottomNavInclude.findViewById(R.id.bottom_navigation);

            if (bottomNavigationView != null) {
                // Configura a navegação e marca o item 'Test' como ativo.
                com.example.upath.BottomNavHelper.setupNavigation(bottomNavigationView, R.id.nav_test);
            }
        }
    }
}