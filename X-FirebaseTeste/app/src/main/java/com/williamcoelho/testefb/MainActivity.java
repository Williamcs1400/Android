package com.williamcoelho.testefb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    /*private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth usuario = FirebaseAuth.getInstance();*/

    private ImageView imageTeste;
    private Button buttonUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageTeste = findViewById(R.id.imageTeste);
        buttonUpload = findViewById(R.id.buttonUpload);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Configura imagem para serem salvas na memoria
                imageTeste.setDrawingCacheEnabled(true);
                imageTeste.buildDrawingCache();

                //Recupera o bitmap da imagem
                Bitmap bitmap = imageTeste.getDrawingCache();

                //Comprime bitmap para um formato de imagem
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);

                //Converte baos para pixels brutos em uma matriz de bytes
                byte[] dadosImagem = baos.toByteArray();

                //Definir nós para o storage do firebase
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imagens = storageReference.child("imagens");

                final String nomeArquivo = UUID.randomUUID().toString();
                final StorageReference imagemRef = imagens.child(nomeArquivo + ".jpeg");

                //Retorna objeto que irá controlar o upload
                UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                uploadTask.addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,
                                e.getMessage().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uri = task.getResult();
                                Toast.makeText(MainActivity.this,
                                        "Sucesso ao fazer o upload: " + uri.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });



        //Cadastrar o usuario
        /*usuario.createUserWithEmailAndPassword("william@gmail.com", "will123")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.i("CreateUser", "Sucesso ao criar o usuário!");
                        }else{
                            Log.i("CreateUser", "Falha ao criar o usuário!");
                        }
                    }
                });

        DatabaseReference no = reference.child("Produtos");
        DatabaseReference no2 = reference.child("Usuários");

        no2.addChildEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Produtos produto = new Produtos();
        produto.setNome("Iphone XI");
        produto.setMarca("Apple");
        produto.setPreco(4463);

        no.child("001").setValue(produto);

        produto.setNome("Galaxy S10+");
        produto.setMarca("Samsung");
        produto.setPreco(3320);

        no.child("002").setValue(produto);

        produto.setNome("Edge");
        produto.setMarca("Motorola");
        produto.setPreco(4919);

        no.child("003").setValue(produto);*/

    }

}
