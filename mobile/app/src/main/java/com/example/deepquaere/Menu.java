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

        Button btnVerProjetos = findViewById(R.id.btnVerProjetos);
        Button btnCriarProjeto = findViewById(R.id.btnCriarProjeto);

        btnVerProjetos.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, MainActivity.class);
            startActivity(intent);
        });

        btnCriarProjeto.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, CriarProjeto.class);
            startActivity(intent);
        });
    }
}
