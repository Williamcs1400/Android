package com.williamcoelho.cardview.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.williamcoelho.cardview.R;
import com.williamcoelho.cardview.adapter.Adapter;
import com.williamcoelho.cardview.model.Postagens;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerPostagem;
    private List<Postagens> postagem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerPostagem = findViewById(R.id.recyclerPostagem);

        //Define layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerPostagem.setLayoutManager( layoutManager );

        //Define adapter
        this.PrepararPostagens();

        Adapter adapter = new Adapter(postagem);
        recyclerPostagem.setAdapter( adapter );
    }

    public void PrepararPostagens(){

        Postagens p = new Postagens("Cristo Redentor", "12/10/1931", "Rio de Janeiro - Brasil", R.drawable.cristo);
        this.postagem.add(p);

        p = new Postagens("Muralha da China", "220 - 206 a.C.", "Norte da China", R.drawable.muralha);
        this.postagem.add(p);

        p = new Postagens("Machu Picchu", "Século XV", "Cordilheira dos Andes - Peru", R.drawable.machupichu);
        this.postagem.add(p);

        p = new Postagens("Taj Mahal", "1653", "Agra - Índia", R.drawable.tajmahal);
        this.postagem.add(p);

        p = new Postagens("Coliseu", "80", "Roma - Ítalia", R.drawable.coliseu);
        this.postagem.add(p);

        p = new Postagens("Petra", "312 - 62 a.C.", "Petra - Jordânia", R.drawable.petra);
        this.postagem.add(p);

        p = new Postagens("Chichén Itzá", "600 - 1200", "Chichén Itzá - México", R.drawable.chichenitza);
        this.postagem.add(p);
    }
}
