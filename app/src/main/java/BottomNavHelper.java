package com.example.upath;

import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Esta classe gerencia toda a navegação da BottomNavigationView
public class BottomNavHelper {

    /**
     * Configura o listener de navegação para a barra inferior.
     * Deve ser chamado no onCreate de cada Activity que usa a BottomNavigationView.
     *
     * @param navigationView A instância do BottomNavigationView.
     * @param currentItemId O ID do item de menu correspondente à Activity atual (R.id.nav_home, R.id.nav_test, etc.).
     */
    public static void setupNavigation(BottomNavigationView navigationView, int currentItemId) {

        // Garante que o item de menu da Activity atual esteja selecionado visualmente
        navigationView.setSelectedItemId(currentItemId);

        // Configura o listener que detecta o clique nos ícones
        navigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                AppCompatActivity currentActivity = (AppCompatActivity) navigationView.getContext();

                // Evita recarregar a Activity se o item clicado for a página atual
                if (itemId == currentItemId) {
                    return true;
                }

                // Define a Activity de destino
                Class<?> destinationActivity = null;

                if (itemId == R.id.nav_home) {
                    destinationActivity = HomeUser.class;
                } else if (itemId == R.id.nav_test) {
                    // Clicou no ícone de Teste
                    destinationActivity = ActivityTeste.class;
                } else if (itemId == R.id.nav_profile) {
                    // Clicou no ícone de Perfil
                    destinationActivity = Profile.class;
                }

                // Inicia a navegação, se o destino for válido
                if (destinationActivity != null) {
                    Intent intent = new Intent(currentActivity, destinationActivity);

                    // Adiciona flags para limpar o histórico de navegação e evitar pilhas profundas (UX)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    currentActivity.startActivity(intent);

                    // Adicional: Finaliza a Activity atual apenas se a nova for diferente.
                    if (destinationActivity != currentActivity.getClass()) {
                        currentActivity.finish();
                    }
                    return true;
                }

                return false;
            }
        });
    }
}
