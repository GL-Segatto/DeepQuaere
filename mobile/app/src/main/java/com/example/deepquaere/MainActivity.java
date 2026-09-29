package com.example.deepquaere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepquaere.api.ApiService;
import com.example.deepquaere.api.RetrofitClient;
import com.example.deepquaere.model.Projeto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProjetos;
    private ProjetoAdapter projetoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewProjetos = findViewById(R.id.recyclerViewProjetos);
        recyclerViewProjetos.setLayoutManager(new LinearLayoutManager(this));

        carregarProjetos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarProjetos();
    }

    private void carregarProjetos() {
        SharedPreferences sharedPreferences = getSharedPreferences("DeepQuaerePrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("JWT_TOKEN", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Sessão expirada. Faça login novamente.", Toast.LENGTH_LONG).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        String authHeader = token.startsWith("Bearer ") ? token : "Bearer " + token;

        apiService.listarProjetos(authHeader).enqueue(new Callback<List<Projeto>>() {
            @Override
            public void onResponse(Call<List<Projeto>> call, Response<List<Projeto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Projeto> projetos = response.body();

                    projetoAdapter = new ProjetoAdapter(projetos, new ProjetoAdapter.OnProjetoClickListener() {
                        @Override
                        public void onProjetoClick(Projeto projeto) {
                            // Abre os detalhes do projeto enviando o ID correspondente
                            Intent intent = new Intent(MainActivity.this, DetalhesProjetoActivity.class);
                            intent.putExtra("PROJETO_ID", projeto.getId());
                            startActivity(intent);
                        }
                    });

                    recyclerViewProjetos.setAdapter(projetoAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao carregar projetos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Projeto>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
