package com.williamcoelho.whatsapp.helper;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.williamcoelho.whatsapp.config.ConfiguracaoFirebase;
import com.williamcoelho.whatsapp.model.Usuario;

public class UsuarioFirebase {

    public static String getIdUsuario(){

        FirebaseAuth auth = ConfiguracaoFirebase.getAutenticacaoFirebase();
        String email = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificar(email);

        return idUsuario;
    }

    public static FirebaseUser getUsuarioAtual(){

        FirebaseAuth auth = ConfiguracaoFirebase.getAutenticacaoFirebase();

        return auth.getCurrentUser();

    }

    public static boolean atualizarFotoUsuario(Uri uri){

        try{

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(!task.isSuccessful()){

                    }

                }
            });

            return true;

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean atualizarNomeUsuario(String nome){

        try{

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(!task.isSuccessful()){

                    }

                }
            });

            return true;

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario getDadosUsuariosLogado(){

        FirebaseUser user = getUsuarioAtual();

        Usuario dadosUsuario = new Usuario();

        dadosUsuario.setEmail(user.getEmail());
        dadosUsuario.setNome(user.getDisplayName());

        if(user.getPhotoUrl() == null){
            dadosUsuario.setFoto("");
        }else{
            dadosUsuario.setFoto(user.getPhotoUrl().toString());
        }

        return dadosUsuario;

    }

}
