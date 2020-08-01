package com.williamcoelho.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.williamcoelho.organizze.R;
import com.williamcoelho.organizze.config.ConfiguracaoFirebase;
import com.williamcoelho.organizze.helper.Base64Custom;
import com.williamcoelho.organizze.helper.DateCustom;
import com.williamcoelho.organizze.model.Movimentacao;
import com.williamcoelho.organizze.model.Usuario;

public class DespesasActivity extends AppCompatActivity {

    private EditText campoValor;
    private TextInputEditText campoData, campoOrigem, campoDescricao;
    private Movimentacao movimentacao;
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double despesaTotal;
    private Double despesaGerada;
    private Double despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoOrigem = findViewById(R.id.editOrigem);
        campoDescricao = findViewById(R.id.editDescricao);

        //Set data com base na data atual
        campoData.setText(DateCustom.dataAtual());

        recuperaDespesaTotal();

    }

    public void salvarDespesa(View view){

        if(validarCampos()){

            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setData(data);
            movimentacao.setCategoria(campoOrigem.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setTipo("d"); //D de despesa
            movimentacao.setValor(valorRecuperado);

            despesaGerada = valorRecuperado;
            despesaAtualizada = despesaTotal + despesaGerada;
            atualizarDespesa();

            movimentacao.salvar(data);

            finish();

        }



    }

    public Boolean validarCampos(){

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoOrigem = campoOrigem.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if(!textoValor.isEmpty()){
            if(!textoData.isEmpty()){
                if(!textoOrigem.isEmpty()){
                    if(!textoDescricao.isEmpty()) {

                        return true;

                    }else{
                        Toast.makeText(DespesasActivity.this,
                                "Descrição não foi preenchida",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }


                }else{
                    Toast.makeText(DespesasActivity.this,
                            "Origem não foi preenchida",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }


            }else{
                Toast.makeText(DespesasActivity.this,
                        "Data não foi preenchida",
                        Toast.LENGTH_SHORT).show();
                return false;
            }


        }else{
            Toast.makeText(DespesasActivity.this,
                            "Valor não foi preenchido",
                            Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperaDespesaTotal(){

        String emailAuth = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailAuth);
        DatabaseReference usuarioRef = databaseReference.child("Usuários").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void atualizarDespesa(){

        String emailAuth = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailAuth);
        DatabaseReference usuarioRef = databaseReference.child("Usuários").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesaAtualizada);

    }
}
