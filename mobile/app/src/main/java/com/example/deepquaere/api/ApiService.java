package com.example.deepquaere.api;

import com.example.deepquaere.model.ItemOrcamento;
import com.example.deepquaere.model.LoginRequest;
import com.example.deepquaere.model.TokenResponse;
import com.example.deepquaere.model.Projeto;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/token/")
    Call<TokenResponse> login(@Body LoginRequest request);

    @GET("api/projetos/")
    Call<List<Projeto>> listarProjetos(@Header("Authorization") String token);

    @POST("api/usuarios/")
    Call<Object> cadastrarUtilizador(@Body LoginRequest request);

    @POST("api/projetos/")
    Call<Object> criarProjeto(
            @Header("Authorization") String token,
            @Body Projeto request
    );

    // CORRIGIDO: Agora usa @Path para buscar os itens do projeto específico na rota aninhada
    @GET("api/projetos/{projeto_id}/itens_orcamento/")
    Call<List<ItemOrcamento>> listarItensOrcamento(
            @Header("Authorization") String token,
            @Path("projeto_id") int projetoId
    );

    @POST("api/projetos/{projeto_id}/itens_orcamento/")
    Call<Object> criarItemOrcamento(
            @Header("Authorization") String token,
            @Path("projeto_id") int projetoId,
            @Body ItemOrcamento item
    );
}