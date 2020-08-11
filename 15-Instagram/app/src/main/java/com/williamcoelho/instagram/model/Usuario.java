package com.williamcoelho.instagram.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private String nome;
    private String email;
    private String senha;
    private String idUsuario;
    private String linkFoto;
    private int seguidores = 0;
    private int seguindo = 0;
    private int publicacoes = 0;

    public Usuario() {
    }

    public void salvar(){

        DatabaseReference reference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference usuarioRef = reference.child("Usuários").child(getIdUsuario());

        usuarioRef.setValue(this);

    }

    public void atualizar(){

        DatabaseReference reference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference usuarioRef = reference.child("Usuários").child(getIdUsuario());

        Map<String, Object> valoresUsuario = converterParaMap();
        usuarioRef.updateChildren(valoresUsuario);

    }

    public void atualizarQtdPostagem(){

        DatabaseReference reference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference usuarioRef = reference.child("Usuários").child(getIdUsuario());

        Map<String, Object> dados = new HashMap<>();
        dados.put("publicacoes", getPublicacoes());
        usuarioRef.updateChildren(dados);

    }

    public Map<String, Object> converterParaMap(){

        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("idUsuario", getIdUsuario());
        usuarioMap.put("linkFoto", getLinkFoto());
        usuarioMap.put("seguidores", getSeguidores());
        usuarioMap.put("seguindo", getSeguindo());
        usuarioMap.put("publicacoes", getPublicacoes());

        return usuarioMap;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(int seguindo) {
        this.seguindo = seguindo;
    }

    public int getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(int publicacoes) {
        this.publicacoes = publicacoes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.toUpperCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getLinkFoto() {
        return linkFoto;
    }

    public void setLinkFoto(String linkFoto) {
        this.linkFoto = linkFoto;
    }
}
