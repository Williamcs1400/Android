package com.williamcoelho.clculodegorjeta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity<inputPreco> extends AppCompatActivity {

    private TextInputEditText inputPreco;
    private SeekBar seekBarPorcentagem;
    private TextView textPorcentagem, textGorjeta, textTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputPreco = findViewById(R.id.inputPreco);
        seekBarPorcentagem = findViewById(R.id.seekBarPorcentagem);
        textPorcentagem = findViewById(R.id.textPorcentagem);
        textGorjeta = findViewById(R.id.textGorjeta);
        textTotal = findViewById(R.id.textTotal);

        seekBarPorcentagem.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textPorcentagem.setText(progress + "%");

                calcularTudo();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void calcularTudo(){

        String auxInput = inputPreco.getText().toString();

        if(auxInput == null || auxInput.equals("")){    //Verifica se foi digitado algo

            Toast.makeText(
                    getApplicationContext(),
                    "Digite um valor primeiro",
                    Toast.LENGTH_SHORT
            ).show();

        }else{

            double valorInput = Double.parseDouble(auxInput);  //Converte o valor digitado em double

            double auxGorjeta = (valorInput * seekBarPorcentagem.getProgress()) / 100;

            textGorjeta.setText("R$: " + Math.round((valorInput * seekBarPorcentagem.getProgress()) / 100));

            textTotal.setText("R$: " + Math.round(valorInput + auxGorjeta));
        }
    }
}

