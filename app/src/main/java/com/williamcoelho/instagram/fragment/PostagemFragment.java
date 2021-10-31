package com.williamcoelho.instagram.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.activity.FiltrosActivity;
import com.williamcoelho.instagram.helper.Permissao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostagemFragment extends Fragment {

    private Button buttonCamera;
    private Button buttonGaleria;

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    private String[] permissoesNecessarias = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    public PostagemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_postagem, container, false);

        //Validar permissoes
        Permissao.validarPermissoes(permissoesNecessarias, getActivity(), 1);

        //Configuracoes iniciais
        buttonCamera = view.findViewById(R.id.buttonCamera);
        buttonGaleria = view.findViewById(R.id.buttonGaleria);

        criaListener(view);

        return view;
    }

    public void criaListener(View view){

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });

        buttonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK){
            Bitmap imagem = null;

            switch(requestCode){
                case SELECAO_CAMERA:
                    imagem = (Bitmap) data.getExtras().get("data");
                    break;
                case SELECAO_GALERIA:
                    Uri localImagemSelecionada = data.getData();
                    try {
                        imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            if(imagem != null){

                //Converter em um byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] dadosImagem = baos.toByteArray();

                //Envia imagem para a tela de aplicacao de filtro
                Intent i = new Intent(getContext(), FiltrosActivity.class);
                i.putExtra("fotoEscolhida", dadosImagem);
                startActivity(i);
            }

        }

    }
}
