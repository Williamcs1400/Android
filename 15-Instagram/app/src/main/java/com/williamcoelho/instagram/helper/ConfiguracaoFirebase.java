package com.williamcoelho.instagram.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static FirebaseAuth auth;
    private static DatabaseReference database;
    private static StorageReference storage;

    public static FirebaseAuth getFirebaseAutenticacao(){

        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }

    public static DatabaseReference getDatabaseReference(){

        if(database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }

        return database;
    }

    public static StorageReference getStorageReference(){

        if(storage == null){
            storage = FirebaseStorage.getInstance().getReference();
        }

        return storage;

    }


}
