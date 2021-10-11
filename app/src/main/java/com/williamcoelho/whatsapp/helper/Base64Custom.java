package com.williamcoelho.whatsapp.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codificar(String palavra){

        return Base64.encodeToString(palavra.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");

    }

    public static String Decodificar(String palavra) {

        return new String(Base64.decode(palavra, Base64.DEFAULT));

    }

}
