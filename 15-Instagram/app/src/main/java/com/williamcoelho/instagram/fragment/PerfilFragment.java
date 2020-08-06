package com.williamcoelho.instagram.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.activity.EditarPerfilActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private TextView textQtdPublicacoes;
    private TextView textQtdSeguidores;
    private TextView textQtdSeguindo;
    private CircleImageView imagePerfil;
    private Button buttonEditarPerfil;
    private GridLayout gridViewPerfil;
    private ProgressBar progressBarPerfil;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Inicializar perfis
        textQtdPublicacoes = view.findViewById(R.id.textQTDPublicacoes);
        textQtdSeguidores = view.findViewById(R.id.textQtdSeguidores);
        textQtdSeguindo = view.findViewById(R.id.textQtdSeguindo);
        imagePerfil = view.findViewById(R.id.imagePerfil);
        buttonEditarPerfil = view.findViewById(R.id.buttonEditarPerfil);
        //gridViewPerfil = view.findViewById(R.id.gridViewPerfil);
        progressBarPerfil = view.findViewById(R.id.progressBarPerfil);

        abrirEdicaoPerfil(view);


        return view;
    }

    public void abrirEdicaoPerfil(View view){

        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(i);
            }
        });
    }
}
