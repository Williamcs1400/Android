package com.williamcoelho.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth auth;
    private static DatabaseReference data;

    //Retorna a instancia do firebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){

        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    //Retorna a instancia do firebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){

        if(data == null){
            data = FirebaseDatabase.getInstance().getReference();
        }
        return data;
    }



}
