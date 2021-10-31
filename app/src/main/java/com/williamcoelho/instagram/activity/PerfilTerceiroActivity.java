package com.williamcoelho.instagram.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.adapter.AdapterGrid;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;
import com.williamcoelho.instagram.helper.UsuarioFirebase;
import com.williamcoelho.instagram.model.Postagem;
import com.williamcoelho.instagram.model.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilTerceiroActivity extends AppCompatActivity {

    private Button buttonAcaoPerfil;
    private CircleImageView imagePerfilTerceiro;
    private TextView seguidores;
    private TextView seguindo;
    private TextView publicacoes;
    private GridView gridView;
    private AdapterGrid adapterGrid;

    private DatabaseReference usuarioRef;
    private DatabaseReference seguidoresRef;
    private DatabaseReference postagemUsuarioRef;

    private String idUsuarioLogado;

    private List<Postagem> postagens;

    private ValueEventListener valueEventListener;

    private Usuario usuarioSelecionado;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_terceiro);

        //Configurar a toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        //Inicializar componentes
        buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);
        imagePerfilTerceiro = findViewById(R.id.imagePerfil);
        seguidores = findViewById(R.id.textQtdSeguidores);
        seguindo= findViewById(R.id.textQtdSeguindo);
        publicacoes = findViewById(R.id.textQTDPublicacoes);
        gridView = findViewById(R.id.gridViewPerfil);
        buttonAcaoPerfil.setText("Carregando");

        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        //Recuperar objeto passado
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            postagemUsuarioRef = ConfiguracaoFirebase.getDatabaseReference().child("Postagens").child(usuarioSelecionado.getIdUsuario());

            //Colocar o nome do usuario na toolbar
            toolbar.setTitle(usuarioSelecionado.getNome());

            exibirFoto();
        }

        iniciarImageLoader();

        carregarPostagens();

        //Abre a foto clicada
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Postagem postagem = postagens.get(position);

                Intent i = new Intent(getApplicationContext(), VisualizarPostagemActivity.class);
                i.putExtra("postagem", postagem);
                i.putExtra("usuarioSelecionado", usuarioSelecionado);
                startActivity(i);

            }
        });

    }

    public void exibirFoto(){

        if(usuarioSelecionado.getLinkFoto() != null){

            Uri url = Uri.parse(usuarioSelecionado.getLinkFoto());
            Glide.with(getApplicationContext()).load(url).into(imagePerfilTerceiro);

        }else{
            imagePerfilTerceiro.setImageResource(R.drawable.avatar);
        }
    }

    public void iniciarImageLoader(){

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100).build();

        ImageLoader.getInstance().init(config);

    }

    public void carregarPostagens(){

        //Recupera as fotos postadas pelo usuario
        postagens = new ArrayList<>();
        postagemUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
                int tamanhoImagem = tamanhoGrid / 3;
                gridView.setColumnWidth(tamanhoImagem);

                List<String> urlFotos =  new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Postagem postagem = ds.getValue(Postagem.class);
                    postagens.add(postagem);
                    urlFotos.add(postagem.getCaminhoFoto());

                }

                publicacoes.setText(String.valueOf(urlFotos.size()));

                //Configurar adapter
                adapterGrid = new AdapterGrid(getApplicationContext(), R.layout.grid_postagem, urlFotos);

                gridView.setAdapter(adapterGrid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void recuperarDadosTerceiro(){

        usuarioRef = ConfiguracaoFirebase.getDatabaseReference().child("Usuários").child(usuarioSelecionado.getIdUsuario());
        valueEventListener = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                String strSeguidores = String.valueOf(usuario.getSeguidores());
                String strSeguindo = String.valueOf(usuario.getSeguindo());
                String strPublicacoes = String.valueOf(usuario.getPublicacoes());

                seguidores.setText(strSeguidores);
                seguindo.setText(strSeguindo);
                publicacoes.setText(strPublicacoes);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void verificaSegueTerceiro(){

        seguidoresRef = ConfiguracaoFirebase.getDatabaseReference().child("Seguidores").child(usuarioSelecionado.getIdUsuario()).child(idUsuarioLogado);

        seguidoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    //Ja estah sendo seguido
                    habilitarBotaoSeguir(true);

                }else{
                    //Nao estah sendo seguido
                    habilitarBotaoSeguir(false);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void recuperarDadosUsuarioLogado(){

        DatabaseReference usuarioLogadoRef = ConfiguracaoFirebase.getDatabaseReference().child("Usuários").child(idUsuarioLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usuarioLogado = dataSnapshot.getValue(Usuario.class);
                verificaSegueTerceiro();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void habilitarBotaoSeguir(boolean seguindo) {

        if (seguindo) {
            buttonAcaoPerfil.setText("Seguindo");
        } else {
            buttonAcaoPerfil.setText("Seguir");
            buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    salvarSeguidor();
                }
            });
        }
    }

    private void salvarSeguidor(){

        HashMap<String, Object> dadosULogado= new HashMap<>();
        dadosULogado.put("nome", usuarioLogado.getNome());
        dadosULogado.put("linkFoto", usuarioLogado.getLinkFoto());

        DatabaseReference seguidorRef = ConfiguracaoFirebase.getDatabaseReference().child("Seguidores")
                .child(usuarioSelecionado.getIdUsuario()).child(usuarioLogado.getIdUsuario());

        seguidorRef.setValue(dadosULogado);

        buttonAcaoPerfil.setText("Seguindo");
        buttonAcaoPerfil.setOnClickListener(null);

        //Incrementar dados do perfil logado
        int seguindo = usuarioLogado.getSeguindo() + 1;
        HashMap<String, Object> dadosSeguindo = new HashMap<>();
        dadosSeguindo.put("seguindo", seguindo);
        DatabaseReference attUsuario = ConfiguracaoFirebase.getDatabaseReference().child("Usuários").child(usuarioLogado.getIdUsuario());
        attUsuario.updateChildren(dadosSeguindo);

        //Incrementar dados do perfil terceiro
        int seguidores = usuarioSelecionado.getSeguidores() + 1;
        HashMap<String, Object> dadosSeguidores = new HashMap<>();
        dadosSeguidores.put("seguidores", seguidores);
        DatabaseReference attTerceiro = ConfiguracaoFirebase.getDatabaseReference().child("Usuários").child(usuarioSelecionado.getIdUsuario());
        attTerceiro.updateChildren(dadosSeguidores);
    }


    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosTerceiro();
        recuperarDadosUsuarioLogado();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
