package com.example.deepquaere;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.deepquaere.model.Projeto;
import java.util.List;

public class ProjetoAdapter extends RecyclerView.Adapter<ProjetoAdapter.ProjetoViewHolder> {

    private List<Projeto> listaProjetos;
    private OnProjetoClickListener listener; // Declaração do listener

    // Construtor atualizado para receber o listener
    public ProjetoAdapter(List<Projeto> listaProjetos, OnProjetoClickListener listener) {
        this.listaProjetos = listaProjetos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProjetoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_projeto, parent, false);
        return new ProjetoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjetoViewHolder holder, int position) {
        Projeto projeto = listaProjetos.get(position);
        holder.tvNome.setText(projeto.getNome());
        holder.tvDatas.setText("Início: " + projeto.getDataInicio());

        // Configura o evento de clique para cada item da lista
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProjetoClick(projeto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProjetos != null ? listaProjetos.size() : 0;
    }

    public static class ProjetoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvDatas;

        public ProjetoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeProjeto);
            tvDatas = itemView.findViewById(R.id.tvDatasProjeto);
        }
    }

    public interface OnProjetoClickListener {
        void onProjetoClick(Projeto projeto);
    }
}