package com.williamcoelho.instagram.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.activity.EditarPerfilActivity;
import com.williamcoelho.instagram.adapter.AdapterGrid;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;
import com.williamcoelho.instagram.helper.UsuarioFirebase;
import com.williamcoelho.instagram.model.Postagem;
import com.williamcoelho.instagram.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private TextView textQtdPublicacoes;
    private TextView textQtdSeguidores;
    private TextView textQtdSeguindo;
    private CircleImageView imagePerfil;
    private Button buttonAcaoPerfil;
    private GridView gridViewPerfil;
    private ProgressBar progressBarPerfil;

    private AdapterGrid adapterGrid;

    private DatabaseReference usuarioRef;
    private DatabaseReference postagemUsuarioRef;
    private ValueEventListener valueEventListener;

    private Usuario usuarioLogado;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Configura toolbar
        //Toolbar toolbar = view.findViewById(R.id.toolbar);
        //toolbar.setTitle("Perfil");

        //Inicializar perfis
        textQtdPublicacoes = view.findViewById(R.id.textQTDPublicacoes);
        textQtdSeguidores = view.findViewById(R.id.textQtdSeguidores);
        textQtdSeguindo = view.findViewById(R.id.textQtdSeguindo);
        imagePerfil = view.findViewById(R.id.imagePerfil);
        buttonAcaoPerfil = view.findViewById(R.id.buttonAcaoPerfil);
        gridViewPerfil = view.findViewById(R.id.gridViewPerfil);
        progressBarPerfil = view.findViewById(R.id.progressBarPerfil);

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        postagemUsuarioRef = ConfiguracaoFirebase.getDatabaseReference().child("Postagens").child(usuarioLogado.getIdUsuario());

        abrirEdicaoPerfil(view);

        //Configuracao de nome e foto
        //toolbar.setTitle(usuarioLogado.getNome());
        exibirFoto();

        iniciarImageLoader();

        carregarPostagens();

        return view;
    }

    public void exibirFoto(){

        if(usuarioLogado.getLinkFoto() != null){

            Uri url = Uri.parse(usuarioLogado.getLinkFoto());
            Glide.with(getActivity()).load(url).into(imagePerfil);

        }else{
            imagePerfil.setImageResource(R.drawable.avatar);
        }
    }

    public void iniciarImageLoader(){

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100).build();

        ImageLoader.getInstance().init(config);

    }

    public void carregarPostagens(){

        //Recupera as fotos postadas pelo usuario
        postagemUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
                int tamanhoImagem = tamanhoGrid / 3;
                gridViewPerfil.setColumnWidth(tamanhoImagem);

                List<String> urlFotos =  new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Postagem postagem = ds.getValue(Postagem.class);
                    urlFotos.add(postagem.getCaminhoFoto());

                }

                textQtdPublicacoes.setText(String.valueOf(urlFotos.size()));

                //Configurar adapter
                adapterGrid = new AdapterGrid(getActivity(), R.layout.grid_postagem, urlFotos);
                gridViewPerfil.setAdapter(adapterGrid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void recuperarDados(){

        usuarioRef = ConfiguracaoFirebase.getDatabaseReference().child("Usuários").child(usuarioLogado.getIdUsuario());
        valueEventListener = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                String strSeguidores = String.valueOf(usuario.getSeguidores());
                String strSeguindo = String.valueOf(usuario.getSeguindo());
                String strPublicacoes = String.valueOf(usuario.getPublicacoes());

                textQtdSeguidores.setText(strSeguidores);
                textQtdSeguindo.setText(strSeguindo);
                //textQtdPublicacoes.setText(strPublicacoes);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void abrirEdicaoPerfil(View view){

        buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarDados();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListener);
    }
}
