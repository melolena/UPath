package com.example.upath;

import android.os.Build;
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

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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

    // --- MAPA DE CONVERSÃO ---
    private Map<String, String> mapaCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_simulation);

        ajustarPadding();
        ProfileHeader.setup(this);

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

        inicializarMapaCursos();
        configurarRetrofit();
        configurarSpinner();
        configurarBottomNav();

        btnSimular.setOnClickListener(v -> realizarSimulacao());
        btnNovaSimulacao.setOnClickListener(v -> resetarTela());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProfileHeader.setup(this);
    }

    // --- AQUI ESTÁ A CORREÇÃO PRINCIPAL ---
    // Esquerda: O que aparece no Spinner (Bonito)
    // Direita: O que vai pro Python (Técnico, MAIÚSCULO, SEM ACENTO)
    private void inicializarMapaCursos() {
        mapaCursos = new HashMap<>();
        mapaCursos.put("Administração", "ADMINISTRACAO");
        mapaCursos.put("Ciência da Computação", "CIENCIA DA COMPUTACAO");
        mapaCursos.put("Direito", "DIREITO");
        mapaCursos.put("Enfermagem", "ENFERMAGEM");
        mapaCursos.put("Fisioterapia", "FISIOTERAPIA");
        mapaCursos.put("Medicina", "MEDICINA");
        mapaCursos.put("Psicologia", "PSICOLOGIA");
        mapaCursos.put("Sistemas de Informação", "SISTEMAS DE INFORMACAO");
    }

    private String getBaseUrl() {
        // Se estiver no Emulador, use 10.0.2.2.
        // Se estiver no celular físico via USB, troque pelo IP do seu PC (ex: "http://192.168.1.15:4000/")
        return "http://10.0.2.2:4000/";
    }

    private void configurarRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        chatService = retrofit.create(ChatService.class);
    }

    private void configurarSpinner() {
        // Estes nomes devem ser IDÊNTICOS às chaves do mapaCursos (lado esquerdo)
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
                R.layout.item_spinner_branco,
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

            // Pega o nome visual (Ex: "Ciência da Computação")
            String nomeVisual = spinnerCursos.getSelectedItem().toString();

            // Converte para o técnico (Ex: "CIENCIA DA COMPUTACAO")
            String nomeTecnico = mapaCursos.get(nomeVisual);

            // Segurança: Se não achou no mapa, avisa e para
            if (nomeTecnico == null) {
                Toast.makeText(this, "Erro: Curso não reconhecido pelo sistema.", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSimular.setText("Calculando...");
            btnSimular.setEnabled(false);

            // Envia o nome TÉCNICO para o backend
            SimulationRequest request = new SimulationRequest(nomeTecnico, nota);

            chatService.realizarSimulacao(request).enqueue(new Callback<SimulationResponse>() {
                @Override
                public void onResponse(Call<SimulationResponse> call, Response<SimulationResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        SimulationResponse body = response.body();

                        if (body.erro != null && !body.erro.isEmpty()) {
                            Toast.makeText(ActivitySimulation.this, "IA: " + body.erro, Toast.LENGTH_LONG).show();
                            restaurarBotoes();
                            return;
                        }

                        mostrarResultado(nomeVisual, body);

                    } else {
                        Toast.makeText(ActivitySimulation.this, "Erro no servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                        restaurarBotoes();
                    }
                }

                @Override
                public void onFailure(Call<SimulationResponse> call, Throwable t) {
                    Toast.makeText(ActivitySimulation.this, "Falha de conexão. Verifique se o servidor está rodando.", Toast.LENGTH_LONG).show();
                    restaurarBotoes();
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Nota inválida", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarResultado(String cursoVisual, SimulationResponse respostaIA) {
        textCursoEscolhido.setText(cursoVisual);
        textResultadoFinal.setText(respostaIA.resultado);
        textMsgDetalhe.setText(respostaIA.mensagem);

        if (respostaIA.aprovado) {
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
                BottomNavigationView nav = bottomNavInclude.findViewById(R.id.bottom_navigation);
                BottomNavHelper.setupNavigation(this, nav, R.id.nav_simulation);
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