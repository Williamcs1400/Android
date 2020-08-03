package com.williamcoelho.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.williamcoelho.whatsapp.config.ConfiguracaoFirebase;

public class Conversa {

    private String idRemetente;
    private String idDestinatario;
    private String ultimaMensagem;
    private Usuario usuarioExibicao;
    private String ehGrupo;
    private Grupo grupo;

    public Conversa() {

        this.setEhGrupo("false");

    }

    public void salvar(){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference conversaRef = databaseReference.child("Conversas");

        conversaRef.child(this.getIdRemetente()).child(this.getIdDestinatario()).setValue(this);

    }

    public String getIdRemetente() {
        return idRemetente;
    }

    public String getEhGrupo() {
        return ehGrupo;
    }

    public void setEhGrupo(String ehGrupo) {
        this.ehGrupo = ehGrupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public Usuario getUsuarioExibicao() {
        return usuarioExibicao;
    }

    public void setUsuarioExibicao(Usuario usuarioExibicao) {
        this.usuarioExibicao = usuarioExibicao;
    }
}
