package com.williamcoelho.cardview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.williamcoelho.cardview.R;
import com.williamcoelho.cardview.model.Postagens;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Postagens> postagem;

    public Adapter(List<Postagens> p){
        this.postagem = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postagem_xml, parent, false);


        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {

        Postagens pInterno = postagem.get(position);

        holder.lugar.setText(pInterno.getLugar());
        holder.data.setText(pInterno.getData());
        holder.localizacao.setText(pInterno.getLocalizacao());
        holder.postagem.setImageResource(pInterno.getImagem());

    }

    @Override
    public int getItemCount() {
        return postagem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView lugar;
        private TextView data;
        private TextView localizacao;
        private ImageView postagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lugar = itemView.findViewById(R.id.textLugar);
            data = itemView.findViewById(R.id.textData);
            localizacao = itemView.findViewById(R.id.textLocalizacao);
            postagem = itemView.findViewById(R.id.imagePostagem);

        }
    }


}
