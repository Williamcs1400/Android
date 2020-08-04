package com.williamcoelho.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.williamcoelho.whatsapp.config.ConfiguracaoFirebase;
import com.williamcoelho.whatsapp.helper.Base64Custom;
import com.williamcoelho.whatsapp.helper.UsuarioFirebase;

import java.io.Serializable;
import java.util.List;

public class Grupo implements Serializable {

    private String id;
    private String nome;
    private String foto;
    private List<Usuario> membros;

    public Grupo() {

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference grupoRef = database.child("Grupos");

        String idFirebase  = grupoRef.push().getKey();
        setId(idFirebase);

    }

    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference grupoRef = database.child("Grupos").child(getId());

        grupoRef.setValue(this);

        for(Usuario usuario: getMembros()){

            String idRemetente = Base64Custom.codificar(usuario.getEmail());
            String idDestinatario = getId();

            Conversa conversa = new Conversa();
            conversa.setIdRemetente(idRemetente);
            conversa.setIdDestinatario(idDestinatario);
            conversa.setUltimaMensagem(conversa.getUltimaMensagem());
            conversa.setEhGrupo("true");
            conversa.setGrupo(this);

            conversa.salvar();

        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }
}
