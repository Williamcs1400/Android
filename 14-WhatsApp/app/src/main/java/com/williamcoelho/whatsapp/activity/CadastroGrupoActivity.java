package com.williamcoelho.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.williamcoelho.whatsapp.R;
import com.williamcoelho.whatsapp.adapter.AdapterGrupoSelecionado;
import com.williamcoelho.whatsapp.config.ConfiguracaoFirebase;
import com.williamcoelho.whatsapp.helper.UsuarioFirebase;
import com.williamcoelho.whatsapp.model.Grupo;
import com.williamcoelho.whatsapp.model.Usuario;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroGrupoActivity extends AppCompatActivity {

    private CircleImageView imageGrupo;
    private EditText editNomeGrupo;
    private TextView textContParticipantes;
    private RecyclerView recyclerMembrosGrupo;

    private Grupo grupo;

    private AdapterGrupoSelecionado adapterGrupoSelecionado;

    private StorageReference storageReference;

    private Usuario usuarioLogado;

    private List<Usuario> membrosSelecionados = new ArrayList<>();

    private static final int SELECAO_GALERIA = 200;

    private FloatingActionButton fabSalvaGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo grupo");
        toolbar.setSubtitle("Adicione um nome ao grupo");
        setSupportActionBar(toolbar);

        //Cofiguracoes iniciais
        imageGrupo = findViewById(R.id.imagePerfilGrupo);
        editNomeGrupo = findViewById(R.id.editNomeGrupo);
        textContParticipantes = findViewById(R.id.textContParticipantes);
        recyclerMembrosGrupo = findViewById(R.id.recyclerMembrosGrupo);
        fabSalvaGrupo = findViewById(R.id.fabSalvarGrupo);

        grupo = new Grupo();

        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        usuarioLogado = UsuarioFirebase.getDadosUsuariosLogado();

        //Recupera lista de membros passada
        if(getIntent().getExtras() != null){
            List<Usuario> membros = (List<Usuario>) getIntent().getExtras().getSerializable("Membros");
            membrosSelecionados.addAll(membros);

            textContParticipantes.setText("Participantes: " + membrosSelecionados.size());

        }

        //Configura o adapter dos membros selecionados
        adapterGrupoSelecionado = new AdapterGrupoSelecionado(membrosSelecionados, getApplicationContext());

        //Configura o recycler view dos membros selecionados
        RecyclerView.LayoutManager layoutManagerHorizontal = new GridLayoutManager(getApplicationContext(), 3);
        recyclerMembrosGrupo.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosGrupo.setHasFixedSize(true);
        recyclerMembrosGrupo.setAdapter(adapterGrupoSelecionado);

        imageGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, SELECAO_GALERIA);
                }
            }
        });

        fabSalvaGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeGrupo = editNomeGrupo.getText().toString();
                membrosSelecionados.add(UsuarioFirebase.getDadosUsuariosLogado());

                if(!nomeGrupo.isEmpty()){

                    grupo.setMembros(membrosSelecionados);
                    grupo.setNome(nomeGrupo);
                    grupo.salvar();

                    Intent i = new Intent(CadastroGrupoActivity.this, ChatActivity.class);
                    i.putExtra("chatGrupo", grupo);
                    startActivity(i);

                }else{
                    Toast.makeText(getApplicationContext(), "Digite um nome para o grupo", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try {

                Uri localImagemSelecionada = data.getData();
                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                if(imagem != null){
                    imageGrupo.setImageBitmap(imagem);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    final StorageReference imagemRef = storageReference.child("imagens").child("grupos").child(grupo.getId());

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(CadastroGrupoActivity.this, "Falha ao salvar no Firebase", Toast.LENGTH_LONG).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(CadastroGrupoActivity.this, "Sucesso ao salvar no Firebase", Toast.LENGTH_LONG).show();

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String uri = task.getResult().toString();
                                    grupo.setFoto(uri);
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
}
