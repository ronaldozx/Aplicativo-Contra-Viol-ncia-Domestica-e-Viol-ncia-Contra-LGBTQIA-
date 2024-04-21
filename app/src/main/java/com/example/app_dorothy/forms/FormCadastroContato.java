package com.example.app_dorothy.forms;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_dorothy.R;
import com.example.app_dorothy.activities.ContatoActivity;
import com.example.app_dorothy.models.Contato;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormCadastroContato extends AppCompatActivity {

    private EditText edt_nome, edt_telefone, edt_email;
    private Button btn_cadastrar, btn_voltar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_contato);
        getSupportActionBar();
        IniciarComponentes();

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String nome = edt_nome.getText().toString();
                String email = edt_email.getText().toString();
                String telefone = edt_telefone.getText().toString();

                if(!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(telefone)){
                    cadastrarContato();
                }else{
                    Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaContato();
            }
        });
    }

    private void IniciarComponentes() {
        edt_nome = findViewById(R.id.edt_nome);
        edt_email = findViewById(R.id.edt_email);
        edt_telefone = findViewById(R.id.edt_telefone);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        btn_voltar = findViewById(R.id.btn_voltar);
        databaseReference = FirebaseDatabase.getInstance().getReference("contato");
    }

    private void abrirTelaContato() {
        startActivity(new Intent(getApplicationContext(), ContatoActivity.class));
        finish();
    }

    private void cadastrarContato(){
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        String idUser = usuarioAtual.getUid();
        String nome = edt_nome.getText().toString();
        String telefone = edt_telefone.getText().toString();
        String email = edt_email.getText().toString();

        Contato c = new Contato();;
        c.setNome(nome);
        c.setTelefone(telefone);
        c.setEmail(email);

        databaseReference.child(idUser).push().setValue(c);
        abrirTelaContato();
    }
}
