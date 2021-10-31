package com.williamcoelho.listadetarefas.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BancoDeDadosHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NOME_DB = "BD_TAREFAS";
    public static String TABELA_TAREFAS = "tarefas";

    public BancoDeDadosHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFAS
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nome TEXT NOT NULL);";

        try{
            db.execSQL(sql);
            Log.i("INFO DB", "SUCESSO AO CRIAR A TABELA");
        }catch (Exception e){
            Log.i("INFO BD", "ERRO AO CRIAR A TABELA" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "";

        try{
            db.execSQL(sql);
            Log.i("INFO DB", "SUCESSO AO ATUALIZAR A TABELA");
        }catch (Exception e){
            Log.i("INFO BD", "ERRO AO ATUALIZAR A TABELA" + e.getMessage());
        }

    }
}
