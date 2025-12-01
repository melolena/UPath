package com.example.upath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHelper {

    public static void setupNavigation(Context context, BottomNavigationView bottomNavigationView, int selectedItemId) {

        // Caso não exista item selecionado (telas como Profile)
        if (selectedItemId == -1) {
            // Desmarca todos para não aparecer nada ativo
            for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                bottomNavigationView.getMenu().getItem(i).setChecked(false);
            }
        } else {
            bottomNavigationView.setSelectedItemId(selectedItemId);
        }

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                // Se clicou no item já ativo
                if (itemId == selectedItemId && selectedItemId != -1) {
                    return true;
                }

                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(context, HomeUser.class);
                    context.startActivity(intent);
                    finishActivity(context);
                    return true;

                } else if (itemId == R.id.nav_test) {
                    Intent intent = new Intent(context, ActivityTeste.class);
                    context.startActivity(intent);
                    finishActivity(context);
                    return true;

                } else if (itemId == R.id.nav_simulation) {
                    Intent intent = new Intent(context, ActivitySimulation.class);
                    context.startActivity(intent);
                    finishActivity(context);
                    return true;
                }

                return false;
            }
        });
    }

    private static void finishActivity(Context context) {
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
