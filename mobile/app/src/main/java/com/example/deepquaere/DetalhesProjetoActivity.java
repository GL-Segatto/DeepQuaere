package com.example.deepquaere;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepquaere.api.ApiService;
import com.example.deepquaere.api.RetrofitClient;
import com.example.deepquaere.model.ItemOrcamento;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesProjetoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewItens;
    private ItemOrcamentoAdapter itemOrcamentoAdapter;
    private TextView tvTotalOrcamento;
    private int projetoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_projeto);

        // 1. Recuperar o ID do projeto enviado pela Intent
        projetoId = getIntent().getIntExtra("PROJETO_ID", -1);

        if (projetoId == -1) {
            Toast.makeText(this, "Erro: ID do projeto inválido.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Inicializar componentes de UI
        recyclerViewItens = findViewById(R.id.recyclerViewItensOrcamento);
        if (recyclerViewItens != null) {
            recyclerViewItens.setLayoutManager(new LinearLayoutManager(this));
        }

        tvTotalOrcamento = findViewById(R.id.tvTotalOrcamento);

        // 3. Inicializar botão de voltar
        Button btnVoltar = findViewById(R.id.btnVoltar);
        if (btnVoltar != null) {
            btnVoltar.setOnClickListener(v -> finish());
        }

        // 4. Configurar o Botão de Adicionar Item
        Button btnAdicionarItem = findViewById(R.id.btnAdicionarItem);
        if (btnAdicionarItem != null) {
            btnAdicionarItem.setOnClickListener(v -> mostrarDialogoAdicionarItem());
        }

        // 5. Carregar os dados da API
        carregarItensOrcamento();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarItensOrcamento();
    }

    private void carregarItensOrcamento() {
        SharedPreferences sharedPreferences = getSharedPreferences("DeepQuaerePrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("JWT_TOKEN", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Sessão expirada. Faça login novamente.", Toast.LENGTH_LONG).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        String authHeader = token.startsWith("Bearer ") ? token : "Bearer " + token;

        apiService.listarItensOrcamento(authHeader, projetoId).enqueue(new Callback<List<ItemOrcamento>>() {
            @Override
            public void onResponse(Call<List<ItemOrcamento>> call, Response<List<ItemOrcamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ItemOrcamento> itens = response.body();

                    if (recyclerViewItens != null) {
                        itemOrcamentoAdapter = new ItemOrcamentoAdapter(itens);
                        recyclerViewItens.setAdapter(itemOrcamentoAdapter);
                    }

                    double totalOrcamento = 0.0;
                    for (ItemOrcamento item : itens) {
                        totalOrcamento += item.getValor();
                    }

                    if (tvTotalOrcamento != null) {
                        tvTotalOrcamento.setText(String.format("Total: R$ %.2f", totalOrcamento));
                    }

                } else {
                    String erroMsg = "Erro desconhecido";
                    try {
                        if (response.errorBody() != null) {
                            erroMsg = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        erroMsg = e.getMessage();
                    }
                    Toast.makeText(DetalhesProjetoActivity.this, "Erro " + response.code() + ": " + erroMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ItemOrcamento>> call, Throwable t) {
                Toast.makeText(DetalhesProjetoActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarDialogoAdicionarItem() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputDescricao = new EditText(this);
        inputDescricao.setHint("Descrição do item");
        layout.addView(inputDescricao);

        final EditText inputValor = new EditText(this);
        inputValor.setHint("Valor (ex: 150.00)");
        inputValor.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inputValor);

        final EditText inputCategoria = new EditText(this);
        inputCategoria.setHint("Categoria (ex: Material, Mão de Obra)");
        layout.addView(inputCategoria);

        new AlertDialog.Builder(this)
                .setTitle("Novo Item de Orçamento")
                .setView(layout)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String descricao = inputDescricao.getText().toString().trim();
                    String valorStr = inputValor.getText().toString().trim();
                    String categoria = inputCategoria.getText().toString().trim();

                    if (!descricao.isEmpty() && !valorStr.isEmpty()) {
                        try {
                            double valor = Double.parseDouble(valorStr);
                            criarItemNaApi(descricao, valor, categoria);
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Preencha a descrição e o valor", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void criarItemNaApi(String descricao, double valor, String categoria) {
        SharedPreferences sharedPreferences = getSharedPreferences("DeepQuaerePrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("JWT_TOKEN", "");
        String authHeader = token.startsWith("Bearer ") ? token : "Bearer " + token;

        ItemOrcamento novoItem = new ItemOrcamento();
        novoItem.setDescricao(descricao);
        novoItem.setValor(valor);
        novoItem.setValorPlanejado(valor);
        novoItem.setCategoria(categoria);
        novoItem.setProjeto(projetoId);

        ApiService apiService = RetrofitClient.getApiService();

        apiService.criarItemOrcamento(authHeader, projetoId, novoItem).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetalhesProjetoActivity.this, "Item adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                    carregarItensOrcamento();
                } else {
                    String erroDetalhado = "";
                    try {
                        if (response.errorBody() != null) {
                            erroDetalhado = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        erroDetalhado = e.getMessage();
                    }
                    Toast.makeText(DetalhesProjetoActivity.this, "Erro " + response.code() + ": " + erroDetalhado, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(DetalhesProjetoActivity.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}