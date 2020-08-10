package com.williamcoelho.instagram.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.williamcoelho.instagram.R;

public class FiltrosActivity extends AppCompatActivity {

    private ImageView imageEscolhida;
    private Bitmap imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        //Inicializada componentes
        imageEscolhida = findViewById(R.id.imageFotoEscolhida);

        //Recupera imagem Escolhida pelo usuario
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            byte[] dadosImagem = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dadosImagem, 0, dadosImagem.length);
            imageEscolhida.setImageBitmap(imagem);
        }

    }
}
