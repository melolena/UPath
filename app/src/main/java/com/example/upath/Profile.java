package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        setupInsets();
        setupHeaderDetail();
        setupProfileOptions();
        configurarBottomNav();
    }

    private void setupInsets() {
        View main = findViewById(R.id.main);
        if (main != null) {
            ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    private void setupHeaderDetail() {

        View header = findViewById(R.id.profile_detail_header);
        if (header == null) return;

        ImageView profileImg = header.findViewById(R.id.profile_image);
        TextView name = header.findViewById(R.id.text_user_name);
        TextView email = header.findViewById(R.id.text_user_email);

        ImageView edit = header.findViewById(R.id.icon_edit);
        ImageView back = header.findViewById(R.id.icon_navigate_profile);

        var prefs = getSharedPreferences("UPATH_PREFS", MODE_PRIVATE);

        String nome = prefs.getString("USER_NAME", "Usuário");
        String foto = prefs.getString("USER_PHOTO", null);
        String emailUser = prefs.getString("USER_EMAIL", "email@email.com");

        name.setText(nome);
        email.setText(emailUser);

        if (foto != null) {
            Glide.with(this).load(foto).into(profileImg);
        }

        edit.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfile.class))
        );

        back.setOnClickListener(v -> finish());
    }

    private void setupProfileOptions() {
        setupOption(R.id.option_resultados, R.drawable.ic_clipboard_check_foreground, R.string.resultado);
        setupOption(R.id.option_historico, R.drawable.ic_clock_foreground, R.string.historico);
        setupOption(R.id.option_salvos, R.drawable.save_foreground, R.string.salvos);
        setupOption(R.id.option_planos, R.drawable.star_foreground, R.string.planos);
        setupOption(R.id.option_sobre_nos, R.drawable.info_circle_foreground, R.string.sobre_nos);
    }

    private void setupOption(int id, int icon, int textId) {
        View v = findViewById(id);
        if (v != null) {
            ((ImageView) v.findViewById(R.id.option_icon)).setImageResource(icon);
            ((TextView) v.findViewById(R.id.option_title)).setText(textId);
        }
    }

    private void configurarBottomNav() {
        try {
            View bottomNavInclude = findViewById(R.id.layout_bottom_nav);
            if (bottomNavInclude != null) {
                BottomNavigationView nav = bottomNavInclude.findViewById(R.id.bottom_navigation);

                // Nenhum item deve ficar ativo
                BottomNavHelper.setupNavigation(this, nav, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
