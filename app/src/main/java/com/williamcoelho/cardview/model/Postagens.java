package com.williamcoelho.cardview.model;

public class Postagens {

    private String lugar;
    private String data;
    private String localizacao;
    private int imagem;

    public Postagens(){

    }

    public Postagens(String lugar, String data, String localizacao, int imagem) {
        this.lugar = lugar;
        this.data = data;
        this.localizacao = localizacao;
        this.imagem = imagem;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}
