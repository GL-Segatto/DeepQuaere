package com.example.deepquaere;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.deepquaere.model.ItemOrcamento;
import java.util.List;

public class ItemOrcamentoAdapter extends RecyclerView.Adapter<ItemOrcamentoAdapter.ItemViewHolder> {

    private List<ItemOrcamento> listaItens;

    public ItemOrcamentoAdapter(List<ItemOrcamento> listaItens) {
        this.listaItens = listaItens;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orcamento, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemOrcamento item = listaItens.get(position);

        // Substitua por getDescricao() ou o nome do método correspondente na sua classe ItemOrcamento
        holder.tvDescricao.setText(item.getDescricao());
        holder.tvValor.setText("R$ " + item.getValor());
    }

    @Override
    public int getItemCount() {
        return listaItens != null ? listaItens.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescricao, tvValor;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoItem);
            tvValor = itemView.findViewById(R.id.tvValorItem);
        }
    }
}