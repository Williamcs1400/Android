package com.williamcoelho.lcoolougasolina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editPrecoAlcool, editPrecoGasolina;
    private TextView textResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    editPrecoAlcool = findViewById(R.id.caixaAlcool);
    editPrecoGasolina = findViewById(R.id.caixaGasolina);
    textResultado = findViewById(R.id.textResult);

    }

    public void calcularPreco(View view){

        //Recuperar os valores digitados
        String precoAlcool = editPrecoAlcool.getText().toString();
        String precoGasolina = editPrecoGasolina.getText().toString();

        //Verificar se foram digitados valores
        if(validarCampos(precoAlcool, precoGasolina)) {

            //Convertes para numero
            Double valorAlcool = Double.parseDouble(precoAlcool);
            Double valorGasolina = Double.parseDouble(precoGasolina);

            if((valorAlcool/valorAlcool) < 0.7){
                textResultado.setText("É melhor ir de álcool!");
            }else{
                textResultado.setText("É melhor ir de gasolina!");
            }


        }else{
            textResultado.setText("Preencha todos os campos!");
        }



    }

    public boolean validarCampos(String pAlcool, String pGasolina){

        boolean resposta = true;

        if(pAlcool == null || pAlcool.equals("")){
            resposta = false;
        }
        else if(pGasolina == null || pAlcool.equals("")){
            resposta = false;
        }

        return resposta;

    }


}
