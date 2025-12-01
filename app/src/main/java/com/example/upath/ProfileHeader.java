package com.example.upath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

// Importante: Usar AppCompatActivity para compatibilidade com Glide e novos Androids
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

public class ProfileHeader {

    public static void setup(AppCompatActivity activity) {

        // 1. Encontra o layout do cabeçalho na tela
        View header = activity.findViewById(R.id.profile_header);
        if (header == null) return; // Se não achar o header, para aqui para não travar

        // 2. Encontra os componentes dentro do header
        CardView cardProfile = header.findViewById(R.id.cardProfile);
        ImageView profileImage = header.findViewById(R.id.profileImage);
        TextView greetingText = header.findViewById(R.id.greetingText);

        // 3. Carrega os dados salvos (Nome e Foto)
        SharedPreferences prefs = activity.getSharedPreferences("UPATH_PREFS", AppCompatActivity.MODE_PRIVATE);
        String nome = prefs.getString("USER_NAME", "Estudante");
        String foto = prefs.getString("USER_PHOTO", null);

        greetingText.setText("Olá, " + nome + "!");

        if (foto != null) {
            Glide.with(activity).load(foto).into(profileImage);
        }

        // 4. AÇÃO DE CLIQUE (AQUI ESTÁ O QUE VOCÊ PEDIU)
        // Quando clicar no Card da foto -> Vai para a Activity Profile
        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Profile.class);
            activity.startActivity(intent);
        });

    }
}