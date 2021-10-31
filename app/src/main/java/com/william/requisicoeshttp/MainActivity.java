package com.william.requisicoeshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView textResultado;
    private Button buttonPesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textResultado = findViewById(R.id.textView);
        buttonPesquisar = findViewById(R.id.button);

        buttonPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTask task = new MyTask();
                String urlApi = "https://viacep.com.br/ws/15370496/json/";
                task.execute(urlApi);
            }
        });
    }

    class MyTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl =  strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer stringBuffer = new StringBuffer();

            try {

                URL url = new URL(stringUrl);
                HttpURLConnection conect = (HttpURLConnection) url.openConnection();

                inputStream = conect.getInputStream();
                inputStreamReader =  new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                Log.i("teste", "reader: " + reader);
                String line = "";
                while((line = reader.readLine()) != null){
                    stringBuffer.append(line);
                    Log.i("teste", line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("teste", "MalformedURLException");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("teste", "IOException");
            }

            return stringBuffer.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String logradouro = null;
            String complemento = null;

            try {
                JSONObject jsonCEP = new JSONObject(s);
                logradouro = jsonCEP.getString("logradouro");
                complemento = jsonCEP.getString("complemento");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            textResultado.setText(s);
        }
    }

}