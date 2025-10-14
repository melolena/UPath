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

        // --- 1. CONFIGURA A BOTTOM NAVIGATION ---
        // Encontra a BottomNavigationView através dos includes
        BottomNavigationView bottomNavigationView =
                findViewById(R.id.layout_bottom_nav).findViewById(R.id.bottom_navigation);

        // Configura a barra de navegação inferior, marcando 'Home' como ativa
        com.example.upath.BottomNavHelper.setupNavigation(bottomNavigationView, R.id.nav_home);

        // --- 2. CONFIGURA O BOTÃO "INICIAR TESTE" ---

        // Localiza o botão pelo ID
        MaterialButton buttonEscolherTeste = findViewById(R.id.buttonEscolherTeste);

        // Adiciona o listener de clique para navegar para a ActivityTeste
        buttonEscolherTeste.setOnClickListener(v -> {
            Intent intent = new Intent(HomeUser.this, ActivityTeste.class);

            // Flags essenciais para evitar problemas de cache e pilha
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish(); // Finaliza a HomeUser
        });

        // --- 3. AJUSTE DO EDGE TO EDGE ---
        // Aplica o padding para evitar que o conteúdo fique atrás das barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Nota: O método público 'goToTeste(View view)' foi removido para evitar conflito com o OnClickListener.
}