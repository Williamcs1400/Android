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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;
import com.williamcoelho.instagram.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNomeCadastro;
    private EditText editEmailCadastro;
    private EditText editSenhaCadastro;
    private ProgressBar progressCadastro;

    private Usuario usuario;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Configuracoes iniciais
        editNomeCadastro = findViewById(R.id.editNomeCadastro);
        editEmailCadastro = findViewById(R.id.editEmailCadastro);
        editSenhaCadastro = findViewById(R.id.editSenhaCadastro);
        progressCadastro = findViewById(R.id.progressCadastro);

        editNomeCadastro.requestFocus();

        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

    }

    public void cadastarUsuario(View view){

        progressCadastro.setVisibility(View.VISIBLE);

        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if(validaCampos()){
            auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    progressCadastro.setVisibility(View.GONE);

                    if(task.isSuccessful()){

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    }else{

                        String excecao = "";
                        try{
                            throw task.getException();
                        }catch(FirebaseAuthWeakPasswordException e){
                            excecao = "Digite uma senha mais forte";
                        }catch(FirebaseAuthInvalidCredentialsException e){
                            excecao = "Digite um email valido";
                        }catch(FirebaseAuthUserCollisionException e){
                            excecao = "Esse email já foi cadastrado";
                        }catch(Exception e){
                            excecao = "Erro ao cadastrar" + e.getMessage();
                        }
                        Toast.makeText(getApplicationContext(), excecao, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public boolean validaCampos(){

        String nome = editNomeCadastro.getText().toString();
        String email = editEmailCadastro.getText().toString();
        String senha = editSenhaCadastro.getText().toString();

        if(!nome.isEmpty()){
            if(!email.isEmpty()){
                if(!senha.isEmpty()){

                    usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    return true;
                }else{
                    Toast.makeText(getApplicationContext(), "Preencha uma senha", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Preencha um email", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Preencha um nome", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

}
