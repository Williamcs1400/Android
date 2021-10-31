package com.williamcoelho.frasedodia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gerarNovaFrase(View view){

        String[] frases = {
                "Não importa a cor do céu. Quem faz o dia bonito é você.",
                "Enquanto o dia semeia sonhos, a noite rega a esperança.",
                "Quanto mais agradecemos, mais coisas boas acontecem.",
                "Não coloque limites em seus sonhos, coloque fé.",
                "A vida me ensinou que chorar alivia, mas sorrir torna tudo mais bonito.",
                "As melhores coisas da vida não são coisas.",
                "Nem tudo na vida são flores, mas quando for, regue",
                "Nada é em vão. Se não é bênção, é lição.",
                "Assim como a lua, a vida tem fases.",
                "Nunca desista daquilo que te faz sorrir.",
                "Bendito seja o sorriso nosso de cada dia.",
                "O amor não faz o mundo girar. O amor é o que faz o giro valer a pena.",
                "Hoje é um dia perfeito para ser feliz.",
                "Bendita seja a verdade dita, o abraço sincero e o amor recíproco."
        };
        int numero = new Random().nextInt(13);

        TextView texto = findViewById(R.id.mostra_frase);
        texto.setText(frases[numero]);
    }
}
