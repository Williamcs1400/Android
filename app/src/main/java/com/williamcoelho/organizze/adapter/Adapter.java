package com.williamcoelho.organizze.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.williamcoelho.organizze.R;
import com.williamcoelho.organizze.model.Movimentacao;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    List<Movimentacao> movimentacoes;
    Context context;

    public Adapter(List<Movimentacao> movimentacoes, Context context){
        this.movimentacoes = movimentacoes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movimentacao movimentacao = movimentacoes.get(position);

        holder.origem.setText(movimentacao.getCategoria());
        holder.descricao.setText(movimentacao.getDescricao());
        holder.valor.setText(String.valueOf(movimentacao.getValor()));
        holder.valor.setTextColor(context.getResources().getColor(R.color.greenButton));


        if(movimentacao.getTipo() == "d" || movimentacao.getTipo().equals("d")){
            holder.valor.setTextColor(context.getResources().getColor(R.color.redButton));
            holder.valor.setText("-" + movimentacao.getValor());
        }


    }

    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView origem;
        TextView descricao;
        TextView valor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            origem = itemView.findViewById(R.id.adapterOrigem);
            descricao = itemView.findViewById(R.id.adapterDescrição);
            valor = itemView.findViewById(R.id.adapterValor);

        }
    }

}
