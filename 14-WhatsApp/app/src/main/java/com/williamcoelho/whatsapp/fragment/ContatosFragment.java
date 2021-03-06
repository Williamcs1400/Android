package com.williamcoelho.whatsapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.williamcoelho.whatsapp.R;
import com.williamcoelho.whatsapp.activity.ChatActivity;
import com.williamcoelho.whatsapp.activity.GrupoActivity;
import com.williamcoelho.whatsapp.adapter.AdapterContatos;
import com.williamcoelho.whatsapp.config.ConfiguracaoFirebase;
import com.williamcoelho.whatsapp.helper.RecyclerItemClickListener;
import com.williamcoelho.whatsapp.helper.UsuarioFirebase;
import com.williamcoelho.whatsapp.model.Usuario;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private DatabaseReference contatosRef;

    private RecyclerView recyclerViewListaContatos;

    private AdapterContatos adapterContatos;

    private ArrayList<Usuario> listaContatos = new ArrayList<>();

    private FirebaseUser userAtual;

    private ValueEventListener valueEventListener;

    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaContatos);
        contatosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("Usuários");
        userAtual = UsuarioFirebase.getUsuarioAtual();

        //Configurar o adapter
        adapterContatos = new AdapterContatos(listaContatos, getActivity());

        //Configurar o recyclerView

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaContatos.setLayoutManager(layoutManager);
        recyclerViewListaContatos.setHasFixedSize(true);
        recyclerViewListaContatos.setAdapter(adapterContatos);

        recyclerViewListaContatos.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), recyclerViewListaContatos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Usuario usuarioSelecionado = listaContatos.get(position);
                boolean cabecalho = usuarioSelecionado.getEmail().isEmpty();

                if(cabecalho){

                    Intent i = new Intent(getActivity(), GrupoActivity.class);
                    startActivity(i);

                }else{
                    Intent i = new Intent(getActivity(), ChatActivity.class);
                    i.putExtra("chatContato", usuarioSelecionado);
                    startActivity(i);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }
        ));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        contatosRef.removeEventListener(valueEventListener);
    }

    public void recuperarContatos(){

        valueEventListener = contatosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                limparListaContatos();

                for(DataSnapshot dados: dataSnapshot.getChildren()){

                    Usuario usuario = dados.getValue(Usuario.class);
                    if(!userAtual.getEmail().equals(usuario.getEmail())){
                        listaContatos.add(usuario);
                    }

                }

                adapterContatos.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void limparListaContatos(){

        listaContatos.clear();
        adicionarNovoGrupo();
    }

    public void adicionarNovoGrupo(){
        Usuario itemGrupo = new Usuario();
        itemGrupo.setNome("Novo grupo");
        itemGrupo.setEmail("");

        listaContatos.add(itemGrupo);
    }

}
