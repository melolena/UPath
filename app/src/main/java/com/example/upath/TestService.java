package com.example.upath;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TestService {

    // 1. Listar os testes disponíveis
    @GET("/api/v1/tests/")
    Call<ApiResponse<List<TestItem>>> listarTestes(@Header("Authorization") String token);

    // 2. Começar um teste (Retorna a 1ª pergunta)
    @POST("/api/v1/tests/{id}/start")
    Call<ApiResponse<TestSession>> iniciarTeste(@Header("Authorization") String token, @Path("id") int testeId);

    // 3. Responder uma pergunta (Retorna a próxima pergunta)
    @POST("/api/v1/tests/{test_id}/question-response")
    Call<ApiResponse<NextQuestion>> responderPergunta(
            @Header("Authorization") String token,
            @Path("test_id") int testeId, // ID fake da sessão (123 no seu python)
            @Body AnswerRequest resposta
    );

    // 4. Finalizar teste (Recebe o resultado final)
    @POST("/api/v1/tests/{test_id}/finish")
    Call<ApiResponse<TestResult>> finalizarTeste(
            @Header("Authorization") String token,
            @Path("test_id") int testeId,
            @Body FinishRequest body
    );

    // 5. Histórico
    @GET("/api/v1/tests/history")
    Call<ApiResponse<List<Object>>> verHistorico(@Header("Authorization") String token);


    // ==========================================
    // CLASSES DE MODELO
    // ==========================================

    class ApiResponse<T> {
        public boolean success;
        public T data;
    }

    class TestItem {
        public int id;
        public String nome;
    }

    class TestSession {
        public int test_id;
        public String pergunta;
    }

    class AnswerRequest {
        public String resposta;
        public AnswerRequest(String r) { this.resposta = r; }
    }

    class NextQuestion {
        public String proxima_pergunta;
    }

    class FinishRequest {
        public List<String> respostas; // Lista de todas as respostas
    }

    class TestResult {
        // Ajuste estes campos conforme o que a IA retorna de verdade no final
        public String resultado;
        public String descricao;
    }
}