package com.example.deepquaere.model;

public class Projeto {
    private int id_projeto;
    private String nome;
    private String descricao;
    private String data_inicio;
    private String data_fim;

    public Projeto() {
    }

    public Projeto(int id_projeto, String nome) {
        this.id_projeto = id_projeto;
        this.nome = nome;
    }

    // Getters
    public int getId() {
        return id_projeto; // Corrigido para retornar id_projeto
    }

    public int getIdProjeto() {
        return id_projeto;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDataInicio() {
        return data_inicio;
    }

    public String getDataFim() {
        return data_fim;
    }

    public void setIdProjeto(int id_projeto) {
        this.id_projeto = id_projeto;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataInicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public void setDataFim(String data_fim) {
        this.data_fim = data_fim;
    }
}