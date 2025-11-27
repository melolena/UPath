package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

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


        // --- 2. CONFIGURAÇÃO DA LISTA DE OPÇÕES ---
        setupProfileOptions();

        // --- 3. CONFIGURAÇÃO DOS CLIQUES DO HEADER ---
        setupProfileNavigationIcon(); // Seta ao lado do email (leva para MainActivity)
        setupEditIconClickListener(); // Ícone de lápis (leva para EditProfile)
    }

    // --- MÉTODOS DE SETUP BÁSICOS ---

    private void setupInsets() {
        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }


    // --- MÉTODOS DE NAVEGAÇÃO DO HEADER ---

    // CONFIGURAÇÃO DO CLIQUE NO ÍCONE DE EDIÇÃO (CORREÇÃO ADICIONADA)
    private void setupEditIconClickListener() {
        View headerDetailInclude = findViewById(R.id.profile_detail_header);

        if (headerDetailInclude != null) {
            ImageView editIcon = headerDetailInclude.findViewById(R.id.icon_edit);

            if (editIcon != null) {
                editIcon.setOnClickListener(v -> {
                    // Inicia a Activity EditProfile
                    Intent intent = new Intent(Profile.this, EditProfile.class);
                    startActivity(intent);
                });
            }
        }
    }

    // CONFIGURAÇÃO DO CLIQUE NA SETA DE NAVEGAÇÃO (JÁ EXISTENTE)
    private void setupProfileNavigationIcon() {
        View headerDetailInclude = findViewById(R.id.profile_detail_header);

        if (headerDetailInclude != null) {
            ImageView navigateIcon = headerDetailInclude.findViewById(R.id.icon_navigate_profile);

            if (navigateIcon != null) {
                navigateIcon.setOnClickListener(v -> {
                    // Inicia a MainActivity
                    Intent intent = new Intent(Profile.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
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

    private void setupOption(int includeId, int iconResId, int textResId) {
        View optionView = findViewById(includeId);

        if (optionView != null) {
            TextView titleView = optionView.findViewById(R.id.option_title);
            ImageView iconView = optionView.findViewById(R.id.option_icon);

            if (titleView != null) {
                titleView.setText(textResId);
            }
            if (iconView != null) {
                iconView.setImageResource(iconResId);
            }
        }
    }
}