package com.example.upath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHelper {

    public static void setupNavigation(Context context, BottomNavigationView bottomNavigationView, int selectedItemId) {

        // Marca o ícone correto como selecionado
        bottomNavigationView.setSelectedItemId(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                // Se clicou no item que já está selecionado, não faz nada
                if (itemId == selectedItemId) {
                    return true;
                }

                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(context, HomeUser.class);
                    context.startActivity(intent);
                    if (context instanceof Activity) ((Activity) context).finish();
                    return true;

                } else if (itemId == R.id.nav_test) {
                    Intent intent = new Intent(context, ActivityTeste.class);
                    context.startActivity(intent);
                    if (context instanceof Activity) ((Activity) context).finish();
                    return true;

                } else if (itemId == R.id.nav_simulation) {
                    // --- MUDANÇA AQUI: Redireciona para a Simulação ---
                    // Troque 'ActivitySimulation.class' pelo nome real da sua classe de simulação
                    Intent intent = new Intent(context, ActivitySimulation.class);
                    context.startActivity(intent);
                    if (context instanceof Activity) ((Activity) context).finish();
                    return true;
                }

                return false;
            }
        });
    }
}