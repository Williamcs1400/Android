package com.williamcoelho.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.williamcoelho.whatsapp.R;
import com.williamcoelho.whatsapp.model.Conversa;
import com.williamcoelho.whatsapp.model.Grupo;
import com.williamcoelho.whatsapp.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterConversas extends RecyclerView.Adapter<AdapterConversas.MyViewHolder> {

    private List<Conversa> conversas;
    private Context context;

    public AdapterConversas(List<Conversa> conversa, Context c) {

        this.conversas = conversa;
        this.context = c;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_conversas, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Conversa listaConversa = conversas.get(position);
        holder.textViewEmailContato.setText(listaConversa.getUltimaMensagem());

        if(listaConversa.getEhGrupo().equals("true")){

            Grupo grupo = listaConversa.getGrupo();
            holder.textViewNomeContato.setText(grupo.getNome());

            if(grupo.getFoto() != null){

                Uri uri = Uri.parse(grupo.getFoto());

                Glide.with(context).load(uri).into(holder.imageViewFotoContato);

            }else{

                holder.imageViewFotoContato.setImageResource(R.drawable.ic_grupo_perfil);

            }


        }else {

            Usuario usuario = listaConversa.getUsuarioExibicao();

            if(usuario != null){

                holder.textViewNomeContato.setText(listaConversa.getUsuarioExibicao().getNome());
                holder.textViewEmailContato.setText(listaConversa.getUltimaMensagem());

                if(listaConversa.getUsuarioExibicao().getFoto() != null){

                    Uri uri = Uri.parse(listaConversa.getUsuarioExibicao().getFoto());

                    Glide.with(context).load(uri).into(holder.imageViewFotoContato);

                }else{

                    holder.imageViewFotoContato.setImageResource(R.drawable.padrao);

                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView imageViewFotoContato;
        private TextView textViewNomeContato;
        private TextView textViewEmailContato;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewFotoContato = itemView.findViewById(R.id.imageViewFotoContato);
            textViewNomeContato = itemView.findViewById(R.id.textViewNomeContato);
            textViewEmailContato = itemView.findViewById(R.id.textViewEmailContato);

        }
    }

}
