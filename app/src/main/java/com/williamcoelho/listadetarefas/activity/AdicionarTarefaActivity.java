package com.williamcoelho.listadetarefas.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputEditText;
import com.williamcoelho.listadetarefas.R;
import com.williamcoelho.listadetarefas.helper.TarefaDAO;
import com.williamcoelho.listadetarefas.model.Tarefa;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private TextInputEditText adicionarTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        adicionarTarefa = findViewById(R.id.adicionarTarefa);

        //Recuperar caso seja edicao
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        //Configurar a caixa de texto
        if(tarefaAtual != null){
            adicionarTarefa.setText(tarefaAtual.getNomeTarefa());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.itemSalvar:

                if(tarefaAtual != null){//Edicao
                    if(!adicionarTarefa.getText().toString().isEmpty()) {

                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(adicionarTarefa.getText().toString());
                        tarefa.setId(tarefaAtual.getId());

                        tarefaDAO.atualizar(tarefa);
                        finish();

                    }

                    }else {//salvar
                    if(!adicionarTarefa.getText().toString().isEmpty()){

                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                        Tarefa tarefa =  new Tarefa();
                        tarefa.setNomeTarefa(adicionarTarefa.getText().toString());
                        tarefaDAO.salvar(tarefa);

                        finish();

                    }
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
