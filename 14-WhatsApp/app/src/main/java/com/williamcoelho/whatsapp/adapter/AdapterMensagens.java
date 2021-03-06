package com.williamcoelho.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.williamcoelho.whatsapp.R;
import com.williamcoelho.whatsapp.helper.UsuarioFirebase;
import com.williamcoelho.whatsapp.model.Mensagem;

import java.util.List;

public class AdapterMensagens extends RecyclerView.Adapter<AdapterMensagens.MyViewHolder> {

    private List<Mensagem> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    public AdapterMensagens(List<Mensagem> lista, Context c) {
        this.mensagens = lista;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = null;

        if(viewType == TIPO_REMETENTE){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente, parent, false);
        }else if(viewType == TIPO_DESTINATARIO){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario, parent, false);
        }

        return new MyViewHolder(item);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Mensagem mensagem = mensagens.get(position);

        String conteudoTexto = mensagem.getConteudo();
        String conteudoImagem = mensagem.getImagem();

        if(conteudoImagem != null){

            Uri url = Uri.parse(conteudoImagem);
            Glide.with(context).load(url).into(holder.imagem);

            String nome = mensagem.getNome();
            if(!nome.isEmpty()){
                holder.nome.setText(nome);
            }
            else{
                holder.nome.setVisibility(View.GONE);
            }
            holder.mensagem.setVisibility(View.GONE);
        }else{

            holder.mensagem.setText(conteudoTexto);
            String nome = mensagem.getNome();
            if(!nome.isEmpty()){
                holder.nome.setText(nome);
            }else{
                holder.nome.setVisibility(View.GONE);
            }
            holder.imagem.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {

        Mensagem mensagem = mensagens.get(position);

        String idUsuario = UsuarioFirebase.getIdUsuario();

        if(idUsuario.equals(mensagem.getIdUsuario())){
            return TIPO_REMETENTE;
        }

        return TIPO_DESTINATARIO;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mensagem, nome;
        ImageView imagem;

        public MyViewHolder(View itemView){
            super(itemView);

            nome = itemView.findViewById(R.id.textNomeExibicao);
            mensagem = itemView.findViewById(R.id.textoMensagem);
            imagem = itemView.findViewById(R.id.imageMensagem);
        }

    }

}
