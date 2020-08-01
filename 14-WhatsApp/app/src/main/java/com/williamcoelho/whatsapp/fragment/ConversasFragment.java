package com.williamcoelho.whatsapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Config;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.williamcoelho.whatsapp.R;
import com.williamcoelho.whatsapp.activity.ChatActivity;
import com.williamcoelho.whatsapp.adapter.AdapterConversas;
import com.williamcoelho.whatsapp.adapter.AdapterMensagens;
import com.williamcoelho.whatsapp.config.ConfiguracaoFirebase;
import com.williamcoelho.whatsapp.helper.RecyclerItemClickListener;
import com.williamcoelho.whatsapp.helper.UsuarioFirebase;
import com.williamcoelho.whatsapp.model.Conversa;
import com.williamcoelho.whatsapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private RecyclerView recyclerConversas;

    private AdapterConversas adapterConversas;

    private List<Conversa> listaConversas = new ArrayList<>();

    private DatabaseReference databaseReference;
    private DatabaseReference conversasRef;

    private ChildEventListener childEventListenerConversas;

    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        //Configuracoes iniciais
        recyclerConversas = view.findViewById(R.id.recyclerConversas);

        //Configura adapter
        adapterConversas = new AdapterConversas(listaConversas, getActivity());

        //Configura recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerConversas.setLayoutManager(layoutManager);
        recyclerConversas.setHasFixedSize(true);
        recyclerConversas.setAdapter(adapterConversas);

        //Configura evento de click
        recyclerConversas.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerConversas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Conversa conversa = listaConversas.get(position);
                Intent i = new Intent(getActivity(), ChatActivity.class);
                i.putExtra("chatContato", conversa.getUsuarioExibicao());
                startActivity(i);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        //Recupera referencia de conversas no firebase
        String idUsuario = UsuarioFirebase.getIdUsuario();

        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        conversasRef = databaseReference.child("Conversas").child(idUsuario);

        recuperarConversas();

        return view;
    }

    public void pesquisarConversas(String texto){

        List<Conversa> listaConversasBusca = new ArrayList<>();
        String nome;
        String ultMensagem;

        for(Conversa conversa : listaConversas){
            nome = conversa.getUsuarioExibicao().getNome().toLowerCase();
            ultMensagem = conversa.getUltimaMensagem().toLowerCase();

            if(nome.contains(texto) || ultMensagem.contains(texto)){

                listaConversasBusca.add(conversa);

            }
        }

        adapterConversas = new AdapterConversas(listaConversasBusca, getActivity());
        recyclerConversas.setAdapter(adapterConversas);
        adapterConversas.notifyDataSetChanged();

    }

    public void recarregarConversas(){

        adapterConversas = new AdapterConversas(listaConversas, getActivity());
        recyclerConversas.setAdapter(adapterConversas);
        adapterConversas.notifyDataSetChanged();

    }

    public void recuperarConversas(){

        childEventListenerConversas = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Conversa conversa = dataSnapshot.getValue(Conversa.class);
                listaConversas.add(conversa);

                adapterConversas.notifyDataSetChanged();

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
