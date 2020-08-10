package com.williamcoelho.instagram.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.adapter.AdapterMiniaturas;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;
import com.williamcoelho.instagram.helper.RecyclerItemClickListener;
import com.williamcoelho.instagram.helper.UsuarioFirebase;
import com.williamcoelho.instagram.model.Postagem;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FiltrosActivity extends AppCompatActivity {

    //Inicializa a biblioteca de filtros
    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    private ImageView imageEscolhida;
    private RecyclerView recyclerFiltros;
    private EditText editDescricao;
    private AdapterMiniaturas adapterMiniaturas;

    private Bitmap imagem;
    private Bitmap imagemFiltro;

    private List<ThumbnailItem> listaFiltro;

    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        //Configurar a toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Filtros");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        //Inicializada componentes
        imageEscolhida = findViewById(R.id.imageFotoEscolhida);
        recyclerFiltros = findViewById(R.id.recyclerFiltros);
        editDescricao = findViewById(R.id.editDescricao);
        listaFiltro = new ArrayList<>();
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        //Recupera imagem Escolhida pelo usuario
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            byte[] dadosImagem = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dadosImagem, 0, dadosImagem.length);
            imageEscolhida.setImageBitmap(imagem);
            imagemFiltro = imagem.copy(imagem.getConfig(), true);

            adapterMiniaturas =  new AdapterMiniaturas(listaFiltro, getApplicationContext());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerFiltros.setLayoutManager(layoutManager);
            recyclerFiltros.setAdapter(adapterMiniaturas);

            //Evento de click na lista de filtros
            recyclerFiltros.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerFiltros, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    ThumbnailItem item = listaFiltro.get(position);

                    imagemFiltro = imagem.copy(imagem.getConfig(), true);
                    Filter filter = item.filter;
                    imageEscolhida.setImageBitmap(filter.processFilter(imagemFiltro));

                }

                @Override
                public void onLongItemClick(View view, int position) {

                }

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            }));

            recuperarListaFiltros();

        }

    }

    public void recuperarListaFiltros(){

        ThumbnailsManager.clearThumbs();
        listaFiltro.clear();

        //Configura filtro normal
        ThumbnailItem item = new ThumbnailItem();
        item.image = imagem;
        item.filterName = "Normal";
        ThumbnailsManager.addThumb(item);

        //Listar todos os filtros
        List<Filter> filters = FilterPack.getFilterPack(getApplicationContext());
        for(Filter filter: filters){
            ThumbnailItem itemFiltro = new ThumbnailItem();
            itemFiltro.image = imagem;
            itemFiltro.filter = filter;
            itemFiltro.filterName = filter.getName();

            ThumbnailsManager.addThumb(itemFiltro);

        }

        listaFiltro.addAll(ThumbnailsManager.processThumbs(getApplicationContext()));
        adapterMiniaturas.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtro, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void publicarPostagem(){

        final Postagem postagem = new Postagem();
        postagem.setIdUsuario(idUsuarioLogado);
        postagem.setDescricao(editDescricao.getText().toString());

        //Salvar no storage
        //Converter em um byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagemFiltro.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] dadosImagem = baos.toByteArray();

        StorageReference reference = ConfiguracaoFirebase.getStorageReference();
        final StorageReference imagemRef = reference.child("imagens").child("postagens").child(postagem.getIdPost() + "jpeg");

        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Erro ao fazer upload da imagem!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        postagem.setCaminhoFoto(url.toString());

                        if(postagem.salvar()){
                            Toast.makeText(getApplicationContext(), "Sucesso ao salvar a imagem!", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.salvarPostagem:
                publicarPostagem();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
