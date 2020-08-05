package com.williamcoelho.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.williamcoelho.instagram.R;
import com.williamcoelho.instagram.helper.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configurar a toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Instagram");
        setSupportActionBar(toolbar);

        //Configuracoes dos objetos
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

    }

    //Cria o menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Trata o click no menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_sair:
                deslogadorUsuario();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogadorUsuario(){
        try{
            auth.signOut();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
