package com.williamcoelho.instagram.helper;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.williamcoelho.instagram.model.Usuario;

public class UsuarioFirebase {

    public static FirebaseUser getUsuarioAtual(){

        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        return auth.getCurrentUser();

    }

    public static String getIdentificadorUsuario(){

        return getUsuarioAtual().getUid();
    }

    public static void atualizaNomeUsuario(String nome){

        try{

            FirebaseUser user = getUsuarioAtual();

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Usuario getDadosUsuarioLogado(){

        FirebaseUser user = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(user.getEmail());
        usuario.setNome(user.getDisplayName());
        usuario.setIdUsuario(user.getUid());

        if(user.getPhotoUrl() == null){
            usuario.setLinkFoto("");
        }else{
            usuario.setLinkFoto(user.getPhotoUrl().toString());
        }
        return usuario;
    }

}
