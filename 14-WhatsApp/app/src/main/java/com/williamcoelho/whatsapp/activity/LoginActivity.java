package com.williamcoelho.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.williamcoelho.whatsapp.R;
import com.williamcoelho.whatsapp.config.ConfiguracaoFirebase;
import com.williamcoelho.whatsapp.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private TextInputEditText editEmail;
    private TextInputEditText editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);

    }

    public void fazerLogin(final Usuario usuario){

        auth = ConfiguracaoFirebase.getAutenticacaoFirebase();

        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            abrirMain();

                        }else{

                            String excecao = "";

                            try{
                                throw task.getException();
                            }catch(FirebaseAuthInvalidUserException e){
                                excecao = "Usuário não está cadastrado!";
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                excecao = "Email e senha não correspondem";
                            }catch(Exception e){
                                excecao = "Erro ao fazer login: " + e.getMessage();
                            }

                            Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void checaDadosDigitados(View view){

        String textoEmail = editEmail.getText().toString();
        String textoSenha = editSenha.getText().toString();

        if(!textoEmail.isEmpty()){
            if(!textoSenha.isEmpty()){

                Usuario usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);

                fazerLogin(usuario);
            }else{
                Toast.makeText(LoginActivity.this, "Digite uma senha!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this, "Digite um email!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = ConfiguracaoFirebase.getAutenticacaoFirebase();
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if(usuarioAtual != null){

            abrirMain();

        }

    }

    public void abrirMain(){

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

    }

    public void abrirCadastro(View view){

        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);

    }

}
