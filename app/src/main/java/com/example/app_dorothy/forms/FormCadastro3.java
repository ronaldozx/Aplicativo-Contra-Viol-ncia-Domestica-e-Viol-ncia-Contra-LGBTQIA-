package com.example.app_dorothy.forms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_dorothy.activities.ProfileActivity;
import com.example.app_dorothy.config.ConfiguracaoFirebase;
import com.example.app_dorothy.dao.DadosCadUsuario;
import com.example.app_dorothy.R;
import com.example.app_dorothy.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro3 extends AppCompatActivity {

    private Spinner spn_genero, spn_orientacao;
    private Button btn_cadastrar, btn_voltar;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    boolean SpinnerAberto =false;
    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form_cadastro3);
        getSupportActionBar();
        IniciarComponentes();

        if(ConfiguracaoFirebase.usuarioAtual !=null){
            btn_cadastrar.setText("Alterar");
        }



        spn_genero.setSelection(DadosCadUsuario.genero);
        spn_orientacao.setSelection(DadosCadUsuario.orientacao);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spn_genero.getSelectedItemPosition() == 0 ){
                    Toast.makeText(FormCadastro3.this, "Selecione um genero", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(spn_orientacao.getSelectedItemPosition() == 0 ){

                    Toast.makeText(FormCadastro3.this, "Selecione uma orientação", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(spn_genero.getSelectedItem().toString().equals("Homem Cisgênero") && spn_orientacao.getSelectedItem().toString().equals("Heterossexual")){
                    Toast.makeText(FormCadastro3.this, "Desculpe-me, mas você não faz parte do nosso público", Toast.LENGTH_SHORT).show();
                    return;
                }

                Usuario usuario = new Usuario();
                usuario.setGenero(spn_genero.getSelectedItemPosition());
                DadosCadUsuario.genero = spn_genero.getSelectedItemPosition();
                usuario.setOrientacao(spn_orientacao.getSelectedItemPosition());
                DadosCadUsuario.orientacao = spn_orientacao.getSelectedItemPosition();
                String email = DadosCadUsuario.email;
                String nome = DadosCadUsuario.nome;
                String cpf = DadosCadUsuario.cpf;
                String nascimento = DadosCadUsuario.data_nascimento;
                String telefone = DadosCadUsuario.telefone;
                String senha = DadosCadUsuario.senha;
                int genero = DadosCadUsuario.genero;
                int orientacao = DadosCadUsuario.orientacao;


                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(senha) || !TextUtils.isEmpty(nome) || !TextUtils.isEmpty(cpf) || !TextUtils.isEmpty(nascimento)
                        || !TextUtils.isEmpty(telefone) || genero == 0 || orientacao == 0 ){
                    if(ConfiguracaoFirebase.usuarioAtual==null){
                        mAuth.createUserWithEmailAndPassword(usuario.getEmail(u.email), senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    usuario.setId(mAuth.getUid());
                                    usuario.salvarUsuario();
                                   // AlertContato();
                                    FormCadastro.activity.finish();
                                    FormCadastro2.activity.finish();
                                    finish();
                                }else{
                                    String erro;
                                    try {
                                        throw task.getException();
                                    }catch (FirebaseAuthWeakPasswordException e){
                                        erro = "Digite uma senha com no minimo 6 caracteres";
                                    }catch (FirebaseAuthUserCollisionException e){
                                        erro = "Essa conta já foi cadastrada";
                                    }catch (FirebaseAuthInvalidCredentialsException e){
                                        erro = "E-mail inválido";
                                    }catch (Exception e){
                                        erro = "Erro ao cadastrar o usuário";
                                    }
                                    Toast.makeText(FormCadastro3.this, ""+erro, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        AtualizarDados();
                    }

                    }
                }
            });

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DadosCadUsuario.orientacao = spn_orientacao.getSelectedItemPosition();
                DadosCadUsuario.genero = spn_genero.getSelectedItemPosition();
                finish();
            }
        });




    }


    private void abrirTelaPrincipal() {
        startActivity(new Intent(getApplicationContext(), FormLogin.class));
        finish();
    }

    private void IniciarComponentes(){
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        btn_voltar = findViewById(R.id.btn_voltar);
        spn_genero = findViewById(R.id.spn_genero);
        spn_orientacao = findViewById(R.id.spn_orientacao);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("usuario");
    }

    private void AlertContato() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setMessage("Cadastre um Contato antes de começar a navegar no aplicativo.");
        alert.setPositiveButton("Tela de Contatos", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), FormCadastroContato.class));
            }
        });
        alert.show();

    }
    private void AtualizarDados(){
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        String id = usuarioAtual.getUid();

        Map<String, Object> map = new HashMap<>();
        map.put("nome", DadosCadUsuario.nome);
        map.put("genero", DadosCadUsuario.genero);
        map.put("orientacao", DadosCadUsuario.orientacao);
        map.put("nascimento",DadosCadUsuario.data_nascimento);
        map.put("email", DadosCadUsuario.email);
        map.put("telefone", DadosCadUsuario.telefone);
        databaseReference.child(id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Atualizado com Sucesso", Toast.LENGTH_LONG);
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                FormCadastro2.activity.finish();
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
