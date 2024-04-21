package com.example.app_dorothy.forms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_dorothy.R;
import com.example.app_dorothy.activities.ProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FormAlterar extends AppCompatActivity {

    private EditText edt_nome, edt_email, edt_telefone, edt_genero, edt_orientacao;
    private Button btn_voltar, btn_atualizar;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_alterar);
        IniciarComponentes();

        btn_atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtualizarDados();
            }
        });
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null){
            String id = usuarioAtual.getUid();
            DatabaseReference databaseReference = database.getReference().child("usuario").child(id);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    edt_nome.setText(snapshot.child("nome").getValue(String.class));
                    edt_genero.setText(snapshot.child("genero").getValue(String.class));
                    edt_orientacao.setText(snapshot.child("orientacao").getValue(String.class));
                    edt_email.setText(mAuth.getCurrentUser().getEmail());
                    edt_telefone.setText(snapshot.child("telefone").getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void IniciarComponentes() {
        edt_nome = findViewById(R.id.edt_nome);
        edt_email = findViewById(R.id.edt_email);
        edt_telefone = findViewById(R.id.edt_telefone);
        edt_genero = findViewById(R.id.edt_genero);
        edt_orientacao = findViewById(R.id.edt_orientacao);
        btn_atualizar = findViewById(R.id.btn_atualizar);
        btn_voltar = findViewById(R.id.btn_voltar);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = database.getReference().child("usuario");
    }

    private void AtualizarDados(){
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        String id = usuarioAtual.getUid();

        Map<String, Object> map = new HashMap<>();
        map.put("nome", edt_nome.getText().toString());
        map.put("genero", edt_genero.getText().toString());
        map.put("orientacao", edt_orientacao.getText().toString());
        map.put("email", edt_email.getText().toString());
        map.put("telefone", edt_telefone.getText().toString());
        databaseReference.child(id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Atualizado com Sucesso", Toast.LENGTH_LONG);
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Falha ao Atualizar", Toast.LENGTH_LONG);
            }
        });
    }
}