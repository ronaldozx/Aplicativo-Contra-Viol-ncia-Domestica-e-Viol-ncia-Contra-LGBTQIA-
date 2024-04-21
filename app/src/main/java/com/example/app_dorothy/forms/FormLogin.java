package com.example.app_dorothy.forms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.app_dorothy.R;
import com.example.app_dorothy.activities.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {

    private Button btn_signup;
    private EditText edt_email, edt_senha;
    private Button btn_signin;
    String[] mensagens = {"Preencha todos os campos", "Login realizado com sucesso"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        getSupportActionBar();
        IniciarComponents();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FormLogin.this,FormCadastro.class);
                startActivity(intent);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                String senha = edt_senha.getText().toString();

                if(email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    AutenticarUsuario(v);
                }
            }
        });
    }

    private void AutenticarUsuario(View v){
        String email = edt_email.getText().toString();
        String senha = edt_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            HomeActivity();
                        }
                    },1000);
                }else{
                    String erro;
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro ao logar o usuário";
                    }
                    Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
       FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null){
            HomeActivity();
        }
   }

    private void HomeActivity(){
        Intent intent = new Intent(FormLogin.this, HomeActivity.class);
        startActivity(intent);
    }

    private void IniciarComponents(){
        btn_signup = findViewById(R.id.btn_signup);
        edt_email = findViewById(R.id.edt_email);
        edt_senha = findViewById(R.id.edt_senha);
        btn_signin = findViewById(R.id.btn_signin);
    }
}