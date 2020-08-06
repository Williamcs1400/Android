package com.williamcoelho.instagram.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    String nome;
    String email;
    String senha;
    String idUsuario;
    String linkFoto;

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

    public Map<String, Object> converterParaMap(){

        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("idUsuario", getIdUsuario());
        usuarioMap.put("linkFoto", getLinkFoto());

        return usuarioMap;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
