package com.example.upath;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge; // Importante para manter o padrão visual
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityTeste extends AppCompatActivity {

    // --- Views ---
    private RecyclerView recyclerChat;
    private EditText editMessage;
    private Button btnSend;
    private LinearLayout inputContainer;
    private LinearLayout layoutResultado;
    private TextView textResultadoFinal;
    private Button btnRestart;
    private TextView textTitleCard;

    // --- Lógica ---
    private ChatAdapter adapter;
    private List<Message> messageList;
    private ChatService chatService;

    // REDE (Use 10.0.2.2 para Emulador)
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Adicionado para consistência com a Home
        setContentView(R.layout.activity_teste);

        // Inicialização de Views
        recyclerChat = findViewById(R.id.recycler_chat);
        editMessage = findViewById(R.id.edit_message);
        btnSend = findViewById(R.id.btn_send);
        inputContainer = findViewById(R.id.input_container);
        layoutResultado = findViewById(R.id.layout_resultado);
        textResultadoFinal = findViewById(R.id.text_resultado_final);
        btnRestart = findViewById(R.id.btn_restart);
        textTitleCard = findViewById(R.id.text_title_card);

        // Setup Lista
        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setAdapter(adapter);

        // Rede e Chat
        configurarRetrofit();
        iniciarChatNoServidor();

        // Botões
        btnSend.setOnClickListener(v -> {
            String msg = editMessage.getText().toString().trim();
            if (!msg.isEmpty()) enviarMensagemUsuario(msg);
        });

        btnRestart.setOnClickListener(v -> reiniciarTeste());

        // Navegação
        configurarBottomNav();
    }

    private void configurarBottomNav() {
        try {
            View bottomNavInclude = findViewById(R.id.layout_bottom_nav);
            if (bottomNavInclude != null) {
                BottomNavigationView bottomNavigationView = bottomNavInclude.findViewById(R.id.bottom_navigation);
                if (bottomNavigationView != null) {
                    // ATENÇÃO: Se o seu Helper pede 'Context', use 'this'. Se não, remova o 'this'.
                    // Aqui estou assumindo que você atualizou o Helper para aceitar Context (como vimos no exemplo anterior)
                    BottomNavHelper.setupNavigation(this, bottomNavigationView, R.id.nav_test);
                }
            }
        } catch (Exception e) {
            android.util.Log.e("ActivityTeste", "Erro no menu: " + e.getMessage());
        }
    }

    // --- MÉTODOS VISUAIS ---

    private void mostrarTelaResultado(String resultado) {
        recyclerChat.setVisibility(View.GONE);
        inputContainer.setVisibility(View.GONE);
        layoutResultado.setVisibility(View.VISIBLE);
        textResultadoFinal.setText(resultado);
        textTitleCard.setText("Seu Resultado");
    }

    private void reiniciarTeste() {
        messageList.clear();
        adapter.notifyDataSetChanged();
        layoutResultado.setVisibility(View.GONE);
        recyclerChat.setVisibility(View.VISIBLE);
        inputContainer.setVisibility(View.VISIBLE);
        textTitleCard.setText("Orientador Vocacional");
        iniciarChatNoServidor();
    }

    private void adicionarMensagemNaTela(String texto, boolean isUser) {
        messageList.add(new Message(texto, isUser));
        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerChat.smoothScrollToPosition(messageList.size() - 1);
    }

    // --- MÉTODOS DE REDE ---

    private void configurarRetrofit() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            chatService = retrofit.create(ChatService.class);
        } catch (Exception e) {
            Toast.makeText(this, "Erro Retrofit: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void iniciarChatNoServidor() {
        chatService.sendMessage(new ChatRequest("Começar")).enqueue(new Callback<ChatService.ChatApiResponse>() {
            @Override
            public void onResponse(Call<ChatService.ChatApiResponse> call, Response<ChatService.ChatApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adicionarMensagemNaTela(response.body().reply, false);
                }
            }
            @Override
            public void onFailure(Call<ChatService.ChatApiResponse> call, Throwable t) {
                adicionarMensagemNaTela("Erro de conexão.", false);
            }
        });
    }

    private void enviarMensagemUsuario(String texto) {
        adicionarMensagemNaTela(texto, true);
        editMessage.setText("");

        chatService.sendMessage(new ChatRequest(texto)).enqueue(new Callback<ChatService.ChatApiResponse>() {
            @Override
            public void onResponse(Call<ChatService.ChatApiResponse> call, Response<ChatService.ChatApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String resposta = response.body().reply;
                    boolean acabou = response.body().isFinal;
                    adicionarMensagemNaTela(resposta, false);
                    if (acabou) buscarResultadoFinal();
                }
            }
            @Override
            public void onFailure(Call<ChatService.ChatApiResponse> call, Throwable t) {
                adicionarMensagemNaTela("Erro ao enviar.", false);
            }
        });
    }

    private void buscarResultadoFinal() {
        chatService.getResult().enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mostrarTelaResultado(response.body().getResultado());
                }
            }
            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                adicionarMensagemNaTela("Erro ao pegar resultado.", false);
            }
        });
    }
}