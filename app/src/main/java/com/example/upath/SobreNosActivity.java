package com.example.upath;

import android.os.Bundle;
import android.widget.ImageView; // Importante

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SobreNosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MembroAdapter adapter;
    List<Membro> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nos);

        // Configurar botão de voltar
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        recyclerView = findViewById(R.id.recyclerMembros);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lista = new ArrayList<>();

        // Sua lista de membros (mantenha como estava)
        lista.add(new Membro("Direlly Kaline", "Desenvolvedora Back-end", R.drawable.direlly));
        lista.add(new Membro("Filipe Leonny", "Desenvolvedor Front-end", R.drawable.filipe));
        lista.add(new Membro("Guilherme Felipe", "Analista de Dados (IA)", R.drawable.guilherme));
        lista.add(new Membro("Igor Machado", "Desenvolvedor Back-end", R.drawable.igor));
        lista.add(new Membro("Ingrid Santos", "Desenvolvedora Back-end", R.drawable.ingrid));
        lista.add(new Membro("Jackson Luiz", "Analista de Dados (IA)", R.drawable.jackson));
        lista.add(new Membro("Juliana Gonçalo", "Desenvolvedora Front-end", R.drawable.juliana));
        lista.add(new Membro("Mauri Almeida", "Designer UX/UI", R.drawable.mauri));
        lista.add(new Membro("Milena Melo", "Desenvolvedora Front-end", R.drawable.milena));

        adapter = new MembroAdapter(this, lista);
        recyclerView.setAdapter(adapter);
    }
}