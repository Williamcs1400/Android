package com.williamcoelho.pedrapapeloutesoura;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pedraSelecionada(View view){
        this.opcaoSelecionada("pedra");
    }

    public void papelSelecionado(View view){
        this.opcaoSelecionada("papel");
    }

    public void tesouraSelecionada(View view){
        this.opcaoSelecionada("tesoura");
    }


    public void opcaoSelecionada(String escolhaUsuario){

        ImageView imagemResultado = findViewById(R.id.imageResultado);
        TextView mostraResultado = findViewById(R.id.TextEscolha);

        int num = new Random().nextInt(3);
        String opcoes[] = {"pedra", "papel", "tesoura"};
        String opcaoApp = opcoes[num];

        switch(opcaoApp){
            case "pedra":
                imagemResultado.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imagemResultado.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imagemResultado.setImageResource(R.drawable.tesoura);
                break;
        }

        if((opcaoApp == "pedra" && escolhaUsuario == "tesoura") ||
                (opcaoApp == "papel" && escolhaUsuario == "pedra") ||
                (opcaoApp == "tesoura" && escolhaUsuario == "papel")){   //AppGanhador

            mostraResultado.setText("Você perdeu :(");

        }else if((escolhaUsuario == "pedra" && opcaoApp == "tesoura") ||
                (escolhaUsuario == "papel" && opcaoApp == "pedra") ||
                (escolhaUsuario == "tesoura" && opcaoApp == "papel")){     //UsuarioGanhador

            mostraResultado.setText("Você ganhou :)");

        }else   //Empate
            mostraResultado.setText("Empate :|");
    }
}
