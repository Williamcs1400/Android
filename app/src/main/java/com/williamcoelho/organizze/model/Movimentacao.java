package com.williamcoelho.organizze.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.williamcoelho.organizze.config.ConfiguracaoFirebase;
import com.williamcoelho.organizze.helper.Base64Custom;
import com.williamcoelho.organizze.helper.DateCustom;

public class Movimentacao {

    private String data, categoria, descricao, tipo;
    private String key;
    private Double valor;

    public Movimentacao() {
    }

    public void salvar(String data){

        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference.child("movimentacao")
                         .child(idUsuario)
                         .child(DateCustom.mesAnoEscolhido(data))
                         .push()
                         .setValue(this);

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
