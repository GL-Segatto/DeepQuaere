package com.example.deepquaere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deepquaere.api.ApiService;
import com.example.deepquaere.api.RetrofitClient;
import com.example.deepquaere.model.LoginRequest;
import com.example.deepquaere.model.TokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnIrParaCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Vinculando os componentes do XML
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnIrParaCadastro = findViewById(R.id.btnIrParaCadastro);

        // Ação do Botão de Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Botão Clicado!", Toast.LENGTH_SHORT).show();

                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    fazerLoginNaApi(username, password);
                } else {
                    Toast.makeText(Login.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Ação para navegar para a tela de cadastro
        btnIrParaCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Cadastro.class);
            startActivity(intent);
        });
    }

    private void fazerLoginNaApi(String username, String password) {
        try {
            Toast.makeText(Login.this, "Iniciando Retrofit...", Toast.LENGTH_SHORT).show();

            ApiService apiService = RetrofitClient.getApiService();
            if (apiService == null) {
                Toast.makeText(Login.this, "Erro: ApiService está nulo!", Toast.LENGTH_LONG).show();
                return;
            }

            LoginRequest request = new LoginRequest(username, password);
            Toast.makeText(Login.this, "Enviando dados...", Toast.LENGTH_SHORT).show();

            apiService.login(request).enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    Toast.makeText(Login.this, "Resposta recebida! Código: " + response.code(), Toast.LENGTH_LONG).show();

                    if (response.isSuccessful() && response.body() != null) {
                        String accessToken = response.body().getAccess();

                        SharedPreferences sharedPreferences = getSharedPreferences("DeepQuaerePrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("JWT_TOKEN", "Bearer " + accessToken);
                        editor.apply();

                        Toast.makeText(Login.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Login.this, Menu.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String erroMsg = "Erro na API: " + response.code();
                        try {
                            if (response.errorBody() != null) {
                                erroMsg += " - " + response.errorBody().string();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(Login.this, erroMsg, Toast.LENGTH_LONG).show();
                        android.util.Log.e("LOGIN_ERRO", erroMsg);
                    }
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    Toast.makeText(Login.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    android.util.Log.e("LOGIN_FAIL", "Falha: ", t);
                }
            });

        } catch (Exception e) {
            Toast.makeText(Login.this, "Exceção capturada: " + e.getMessage(), Toast.LENGTH_LONG).show();
            android.util.Log.e("LOGIN_EXCEPTION", "Erro crítico: ", e);
        }
    }
}