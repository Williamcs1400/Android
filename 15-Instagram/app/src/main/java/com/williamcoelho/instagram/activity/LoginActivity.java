package com.williamcoelho.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;
import com.williamcoelho.instagram.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmailLogin;
    private EditText editSenhaLogin;
    private ProgressBar progressLogin;

    private Usuario usuario;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        //Configuracoes iniciais
        editEmailLogin = findViewById(R.id.editEmailTelaLogin);
        editSenhaLogin = findViewById(R.id.editSenhaTelaLogin);
        progressLogin = findViewById(R.id.progressLogin);

        editEmailLogin.requestFocus();

    }

    public void verificarUsuarioLogado(){

        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    public void fazerLogin(View view){

        progressLogin.setVisibility(View.VISIBLE);

        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if(validaCampos()){

            auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    progressLogin.setVisibility(View.GONE);

                    if(task.isSuccessful()){

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

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
    }

    public boolean validaCampos(){

        String email = editEmailLogin.getText().toString();
        String senha = editSenhaLogin.getText().toString();

        if(!email.isEmpty()){
            if(!senha.isEmpty()){

                usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);

                return true;

            }else{
                Toast.makeText(getApplicationContext(), "Digite sua senha", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Digite seu email", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void abrirCadastro(View view){

        Intent i = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(i);

    }

}
