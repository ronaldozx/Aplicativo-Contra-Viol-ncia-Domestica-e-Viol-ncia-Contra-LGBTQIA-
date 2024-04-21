package com.example.app_dorothy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_dorothy.dao.Permissoes;
import com.example.app_dorothy.forms.FormCadastro3;
import com.example.app_dorothy.forms.FormLogin;


public class TelaSplash extends AppCompatActivity {

    boolean isFirst = true;

    private void chamaVerificador(){
        if(Permissoes.verificarPermissoes(this)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isFirst){
                        isFirst=false;
                        startActivity(new Intent(TelaSplash.this, FormLogin.class));
                        finish();
                    }
                }
            },1000);
        }else{
            Toast.makeText(this, "Verifique as Permissões na Configuração", Toast.LENGTH_SHORT).show();
        }
    }
    private  void loop(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chamaVerificador();
                loop();
            }
        },2000);
    }
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loop();
    }
}
