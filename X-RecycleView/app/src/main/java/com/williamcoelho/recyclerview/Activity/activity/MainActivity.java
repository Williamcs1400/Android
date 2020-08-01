package com.williamcoelho.recyclerview.Activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.williamcoelho.recyclerview.Activity.RecyclerItemClickListener;
import com.williamcoelho.recyclerview.Activity.adpter.Adapter;
import com.williamcoelho.recyclerview.Activity.model.Filme;
import com.williamcoelho.recyclerview.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Filme> listaFilmes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        //Criar listagem de filmes
        this.criarFilmes();

        //Configurar adpter
        Adapter adapter = new Adapter(listaFilmes);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        //Adicionar evento de click
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Filme filme = listaFilmes.get(position);
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Item pressionado: " + filme.getTitulo(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Filme filme = listaFilmes.get(position);
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Item longamente pressionado: " + filme.getTitulo(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }


    public void criarFilmes(){
        Filme filme = new Filme("Avengers: Endgame", "2019", "Aventura");
        this.listaFilmes.add(filme);

        filme = new Filme("Avatar", "2009", "Aventura");
        this.listaFilmes.add(filme);

        filme = new Filme("Titanic", "1997", "Romance");
        this.listaFilmes.add(filme);

        filme = new Filme("Star Wars: The Force Awakens", "2015", "Aventura");
        this.listaFilmes.add(filme);

        filme = new Filme("Avengers: Infinity War", "2018", "Aventura");
        this.listaFilmes.add(filme);

        filme = new Filme("Jurassic World", "2015", "Ação");
        this.listaFilmes.add(filme);

        filme = new Filme("The Lion King", "2019", "Aventura");
        this.listaFilmes.add(filme);

        filme = new Filme("Marvel's The Avengers", "2012", "Aventura");
        this.listaFilmes.add(filme);

        filme = new Filme("Furious 7", "2015", "Corrida");
        this.listaFilmes.add(filme);

        filme = new Filme("Frozen II", "2019", "Animação");
        this.listaFilmes.add(filme);
    }
}
