package com.williamcoelho.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference data;
    private static StorageReference storage;

    public static FirebaseAuth getAutenticacaoFirebase(){

        if(autenticacao == null){

            autenticacao = FirebaseAuth.getInstance();

        }

        return autenticacao;

    }

    public static DatabaseReference getFirebaseDatabase(){

        if(data == null){

            data = FirebaseDatabase.getInstance().getReference();

        }

        return data;

    }

    public static StorageReference getFirebaseStorage(){

        if(storage == null){

            storage = FirebaseStorage.getInstance().getReference();

        }

        return storage;

    }

}
