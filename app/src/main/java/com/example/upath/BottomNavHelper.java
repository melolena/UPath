package com.example.upath;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHelper {

    public static void setupNavigation(BottomNavigationView navigationView, int currentItemId) {

        // Seleciona o item atual visualmente
        navigationView.setSelectedItemId(currentItemId);

        navigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                Context context = navigationView.getContext();

                // Evita recarregar a Activity atual
                if (itemId == currentItemId) return true;

                Class<?> destinationActivity = null;

                // Substituindo switch-case por if-else
                if (itemId == R.id.nav_home) {
                    destinationActivity = HomeUser.class;
                } else if (itemId == R.id.nav_test) {
                    destinationActivity = ActivityTeste.class;
                } else if (itemId == R.id.nav_profile) {
                    destinationActivity = Profile.class;
                }

                if (destinationActivity != null) {
                    Intent intent = new Intent(context, destinationActivity);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);

                    if (context instanceof AppCompatActivity) {
                        AppCompatActivity currentActivity = (AppCompatActivity) context;
                        if (destinationActivity != currentActivity.getClass()) {
                            currentActivity.finish();
                        }
                    }
                    return true;
                }

                return false;
            }
        });
    }
}
