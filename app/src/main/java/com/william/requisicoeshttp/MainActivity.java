package com.william.requisicoeshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.william.requisicoeshttp.api.CEPService;
import com.william.requisicoeshttp.api.DataService;
import com.william.requisicoeshttp.model.CEP;
import com.william.requisicoeshttp.model.Photo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textResultado;
    private Button buttonPesquisar;
    private Retrofit retrofit;
    private List<Photo> myPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textResultado = findViewById(R.id.textView);
        buttonPesquisar = findViewById(R.id.button);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        buttonPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getListRetrofit();
                getCepRetrofit();
//                MyTask task = new MyTask();
//                String urlApi = "https://viacep.com.br/ws/15370496/json/";
//                task.execute(urlApi);
            }
        });
    }

    private void getListRetrofit(){
        DataService dataService = retrofit.create(DataService.class);
        Call<List<Photo>> call = dataService.getPhotos();

        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                myPhotos = response.body();

                for(int i = 0; i < myPhotos.size(); i++){
                    Log.d("Resultado: ", "photo " + i + ": " + myPhotos.get(i));
                }

            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {

            }
        });
    }

    private void getCepRetrofit(){
        CEPService cepService = retrofit.create(CEPService.class);
        Call<CEP> call = cepService.getCEP("15370496");

        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                if(response.isSuccessful()){
                    CEP cep = response.body();
                    Log.d("aaaa", "entrou: " + cep);
                    textResultado.setText(cep.toString());
                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                Log.d("aaaa", "entrou fail: ");

            }
        });
    }


    // Fazendo tudo na mão
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