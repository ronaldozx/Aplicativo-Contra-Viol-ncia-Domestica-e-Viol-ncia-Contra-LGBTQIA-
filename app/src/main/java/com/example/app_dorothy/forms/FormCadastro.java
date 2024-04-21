package com.example.app_dorothy.forms;

import static com.example.app_dorothy.config.ConfiguracaoFirebase.usuarioAtual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app_dorothy.config.ConfiguracaoFirebase;
import com.example.app_dorothy.dao.DadosCadUsuario;
import com.example.app_dorothy.R;
import com.example.app_dorothy.models.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FormCadastro extends AppCompatActivity {

    private EditText edt_email, edt_senha, edt_confirmar_senha;
    private TextView tv_inscreva;
    private Button btn_proximo;
    private TextView tv_logar;
    Usuario u;
    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso", "Senhas imcompativeis", "Senha menor que 6 digitos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar();
        IniciarComponentes();
        activity = this;
        IniciarUsuario();

        btn_proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                u = new Usuario();
                u.setEmail(edt_email.getText().toString());
                DadosCadUsuario.email = edt_email.getText().toString();
                u.setSenha(edt_senha.getText().toString());
                DadosCadUsuario.senha = edt_senha.getText().toString();
                u.setConfirmar_senha(edt_confirmar_senha.getText().toString());
                DadosCadUsuario.confirmar_senha = edt_confirmar_senha.getText().toString();

                if(usuarioAtual == null){

                    if(TextUtils.isEmpty(u.getEmail(u.email)) || TextUtils.isEmpty(u.getSenha(u.senha)) || TextUtils.isEmpty(u.getConfirmar_senha())){
                        Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }else if(!u.senha.equals(u.confirmar_senha)) {
                        Snackbar snackbar = Snackbar.make(view, mensagens[2], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }else if(u.senha.length() <= 5){
                        Snackbar snackbar = Snackbar.make(view, mensagens[3], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }else{
                        startActivity(new Intent(getApplicationContext(), FormCadastro2.class));
                    }
                }
                else{
                    edt_senha.setText(DadosCadUsuario.senha);
                    edt_confirmar_senha.setText(DadosCadUsuario.confirmar_senha);
                    edt_email.setText(DadosCadUsuario.email);
                    if(TextUtils.isEmpty(u.getEmail(u.email))){
                        Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                    else{
                        startActivity(new Intent(getApplicationContext(), FormCadastro2.class));
                    }
                }

            }
        });
    }
    void IniciarUsuario(){
        DadosCadUsuario.senha ="";
        DadosCadUsuario.data_nascimento="";
        DadosCadUsuario.orientacao = 0;
        DadosCadUsuario.nome="";
        DadosCadUsuario.email="";
        DadosCadUsuario.cpf="";
        DadosCadUsuario.confirmar_senha="";
        DadosCadUsuario.genero =0;
        DadosCadUsuario.telefone="";
    }

    private void IniciarComponentes(){
        edt_email = findViewById(R.id.edt_email);
        edt_senha = findViewById(R.id.edt_senha);
        edt_confirmar_senha = findViewById(R.id.edt_confirmar_senha);
        btn_proximo = findViewById(R.id.btn_proximo);
        tv_inscreva = findViewById(R.id.tv_cadastro);
        tv_logar = findViewById(R.id.tv_tem_cadastro);
    }
    public  static Activity activity = null;
}