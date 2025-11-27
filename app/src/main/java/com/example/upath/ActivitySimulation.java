package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivitySimulation extends AppCompatActivity {

    // --- VIEWS ---
    private CardView cardInput;
    private Spinner spinnerCursos;
    private EditText editNota;
    private Button btnSimular;

    private LinearLayout layoutResultado;
    private TextView textCursoEscolhido;
    private TextView textResultadoFinal;
    private TextView textMsgDetalhe;
    private Button btnNovaSimulacao;

    // --- REDE ---
    private ChatService chatService;
    // IMPORTANTE: Se usar emulador use 10.0.2.2. Se usar celular físico, use o IP do PC (ex: 192.168.1.X)
    private static final String BASE_URL = "http://10.0.2.2:4000/";

    // --- MAPEAMENTO (O Segredo para a Ortografia Correta) ---
    private Map<String, String> mapaCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_simulation);

        ajustarPadding();

        // Inicializa Views
        cardInput = findViewById(R.id.card_input_simulacao);
        spinnerCursos = findViewById(R.id.spinner_cursos);
        editNota = findViewById(R.id.edit_nota);
        btnSimular = findViewById(R.id.btn_simular);

        layoutResultado = findViewById(R.id.layout_resultado_simulacao);
        textCursoEscolhido = findViewById(R.id.text_curso_escolhido);
        textResultadoFinal = findViewById(R.id.text_resultado_final);
        textMsgDetalhe = findViewById(R.id.text_msg_detalhe);
        btnNovaSimulacao = findViewById(R.id.btn_nova_simulacao);

        // Inicializa Mapeamento
        inicializarMapaCursos();

        // Configurações
        configurarRetrofit();
        configurarSpinner();
        configurarBottomNav();

        // Ações
        btnSimular.setOnClickListener(v -> realizarSimulacao());
        btnNovaSimulacao.setOnClickListener(v -> resetarTela());
    }

    private void inicializarMapaCursos() {
        mapaCursos = new HashMap<>();
        // CHAVE (O que aparece na tela) -> VALOR (O que a IA conhece)
        mapaCursos.put("Administração", "ADMINISTRACAO");
        mapaCursos.put("Ciência da Computação", "CIENCIA DA COMPUTACAO");
        mapaCursos.put("Direito", "DIREITO");
        mapaCursos.put("Enfermagem", "ENFERMAGEM");
        mapaCursos.put("Fisioterapia", "FISIOTERAPIA");
        mapaCursos.put("Medicina", "MEDICINA");
        mapaCursos.put("Psicologia", "PSICOLOGIA");
        mapaCursos.put("Sistemas de Informação", "SISTEMAS DE INFORMACAO");
    }

    private void configurarRetrofit() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            chatService = retrofit.create(ChatService.class);
        } catch (Exception e) {
            Toast.makeText(this, "Erro Config: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void configurarSpinner() {
        // A lista visual agora tem acentos e ortografia correta
        String[] cursosVisuais = {
                "Selecione...",
                "Administração",
                "Ciência da Computação",
                "Direito",
                "Enfermagem",
                "Fisioterapia",
                "Medicina",
                "Psicologia",
                "Sistemas de Informação"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_spinner_branco, // Seu XML com texto branco
                cursosVisuais
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCursos.setAdapter(adapter);
    }

    private void realizarSimulacao() {
        String notaStr = editNota.getText().toString().trim();
        int posicaoCurso = spinnerCursos.getSelectedItemPosition();

        if (posicaoCurso == 0) {
            Toast.makeText(this, "Selecione um curso", Toast.LENGTH_SHORT).show();
            return;
        }
        if (notaStr.isEmpty()) {
            editNota.setError("Digite sua nota");
            return;
        }

        try {
            double nota = Double.parseDouble(notaStr);

            // 1. Pega o nome BONITO da tela (ex: "Ciência da Computação")
            String nomeVisual = spinnerCursos.getSelectedItem().toString();

            // 2. Converte para o nome TÉCNICO da IA (ex: "CIENCIA DA COMPUTACAO")
            String nomeTecnico = mapaCursos.get(nomeVisual);

            if (nomeTecnico == null) {
                Toast.makeText(this, "Erro interno: Curso não mapeado", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSimular.setText("Calculando...");
            btnSimular.setEnabled(false);

            // Envia o NOME TÉCNICO para o servidor
            SimulationRequest request = new SimulationRequest(nomeTecnico, nota);

            chatService.realizarSimulacao(request).enqueue(new Callback<SimulationResponse>() {
                @Override
                public void onResponse(Call<SimulationResponse> call, Response<SimulationResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Passamos o 'nomeVisual' para exibir na tela de resultado (fica mais bonito)
                        mostrarResultado(nomeVisual, response.body());
                    } else {
                        Toast.makeText(ActivitySimulation.this, "Erro servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                        restaurarBotoes();
                    }
                }

                @Override
                public void onFailure(Call<SimulationResponse> call, Throwable t) {
                    Toast.makeText(ActivitySimulation.this, "Falha de conexão.", Toast.LENGTH_LONG).show();
                    restaurarBotoes();
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Nota inválida", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarResultado(String cursoVisual, SimulationResponse respostaIA) {
        textCursoEscolhido.setText(cursoVisual); // Exibe com acento
        textResultadoFinal.setText(respostaIA.getResultado());
        textMsgDetalhe.setText(respostaIA.getMensagem());

        if (respostaIA.isAprovado()) {
            textResultadoFinal.setTextColor(0xFF10B981); // Verde
        } else {
            textResultadoFinal.setTextColor(0xFFEF4444); // Vermelho
        }

        cardInput.setVisibility(View.GONE);
        layoutResultado.setVisibility(View.VISIBLE);
        restaurarBotoes();
    }

    private void restaurarBotoes() {
        btnSimular.setText("Simular Agora");
        btnSimular.setEnabled(true);
    }

    private void resetarTela() {
        editNota.setText("");
        spinnerCursos.setSelection(0);
        layoutResultado.setVisibility(View.GONE);
        cardInput.setVisibility(View.VISIBLE);
    }

    private void configurarBottomNav() {
        try {
            View bottomNavInclude = findViewById(R.id.layout_bottom_nav);
            if (bottomNavInclude != null) {
                BottomNavigationView bottomNavigationView = bottomNavInclude.findViewById(R.id.bottom_navigation);
                if (bottomNavigationView != null) {
                    BottomNavHelper.setupNavigation(this, bottomNavigationView, R.id.nav_simulation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ajustarPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}