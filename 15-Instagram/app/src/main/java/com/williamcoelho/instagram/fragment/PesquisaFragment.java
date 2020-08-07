package com.williamcoelho.instagram.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.activity.PerfilTerceiroActivity;
import com.williamcoelho.instagram.adapter.AdapterPesquisa;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;
import com.williamcoelho.instagram.helper.RecyclerItemClickListener;
import com.williamcoelho.instagram.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisaFragment extends Fragment {

    private SearchView searchViewPesquisa;
    private RecyclerView recyclerViewPesquisa;

    private AdapterPesquisa adapterPesquisa;

    private List<Usuario> listaUsuarios;

    private DatabaseReference usuariosRef;

    public PesquisaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        //Configuracoes iniciais
        searchViewPesquisa = view.findViewById(R.id.searchViewPesquisa);
        recyclerViewPesquisa = view.findViewById(R.id.recyclerViewPesquisa);
        usuariosRef= ConfiguracaoFirebase.getDatabaseReference().child("Usuários");
        listaUsuarios = new ArrayList<>();

        //Configurar adapter
        adapterPesquisa =  new AdapterPesquisa(listaUsuarios, getActivity());

        //Configurar recyclerview
        recyclerViewPesquisa.setHasFixedSize(true);
        recyclerViewPesquisa.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPesquisa.setAdapter(adapterPesquisa);

        recyclerViewPesquisa.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewPesquisa, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Usuario usuarioSeleciondo = listaUsuarios.get(position);
                Intent i = new Intent(getActivity(), PerfilTerceiroActivity.class);
                i.putExtra("usuarioSelecionado", usuarioSeleciondo);
                startActivity(i);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        //Configurar o search view
        searchViewPesquisa.setQueryHint("Buscar usuários");
        searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String textoDigitado = newText.toUpperCase();
                pesquisarUsuarios(textoDigitado);

                return true;
            }
        });

        return view;
    }

    public void pesquisarUsuarios(String texto){

        listaUsuarios.clear();

        if(texto.length() > 0){

            Query query = usuariosRef.orderByChild("nome").startAt(texto).endAt(texto + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaUsuarios.clear();
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        listaUsuarios.add(ds.getValue(Usuario.class));

                    }

                    adapterPesquisa.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

}
