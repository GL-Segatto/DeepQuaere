package com.example.deepquaere.model;

import com.google.gson.annotations.SerializedName;

public class ItemOrcamento {

    @SerializedName("id_item_orcamento")
    private int id_item_orcamento;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("valor")
    private double valor;

    @SerializedName("valor_planejado")
    private double valorPlanejado;

    @SerializedName("categoria")
    private String categoria;

    @SerializedName("projeto")
    private int projeto;

    // Construtores vazios e com parâmetros (se necessário)
    public ItemOrcamento() {
    }

    // Getters e Setters
    public int getId_item_orcamento() {
        return id_item_orcamento;
    }

    public void setId_item_orcamento(int id_item_orcamento) {
        this.id_item_orcamento = id_item_orcamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValorPlanejado() {
        return valorPlanejado;
    }

    public void setValorPlanejado(double valorPlanejado) {
        this.valorPlanejado = valorPlanejado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getProjeto() {
        return projeto;
    }

    public void setProjeto(int projeto) {
        this.projeto = projeto;
    }
}