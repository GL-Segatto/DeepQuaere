package com.example.deepquaere;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deepquaere.api.ApiService;
import com.example.deepquaere.api.RetrofitClient;
import com.example.deepquaere.model.Projeto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CriarProjeto extends AppCompatActivity {

    private EditText etNomeProjeto;
    private Button btnSalvarProjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_projeto);

        etNomeProjeto = findViewById(R.id.etNomeProjeto);
        btnSalvarProjeto = findViewById(R.id.btnSalvarProjeto);

        btnSalvarProjeto.setOnClickListener(v -> {
            String nomeProjeto = etNomeProjeto.getText().toString().trim();

            if (!nomeProjeto.isEmpty()) {
                enviarNovoProjetoParaApi(nomeProjeto);
            } else {
                Toast.makeText(CriarProjeto.this, "Insira o nome do projeto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarNovoProjetoParaApi(String nome) {
        // Recuperar o token JWT guardado no Login
        SharedPreferences sharedPreferences = getSharedPreferences("DeepQuaerePrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("JWT_TOKEN", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Sessão expirada. Faça login novamente.", Toast.LENGTH_LONG).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();

        // Como o Django exige data_inicio e data_fim obrigatoriamente,
        // geramos a data atual em formato de texto (YYYY-MM-DD) para evitar erros 400.
        String dataAtual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Projeto request = new Projeto();
        request.setNome(nome);
        request.setDataInicio(dataAtual);
        request.setDataFim(dataAtual);

        // Garantir o formato correto do Header com "Bearer"
        String authHeader = token.startsWith("Bearer ") ? token : "Bearer " + token;

        apiService.criarProjeto(authHeader, request).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CriarProjeto.this, "Projeto criado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a tela de criação e volta para o Menu
                } else {
                    String erroMsg = "Erro ao criar projeto: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            erroMsg += " - " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(CriarProjeto.this, erroMsg, Toast.LENGTH_LONG).show();
                    android.util.Log.e("PROJETO_ERRO", erroMsg);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(CriarProjeto.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
                android.util.Log.e("PROJETO_FAIL", "Falha: ", t);
            }
        });
    }
}