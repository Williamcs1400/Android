package com.williamcoelho.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.williamcoelho.whatsapp.R;
import com.williamcoelho.whatsapp.adapter.AdapterMensagens;
import com.williamcoelho.whatsapp.config.ConfiguracaoFirebase;
import com.williamcoelho.whatsapp.helper.Base64Custom;
import com.williamcoelho.whatsapp.helper.UsuarioFirebase;
import com.williamcoelho.whatsapp.model.Conversa;
import com.williamcoelho.whatsapp.model.Grupo;
import com.williamcoelho.whatsapp.model.Mensagem;
import com.williamcoelho.whatsapp.model.Usuario;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView nomeContatoChat;
    private CircleImageView fotoContatoChat;
    private EditText editMensagem;
    private RecyclerView recyclerMensagens;
    private ImageView imageCamera;

    private boolean isGroup;

    private List<Mensagem> listaMensagens = new ArrayList<>();

    private Usuario usuarioDestinatario;
    private Usuario usuarioRemetente;

    private ChildEventListener childEventListenerMensagens;

    private DatabaseReference databaseReference;
    private DatabaseReference mensagens;
    private StorageReference storage;

    private AdapterMensagens adapterMensagens;

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    //Identificar remetente e destinatario
    private String idRemetente;
    private String idDestinatario;

    private Grupo grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //Configuracoes iniciais
        nomeContatoChat = findViewById(R.id.nomeContatoChat);
        fotoContatoChat = findViewById(R.id.fotoContatoChat);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        imageCamera = findViewById(R.id.imageCamera);

        //Recupera usuario remetente
        idRemetente = UsuarioFirebase.getIdUsuario();
        usuarioRemetente = UsuarioFirebase.getDadosUsuariosLogado();

        //Recupera dados do Usuario passado
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            if(bundle.containsKey("chatGrupo")){

                isGroup = true;

                grupo = (Grupo) bundle.getSerializable("chatGrupo");
                idDestinatario = grupo.getId();
                nomeContatoChat.setText(grupo.getNome());

                String foto = grupo.getFoto();

                if(foto != null){
                    Uri url = Uri.parse(foto);
                    Glide.with(ChatActivity.this).load(url).into(fotoContatoChat);
                }else{
                    fotoContatoChat.setImageResource(R.drawable.icone_grupo);
                }


            }
            if(bundle.containsKey("chatContato")){

                isGroup = false;

                usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
                nomeContatoChat.setText(usuarioDestinatario.getNome());

                String foto = usuarioDestinatario.getFoto();

                if(foto != null){
                    Uri url = Uri.parse(usuarioDestinatario.getFoto());
                    Glide.with(ChatActivity.this).load(url).into(fotoContatoChat);
                }else{
                    fotoContatoChat.setImageResource(R.drawable.padrao);
                }

                //Recupera usuario destinatario
                idDestinatario = Base64Custom.codificar(usuarioDestinatario.getEmail());

            }
        }

        //Configurar o adapter
        adapterMensagens = new AdapterMensagens(listaMensagens, getApplicationContext());

        //Configurar o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapterMensagens);

        //Configura destino no firebase para salvar conversas
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        mensagens = databaseReference.child("Mensagens").child(idRemetente).child(idDestinatario);

        //Configura destino no firebase para salvar imagens - storage
        storage = ConfiguracaoFirebase.getFirebaseStorage();

        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Criando interface para escolha de onde vai vir a imagem
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);

                builder.setTitle("Selecione a opção desejada:");
                builder.setMessage("Você pode escolher tirar uma foto agora ou escolher um da galeria para enviar.");
                builder.setPositiveButton("Tirar foto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(i.resolveActivity(getPackageManager()) != null){
                            startActivityForResult(i, SELECAO_CAMERA);
                        }

                    }
                });

                builder.setNegativeButton("Escolher da galeria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        if(i.resolveActivity(getPackageManager()) != null){
                            startActivityForResult(i, SELECAO_GALERIA);
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        recuperarMensagem();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            Bitmap imagem = null;


            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagem);
                        break;
                }

                if(imagem != null){

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Criar nome da imagem
                    String nomeImagem = UUID.randomUUID().toString();

                    //Configurar a referencia no firebase
                    final StorageReference imagemRef = storage.child("imagens").child("conversas").child(idRemetente).child(nomeImagem);

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, "Erro ao fazer upload", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Sucesso ao fazer upload
                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();

                                    Mensagem mensagem = new Mensagem();
                                    mensagem.setIdUsuario(idRemetente);
                                    mensagem.setConteudo("imagem.jpeg");
                                    mensagem.setImagem(url.toString());

                                    //Salvar para o remetente e na funcao chama para salvar para o destinatario
                                    salvarMensagem(idRemetente, idDestinatario, mensagem);

                                    salvarMensagemDestinatario(idDestinatario, idRemetente, mensagem);

                                }
                            });
                        }
                    });

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void enviarMensagem(View view){

        String conteudo = editMensagem.getText().toString();

        if(!conteudo.isEmpty()){

            if(!isGroup){
                Mensagem mensagem = new Mensagem();
                mensagem.setIdUsuario(idRemetente);
                mensagem.setConteudo(conteudo);

                salvarMensagem(idRemetente, idDestinatario, mensagem);
                salvarMensagemDestinatario(idDestinatario, idRemetente, mensagem);

                //salvar conversa para o remetente
                salvarConversa(idRemetente, idDestinatario, usuarioDestinatario, mensagem, false);

                //salvar conversa para o destinatario
                salvarConversa(idDestinatario, idRemetente, usuarioRemetente, mensagem, false);

                //Limpar caixa de texto
                editMensagem.setText("");
            }else{

                for(Usuario membro: grupo.getMembros()){

                    String idRemetenteGrupo = Base64Custom.codificar(membro.getEmail());
                    String idUsuarioLogadoGrupo = UsuarioFirebase.getIdUsuario();

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                    mensagem.setConteudo(conteudo);
                    mensagem.setNome(usuarioRemetente.getNome());

                    salvarMensagem(idRemetenteGrupo, idDestinatario, mensagem);

                    salvarConversa(idRemetenteGrupo, idDestinatario, usuarioDestinatario,  mensagem, true);

                    //Limpar caixa de texto
                    editMensagem.setText("");
                }

            }

        }else{
            Toast.makeText(ChatActivity.this, "Digite uma mensagem!", Toast.LENGTH_SHORT).show();
        }

    }

    private void salvarConversa(String remetente, String destinarario, Usuario usuarioExibicao, Mensagem mensagem, boolean ehGrupo){

        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(remetente);
        conversaRemetente.setIdDestinatario(destinarario);
        conversaRemetente.setUltimaMensagem(mensagem.getConteudo());

        if(ehGrupo){

            conversaRemetente.setEhGrupo("true");
            conversaRemetente.setGrupo(grupo);

        }else {

            conversaRemetente.setUsuarioExibicao(usuarioExibicao);
            conversaRemetente.setEhGrupo("false");

        }

        conversaRemetente.salvar();

    }

    private void salvarMensagem(String remetente, String destinarario, Mensagem mensagem){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference mensagemRef = databaseReference.child("Mensagens");

        mensagemRef.child(remetente).child(destinarario).push().setValue(mensagem);

    }

    private void salvarMensagemDestinatario(String remetente, String destinarario, Mensagem mensagem){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference mensagemRef = databaseReference.child("Mensagens");

        mensagemRef.child(remetente).child(destinarario).push().setValue(mensagem);

    }

    /*@Override
    protected void onStart() {
        super.onStart();
        recuperarMensagem();
    }*/

    /*@Override
    protected void onPause() {
        super.onPause();
        mensagens.removeEventListener(childEventListenerMensagens);
    }*/

    private void recuperarMensagem(){

        childEventListenerMensagens = mensagens.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mensagem msg = dataSnapshot.getValue(Mensagem.class);
                listaMensagens.add(msg);
                adapterMensagens.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
