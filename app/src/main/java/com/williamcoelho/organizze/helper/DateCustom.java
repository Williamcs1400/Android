package com.williamcoelho.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual(){

        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;

    }

    public static String mesAnoEscolhido(String data){

        String retornoData[] = data.split("/");
        String dia = retornoData[0]; //Dia 09
        String mes = retornoData[1]; //mes 05
        String ano = retornoData[2]; //ano 2020

        String mesAno = mes + ano;
        return mesAno;

    }

}
