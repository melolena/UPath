package com.example.upath;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityTeste extends AppCompatActivity {

    // --- Views da Tela ---
    private RecyclerView recyclerChat;
    private EditText editMessage;
    private Button btnSend;

    // --- Lógica do Chat ---
    private ChatAdapter adapter;
    private List<Message> messageList;
    private ChatService chatService;

    // --- Configuração de Rede ---
    // USE "http://10.0.2.2:3000/" SE ESTIVER NO EMULADOR
    // USE "http://SEU_IP_PC:3000/" SE ESTIVER NO CELULAR FÍSICO (Ex: 192.168.0.10)
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Certifique-se de que seu XML se chama activity_teste ou mude aqui
        setContentView(R.layout.activity_teste);

        // 1. Inicializar os componentes da tela
        recyclerChat = findViewById(R.id.recycler_chat);
        editMessage = findViewById(R.id.edit_message);
        btnSend = findViewById(R.id.btn_send);

        // 2. Configurar a lista de mensagens (RecyclerView)
        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setAdapter(adapter);

        // 3. Configurar o Retrofit (Conexão com o servidor)
        configurarRetrofit(); // <--- A CHAMADA ESTÁ CORRETA AQUI

        // 4. Iniciar conversa automaticamente (Envia "Começar" para o servidor)
        iniciarChatNoServidor();

        // 5. Ação do Botão Enviar
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensagemDigitada = editMessage.getText().toString().trim();
                if (!mensagemDigitada.isEmpty()) {
                    enviarMensagemUsuario(mensagemDigitada);
                }
            }
        });
    }

    // CORREÇÃO FEITA AQUI: O nome deve ser igual ao chamado no onCreate
    private void configurarRetrofit() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            chatService = retrofit.create(ChatService.class);
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao iniciar Retrofit: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void iniciarChatNoServidor() {
        // Envia a palavra chave "Começar" (invisível para o usuário) para resetar o chat no Node
        Call<ChatService.ChatApiResponse> call = chatService.sendMessage(new ChatRequest("Começar"));

        call.enqueue(new Callback<ChatService.ChatApiResponse>() {
            @Override
            public void onResponse(Call<ChatService.ChatApiResponse> call, Response<ChatService.ChatApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // O servidor responde com a 1ª pergunta
                    adicionarMensagemNaTela(response.body().reply, false);
                }
            }

            @Override
            public void onFailure(Call<ChatService.ChatApiResponse> call, Throwable t) {
                adicionarMensagemNaTela("Erro de conexão. Verifique se o server.js está rodando.", false);
                Log.e("ActivityTeste", "Erro API: " + t.getMessage());
            }
        });
    }

    private void enviarMensagemUsuario(String texto) {
        // 1. Mostra a mensagem do usuário na tela imediatamente
        adicionarMensagemNaTela(texto, true);
        editMessage.setText(""); // Limpa o campo

        // 2. Envia para o servidor
        Call<ChatService.ChatApiResponse> call = chatService.sendMessage(new ChatRequest(texto));

        call.enqueue(new Callback<ChatService.ChatApiResponse>() {
            @Override
            public void onResponse(Call<ChatService.ChatApiResponse> call, Response<ChatService.ChatApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String respostaIA = response.body().reply;
                    boolean acabou = response.body().isFinal;

                    // Mostra resposta da IA
                    adicionarMensagemNaTela(respostaIA, false);

                    // Se o servidor disse que acabou (final: true), busca o resultado
                    if (acabou) {
                        buscarResultadoFinal();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatService.ChatApiResponse> call, Throwable t) {
                adicionarMensagemNaTela("Falha ao enviar mensagem.", false);
            }
        });
    }

    private void buscarResultadoFinal() {
        // Chama a rota /resultado do servidor
        chatService.getResult().enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String resultado = response.body().getResultado();
                    // Mostra o resultado final em destaque
                    adicionarMensagemNaTela("🎓 RESULTADO FINAL:\n\n" + resultado, false);

                    // Opcional: Desabilitar o chat pois acabou
                    editMessage.setEnabled(false);
                    btnSend.setEnabled(false);
                    editMessage.setHint("Teste finalizado.");
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                adicionarMensagemNaTela("Erro ao carregar o resultado.", false);
            }
        });
    }

    // Método auxiliar para atualizar a UI sem repetir código
    private void adicionarMensagemNaTela(String texto, boolean enviadoPeloUsuario) {
        messageList.add(new Message(texto, enviadoPeloUsuario));
        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerChat.smoothScrollToPosition(messageList.size() - 1);
    }
}