package com.williamcoelho.instagram.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.model.Postagem;
import com.williamcoelho.instagram.model.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisualizarPostagemActivity extends AppCompatActivity {

    private TextView textNome;
    private TextView textCurtidas;
    private TextView textDescricao;
    private TextView textComentarios;
    private CircleImageView imagemPerfil;
    private ImageView imagemPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_postagem);

        //Configurar a toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Visualizar postagem");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        //**********************//
        inicializarComponentes();

        //Recupera dados da activity
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            Postagem postagem = (Postagem) bundle.getSerializable("postagem");
            Usuario usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            //Exibir dados do usuario
            Uri uriUsuario = Uri.parse(usuarioSelecionado.getLinkFoto());
            Glide.with(VisualizarPostagemActivity.this).load(uriUsuario).into(imagemPerfil);

            textNome.setText(usuarioSelecionado.getNome());

            //Exibir dados da postagem
            Uri uriPostagem = Uri.parse(postagem.getCaminhoFoto());
            Glide.with(VisualizarPostagemActivity.this).load(uriPostagem).into(imagemPrincipal);
            textDescricao.setText(postagem.getDescricao());

        }

    }

    public void inicializarComponentes(){

        textNome = findViewById(R.id.textNomeVisualizacao);
        textCurtidas = findViewById(R.id.textCurtidas);
        textDescricao = findViewById(R.id.textDescricaoVisualizar);
        textComentarios = findViewById(R.id.textComentariosVisualizar);
        imagemPerfil = findViewById(R.id.imagePerfilVisualizacao);
        imagemPrincipal = findViewById(R.id.imagePrincipalVisualizacao);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
