package com.williamcoelho.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.transition.CircularPropagation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.williamcoelho.whatsapp.R;
import com.williamcoelho.whatsapp.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterGrupoSelecionado extends RecyclerView.Adapter<AdapterGrupoSelecionado.MyViewHolder> {

    private List<Usuario> listaContatos;
    private Context context;

    public AdapterGrupoSelecionado(List<Usuario> contatos, Context c) {

        this.listaContatos = contatos;
        this.context = c;

    }

    @NonNull
    @Override
    public AdapterGrupoSelecionado.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_grupo_selecionado, parent, false);

        return new AdapterGrupoSelecionado.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGrupoSelecionado.MyViewHolder holder, int position) {

        Usuario usuario = listaContatos.get(position);

        holder.textNome.setText(usuario.getNome());

        if(usuario.getFoto() != null){

            Uri uri = Uri.parse(usuario.getFoto());

            Glide.with(context).load(uri).into(holder.imageFotoPerfil);

        }else{
            holder.imageFotoPerfil.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageFotoPerfil;
        TextView textNome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFotoPerfil = itemView.findViewById(R.id.imageViewFotoMembroSelecionado);
            textNome = itemView.findViewById(R.id.textNomeMembroSelecionado);

        }
    }
}
