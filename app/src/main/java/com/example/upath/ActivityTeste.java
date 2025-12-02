package com.example.upath;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityTeste extends AppCompatActivity {

    private RecyclerView recyclerChat;
    private TextInputEditText editMessage;
    private Button btnSend;
    private LinearLayout inputContainer;
    private LinearLayout layoutResultado;
    private TextView textResultadoFinal;
    private Button btnRestart;
    private TextView textTitleCard;

    private ChatAdapter adapter;
    private List<Message> messageList;
    private ChatService chatService;

    // Ajuste a porta se necessário (3000, 4000 ou 8001)
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teste);

        ProfileHeader.setup(this);

        // Views
        recyclerChat = findViewById(R.id.recycler_chat);
        editMessage = findViewById(R.id.edit_message);
        btnSend = findViewById(R.id.btn_send);
        inputContainer = findViewById(R.id.input_container);
        layoutResultado = findViewById(R.id.layout_resultado);
        textResultadoFinal = findViewById(R.id.text_resultado_final);
        btnRestart = findViewById(R.id.btn_restart);
        textTitleCard = findViewById(R.id.text_title_card);

        // Lista inicial
        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setAdapter(adapter);

        configurarRetrofit();
        iniciarChatNoServidor();

        // BOTÃO ENVIAR (Lógica para não cancelar acentuação)
        btnSend.setOnClickListener(v -> {
            String textoDigitado = editMessage.getText() != null
                    ? editMessage.getText().toString()
                    : "";

            if (!textoDigitado.trim().isEmpty()) {
                adicionarMensagemNaTela(textoDigitado, true);
                enviarMensagemParaServidor(textoDigitado);

                // Delay pequeno para limpar o campo sem bugar teclado
                editMessage.postDelayed(() -> editMessage.setText(""), 140);
            }
        });

        btnRestart.setOnClickListener(v -> reiniciarTeste());
        configurarBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProfileHeader.setup(this);
    }

    private void configurarBottomNav() {
        try {
            View bottomNavInclude = findViewById(R.id.layout_bottom_nav);
            if (bottomNavInclude != null) {
                BottomNavigationView nav = bottomNavInclude.findViewById(R.id.bottom_navigation);
                BottomNavHelper.setupNavigation(this, nav, R.id.nav_test);
            }
        } catch (Exception ignored) {}
    }

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

    private void configurarRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new com.google.gson.GsonBuilder()
                                        .setLenient()
                                        .disableHtmlEscaping()
                                        .create()
                        )
                )
                .build();

        chatService = retrofit.create(ChatService.class);
    }

    private void iniciarChatNoServidor() {
        chatService.sendMessage(new ChatRequest("Começar"))
                .enqueue(new Callback<ChatService.ChatApiResponse>() {
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

    private void enviarMensagemParaServidor(String texto) {
        chatService.sendMessage(new ChatRequest(texto))
                .enqueue(new Callback<ChatService.ChatApiResponse>() {
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
        chatService.getResult()
                .enqueue(new Callback<ResultResponse>() {
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