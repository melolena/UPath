package com.example.upath;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView; // Import necessário
import android.widget.ImageView; // Import necessário

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // --- 1. CONFIGURAÇÃO BASE DE SEGURANÇA E INSETS ---
        setupInsets();
        setupBottomNav();

        // --- 2. CONFIGURAÇÃO DA LISTA DE OPÇÕES (ADICIONADO) ---
        setupProfileOptions();
    }

    // --- MÉTODOS DE SETUP BÁSICOS ---

    private void setupInsets() {
        // Encontra o layout principal para os insets
        View mainLayout = findViewById(R.id.main);

        // Se o layout for encontrado, aplica o listener de insets
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    private void setupBottomNav() {
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

    // --- MÉTODOS DE CONFIGURAÇÃO DA LISTA DE OPÇÕES ---

    private void setupProfileOptions() {
        // 1. RESULTADOS
        setupOption(R.id.option_resultados, R.drawable.ic_clipboard_check_foreground, R.string.resultado);

        // 2. HISTÓRICO
        setupOption(R.id.option_historico, R.drawable.ic_clock_foreground, R.string.historico);

        // 3. SALVOS
        setupOption(R.id.option_salvos, R.drawable.save_foreground, R.string.salvos);

        // 4. PLANOS
        setupOption(R.id.option_planos, R.drawable.star_foreground, R.string.planos);

        // 5. SOBRE NÓS
        setupOption(R.id.option_sobre_nos, R.drawable.info_circle_foreground, R.string.sobre_nos);
    }

    /**
     * Encontra o item de include, busca seus componentes e define o texto e ícone.
     * @param includeId ID do include no layout principal (e.g., R.id.option_historico).
     * @param iconResId ID do drawable do ícone (e.g., R.drawable.ic_history_time_foreground).
     * @param textResId ID da string do texto (e.g., R.string.historico).
     */
    private void setupOption(int includeId, int iconResId, int textResId) {
        // Encontra o item inteiro (CardView)
        View optionView = findViewById(includeId);

        if (optionView != null) {
            // Encontra o TextView e o ImageView DENTRO do item_profile_option.xml
            TextView titleView = optionView.findViewById(R.id.option_title);
            ImageView iconView = optionView.findViewById(R.id.option_icon);

            // Define o texto e o ícone
            if (titleView != null) {
                titleView.setText(textResId);
            }
            if (iconView != null) {
                iconView.setImageResource(iconResId);
            }
        }
    }
}