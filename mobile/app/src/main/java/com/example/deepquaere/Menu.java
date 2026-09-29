package com.example.deepquaere;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Referência aos botões do menu
        Button btnVerProjetos = findViewById(R.id.btnVerProjetos);
        Button btnCriarProjeto = findViewById(R.id.btnCriarProjeto);

        // 1. Botão "Gerenciar Projetos" - AGORA abre a MainActivity (listagem)
        btnVerProjetos.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, MainActivity.class);
            startActivity(intent);
        });

        // 2. Botão "Novo Projeto" - Abre a tela CriarProjeto
        btnCriarProjeto.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, CriarProjeto.class);
            startActivity(intent);
        });
    }
}