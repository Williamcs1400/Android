package com.williamcoelho.whatsapp.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.williamcoelho.whatsapp.R;
import com.williamcoelho.whatsapp.adapter.AdapterContatos;
import com.williamcoelho.whatsapp.adapter.AdapterGrupoSelecionado;
import com.williamcoelho.whatsapp.config.ConfiguracaoFirebase;
import com.williamcoelho.whatsapp.helper.RecyclerItemClickListener;
import com.williamcoelho.whatsapp.helper.UsuarioFirebase;
import com.williamcoelho.whatsapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GrupoActivity extends AppCompatActivity {

    private RecyclerView recyclerMembros;
    private RecyclerView recyclerMembrosSelecionados;

    private AdapterContatos adapterContatos;
    private AdapterGrupoSelecionado adapterGrupoSelecionado;

    private List<Usuario> listaMembros = new ArrayList<>();
    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();

    private int qtdSelecionados;
    private int qtdTotal;

    private DatabaseReference contatosRef;

    private FirebaseUser userAtual;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Novo grupo");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Configuracoes iniciais
        recyclerMembros = findViewById(R.id.recyclerMembros);
        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosSelecionados);

        contatosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("Usuários");
        userAtual = UsuarioFirebase.getUsuarioAtual();

        //Configura o adapter
        adapterContatos = new AdapterContatos(listaMembros, getApplicationContext());

        //Configura o recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMembros.setLayoutManager(layoutManager);
        recyclerMembros.setHasFixedSize(true);
        recyclerMembros.setAdapter(adapterContatos);

        recuperarContatos();

        recyclerMembros.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerMembros,
                        new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Usuario usuarioSelecionado = listaMembros.get(position);

                //remover o usuario selecionado
                listaMembros.remove(usuarioSelecionado);
                adapterContatos.notifyDataSetChanged();

                //Adiciona na lista de selecionados
                listaMembrosSelecionados.add(usuarioSelecionado);
                adapterGrupoSelecionado.notifyDataSetChanged();

                atualizarParticipantesToolbar();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        //Configura o adapter dos membros selecionados
        adapterGrupoSelecionado = new AdapterGrupoSelecionado(listaMembrosSelecionados, getApplicationContext());

        //Configura o recycler view dos membros selecionados
        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembrosSelecionados.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter(adapterGrupoSelecionado);

        recyclerMembrosSelecionados.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerMembrosSelecionados, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Usuario usuarioSelecionado = listaMembrosSelecionados.get(position);

                //Remover da lista
                listaMembrosSelecionados.remove(usuarioSelecionado);
                adapterGrupoSelecionado.notifyDataSetChanged();

                //Colocar na outra lista
                listaMembros.add(usuarioSelecionado);
                adapterContatos.notifyDataSetChanged();

                atualizarParticipantesToolbar();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    public void recuperarContatos(){

        contatosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dados: dataSnapshot.getChildren()){

                    Usuario usuario = dados.getValue(Usuario.class);
                    if(!userAtual.getEmail().equals(usuario.getEmail())){
                        listaMembros.add(usuario);
                    }

                }

                adapterContatos.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarParticipantesToolbar(){

        qtdSelecionados = listaMembrosSelecionados.size();
        qtdTotal = listaMembros.size() + qtdSelecionados;

        toolbar.setSubtitle(qtdSelecionados + " de " + qtdTotal + " selecionados");

    }

}
