package com.example.upath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.signature.ObjectKey; // IMPORTANTE

public class ProfileHeader {

    public static void setup(AppCompatActivity activity) {

        // Encontra o include do header
        View header = activity.findViewById(R.id.profile_header);
        if (header == null) return;

        // Views internas
        CardView cardProfile = header.findViewById(R.id.cardProfile);
        ImageView profileImage = header.findViewById(R.id.profileImage);
        TextView greetingTitle = header.findViewById(R.id.greetingTitle);
        TextView greetingSubtitle = header.findViewById(R.id.greetingSubtitle);

        // Recupera dados salvos
        SharedPreferences prefs = activity.getSharedPreferences("UPATH_PREFS", AppCompatActivity.MODE_PRIVATE);

        String nome = prefs.getString("USER_NAME", "Estudante");
        String foto = prefs.getString("USER_PHOTO", null);

        // Define textos
        greetingTitle.setText("Olá, " + nome + "!");
        greetingSubtitle.setText("O que vai ser hoje?");

        // Carrega a foto com Glide
        if (foto != null && !foto.isEmpty()) {
            Glide.with(activity)
                    .load(foto)
                    .transform(new CircleCrop())
                    .placeholder(R.drawable.user_default_foreground)
                    .error(R.drawable.user_default_foreground)
                    // --- O SEGREDO ESTÁ AQUI EMBAIXO ---
                    // Usa o tempo atual como assinatura. Como o tempo sempre muda,
                    // o Glide entende que precisa recarregar a imagem do zero.
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.user_default_foreground);
        }

        // Ação de clique -> abre tela de Perfil
        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Profile.class);
            activity.startActivity(intent);
        });
    }
}