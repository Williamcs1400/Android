package com.williamcoelho.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.williamcoelho.whatsapp.R;
import com.williamcoelho.whatsapp.config.ConfiguracaoFirebase;
import com.williamcoelho.whatsapp.helper.Base64Custom;
import com.williamcoelho.whatsapp.helper.UsuarioFirebase;
import com.williamcoelho.whatsapp.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private TextInputEditText editNome;
    private TextInputEditText editEmail;
    private TextInputEditText editSenha;

    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editNome = findViewById(R.id.editNomeApp);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);

    }

    public void fazerCadastroFirebase() {

        auth = ConfiguracaoFirebase.getAutenticacaoFirebase();

        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();

                        String idUsuario = Base64Custom.codificar(usuario.getEmail());
                        usuario.setIdUsuario(idUsuario);
                        usuario.salvarDatabase();

                        UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                        finish();

                    } else {

                        String excecao = "";

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            excecao = "Digite uma senha mais forte!";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            excecao = "Digite uma email válido!";
                        } catch (FirebaseAuthUserCollisionException e) {
                            excecao = "Essa conta já foi cadastrada!";
                        } catch (Exception e) {
                            excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                            e.printStackTrace();
                        }

                        Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_LONG).show();

                    }
                }
            });
    }

    public void checaDadosDigitados(View view){

        String textNome = editNome.getText().toString();
        String textEmail = editEmail.getText().toString();
        String textSenha = editSenha.getText().toString();

        if(!textNome.isEmpty()){
           if(!textEmail.isEmpty()){
               if(!textSenha.isEmpty()){

                   usuario = new Usuario();

                   usuario.setNome(textNome);
                   usuario.setEmail(textEmail);
                   usuario.setSenha(textSenha);

                   fazerCadastroFirebase();

               }else{
                   Toast.makeText(CadastroActivity.this, "Digite uma senha!", Toast.LENGTH_SHORT).show();
               }
           }else{
               Toast.makeText(CadastroActivity.this, "Digite um email!", Toast.LENGTH_SHORT).show();
           }
        }else{
            Toast.makeText(CadastroActivity.this, "Digite um nome!", Toast.LENGTH_SHORT).show();
        }

    }

}
