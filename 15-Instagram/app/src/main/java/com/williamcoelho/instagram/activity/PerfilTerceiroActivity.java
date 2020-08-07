package com.williamcoelho.instagram.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;
import com.williamcoelho.instagram.helper.UsuarioFirebase;
import com.williamcoelho.instagram.model.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilTerceiroActivity extends AppCompatActivity {

    private Button buttonAcaoPerfil;
    private CircleImageView imagePerfilTerceiro;
    private TextView seguidores;
    private TextView seguindo;
    private TextView publicacoes;

    private DatabaseReference usuarioRef;
    private DatabaseReference seguidoresRef;

    private String idUsuarioLogado;

    private ValueEventListener valueEventListener;

    private Usuario usuarioSelecionado;

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
        buttonAcaoPerfil.setText("Seguir");

        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        //Recuperar objeto passado
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            //Colocar o nome do usuario na toolbar
            toolbar.setTitle(usuarioSelecionado.getNome());

            exibirFoto();
        }

        verificaSegueTerceiro();
    }

    public void exibirFoto(){

        if(usuarioSelecionado.getLinkFoto() != null){

            Uri url = Uri.parse(usuarioSelecionado.getLinkFoto());
            Glide.with(getApplicationContext()).load(url).into(imagePerfilTerceiro);

        }else{
            imagePerfilTerceiro.setImageResource(R.drawable.avatar);
        }
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

        seguidoresRef = ConfiguracaoFirebase.getDatabaseReference().child("Seguidores").child(idUsuarioLogado).child(usuarioSelecionado.getIdUsuario());

        seguidoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    //Ja estah sendo seguido
                    habilitarBotaoSeguir(true);

                }else{
                    //Nao estah sendo seguido
                    habilitarBotaoSeguir(false);
                    buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void salvarSeguidor(){

    }

    private void habilitarBotaoSeguir(boolean seguindo){

        if(seguindo){
            buttonAcaoPerfil.setText("Seguindo");
        }else{
            buttonAcaoPerfil.setText("Seguir");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosTerceiro();
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
