package com.williamcoelho.instagram.model;

import com.google.firebase.database.DatabaseReference;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;

import java.io.Serializable;

public class Postagem implements Serializable {

    private String idPost;
    private String idUsuario;
    private String descricao;
    private String caminhoFoto;

    public Postagem() {

        //Gera o id unico da postagem
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference postagemRef = databaseReference.child("Postagens");
        String idPostagem = postagemRef.push().getKey();
        setIdPost(idPostagem);

    }

    public boolean salvar(){

        DatabaseReference reference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference postagensRef = reference.child("Postagens").child(getIdUsuario()).child(getIdPost());

        postagensRef.setValue(this);

        return true;

    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
