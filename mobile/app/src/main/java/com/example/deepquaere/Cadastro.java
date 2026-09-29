package com.example.deepquaere;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deepquaere.api.ApiService;
import com.example.deepquaere.api.RetrofitClient;
import com.example.deepquaere.model.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cadastro extends AppCompatActivity {

    private EditText etNovoUsername, etNovaPassword;
    private Button btnConcluirCadastro, btnVoltarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etNovoUsername = findViewById(R.id.etNovoUsername);
        etNovaPassword = findViewById(R.id.etNovaPassword);
        btnConcluirCadastro = findViewById(R.id.btnConcluirCadastro);
        btnVoltarLogin = findViewById(R.id.btnVoltarLogin);

        btnConcluirCadastro.setOnClickListener(v -> {
            String username = etNovoUsername.getText().toString().trim();
            String password = etNovaPassword.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                fazerCadastroNaApi(username, password);
            } else {
                Toast.makeText(Cadastro.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            }
        });

        btnVoltarLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Cadastro.this, Login.class);
            startActivity(intent);
            finish();
        });
    }

    private void fazerCadastroNaApi(String username, String password) {
        ApiService apiService = RetrofitClient.getApiService();
        LoginRequest request = new LoginRequest(username, password);

        apiService.cadastrarUtilizador(request).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Cadastro.this, "Conta criada com sucesso! Faça login.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Cadastro.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    String erroMsg = "Erro no cadastro: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            erroMsg += " - " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Cadastro.this, erroMsg, Toast.LENGTH_LONG).show();
                    android.util.Log.e("CADASTRO_ERRO", erroMsg);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(Cadastro.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
                android.util.Log.e("CADASTRO_FAIL", "Falha: ", t);
            }
        });
    }
}
