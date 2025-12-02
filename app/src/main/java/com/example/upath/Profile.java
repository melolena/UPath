package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
// import android.widget.Toast; // Não precisa mais do Toast se não for usar

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.signature.ObjectKey;
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

    @Override
    protected void onResume() {
        super.onResume();
        setupHeaderDetail();
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

        String nomeSalvo = prefs.getString("USER_NAME", "Usuário");
        String emailSalvo = prefs.getString("USER_EMAIL", "email@email.com");
        String fotoSalva = prefs.getString("USER_PHOTO", null);

        name.setText(nomeSalvo);
        email.setText(emailSalvo);

        if (fotoSalva != null) {
            Glide.with(this)
                    .load(fotoSalva)
                    .transform(new CircleCrop())
                    .placeholder(R.drawable.user_default_foreground)
                    .error(R.drawable.user_default_foreground)
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .into(profileImg);
        } else {
            profileImg.setImageResource(R.drawable.user_default_foreground);
        }

        edit.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfile.class))
        );

        back.setOnClickListener(v -> finish());
    }

    private void setupProfileOptions() {
        // Configura o visual
        setupOption(R.id.option_sobre_nos, R.drawable.info_circle_foreground, "Sobre Nós");

        // Configura o clique para abrir a tela
        View sobreNos = findViewById(R.id.option_sobre_nos);
        if (sobreNos != null) {
            sobreNos.setOnClickListener(v -> {
                // CORREÇÃO AQUI: Abre a Activity de verdade
                Intent intent = new Intent(Profile.this, SobreNosActivity.class);
                startActivity(intent);
            });
        }
    }

    private void setupOption(int id, int icon, String text) {
        View v = findViewById(id);
        if (v != null) {
            ImageView iconView = v.findViewById(R.id.option_icon);
            TextView titleView = v.findViewById(R.id.option_title);

            if (iconView != null) iconView.setImageResource(icon);
            if (titleView != null) titleView.setText(text);
        }
    }

    private void configurarBottomNav() {
        try {
            View bottomNavInclude = findViewById(R.id.layout_bottom_nav);
            if (bottomNavInclude != null) {
                BottomNavigationView nav = bottomNavInclude.findViewById(R.id.bottom_navigation);
                BottomNavHelper.setupNavigation(this, nav, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}   