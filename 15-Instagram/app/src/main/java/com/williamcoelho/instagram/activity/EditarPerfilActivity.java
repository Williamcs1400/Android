package com.williamcoelho.instagram.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;
import com.williamcoelho.instagram.helper.UsuarioFirebase;
import com.williamcoelho.instagram.model.Usuario;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    private CircleImageView imagePerfil;
    private TextInputEditText editNomePerfil;
    private TextView textAlterarImagem;

    private static final int SELECAO_GALERIA = 200;

    private Usuario usuarioLogado;

    private FirebaseUser usuarioPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        //Configuracoes iniciais
        imagePerfil = findViewById(R.id.imageEditarPerfil);
        textAlterarImagem = findViewById(R.id.textAlterarImagem);
        editNomePerfil = findViewById(R.id.editNomePerfil);

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //Configurar a toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Editar de perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        recuperarDadosUsuario();

    }

    public void recuperarDadosUsuario(){

        usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        editNomePerfil.setText(usuarioPerfil.getDisplayName());
        Uri url = usuarioPerfil.getPhotoUrl();
        if(url != null){
            Glide.with(EditarPerfilActivity.this).load(url).into(imagePerfil);
        }
        else{
            imagePerfil.setImageResource(R.drawable.avatar);
        }

    }

    public void salvarAlteracoes(View view){

        String nomeAtualizado = editNomePerfil.getText().toString();

        //Atualizar no firebaseAuth
        UsuarioFirebase.atualizaNomeUsuario(nomeAtualizado);

        //Atualizar no database
        usuarioLogado.setNome(nomeAtualizado);
        usuarioLogado.atualizar();

        Toast.makeText(getApplicationContext(), "Dados alterados com sucesso!", Toast.LENGTH_SHORT).show();

    }

    public void alterarFoto(View view){

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, SELECAO_GALERIA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;

            try{
                //Selecao da galeria
                switch(requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                //Carregar a imagem
                if(imagem != null){
                    imagePerfil.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar no firebase
                    final StorageReference imagemRef = ConfiguracaoFirebase.getStorageReference()
                            .child("imagens").child("perfil").child(UsuarioFirebase.getIdentificadorUsuario() + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Erro ao fazer upload da imagem!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizarFotoUsuario(url);
                                }
                            });
                        }
                    });

                }

            }catch(Exception e){
                e.printStackTrace();

            }
        }
    }

    private void atualizarFotoUsuario(Uri url){

        //Atualizar a foto no profile do firebase
        UsuarioFirebase.atualizaFotoUsuario(url);

        //Atualizar a foto no database
        usuarioLogado.setLinkFoto(url.toString());
        usuarioLogado.atualizar();

        Toast.makeText(getApplicationContext(), "Sua foto foi atualizada", Toast.LENGTH_SHORT).show();
    }

    //Sobrescreve esse metodo para voltar para a fragment de perfil
    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;
    }
}
