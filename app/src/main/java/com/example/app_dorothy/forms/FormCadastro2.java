package com.example.app_dorothy.forms;

import static com.example.app_dorothy.config.ConfiguracaoFirebase.usuarioAtual;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_dorothy.config.ConfiguracaoFirebase;
import com.example.app_dorothy.dao.DadosCadUsuario;
import com.example.app_dorothy.R;
import com.example.app_dorothy.models.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.InputMismatchException;

public class FormCadastro2 extends AppCompatActivity {

    private EditText edt_nome, edt_cpf, edt_telefone, edt_data_nascimento;
    private Button btn_proximo,btn_voltar;
    long idade;

    private FirebaseDatabase database;

    DatePickerDialog.OnDateSetListener setListener;
    Usuario u;
    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso", "Senhas imcompativeis", "CPF inválido","Idade Insuficiente"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro2);
        getSupportActionBar();
        IniciarComponentes();
        activity = this;

        edt_nome.setText(DadosCadUsuario.nome);
        edt_cpf.setText(DadosCadUsuario.cpf);
        edt_data_nascimento.setText(DadosCadUsuario.data_nascimento);
        edt_telefone.setText(DadosCadUsuario.telefone);

        if(!TextUtils.isEmpty(DadosCadUsuario.data_nascimento)){
            String[] data = DadosCadUsuario.data_nascimento.split("/");
            calcularIdade(Integer.parseInt(data[2]) ,Integer.parseInt(data[1]),Integer.parseInt(data[0]));
        }
        if(usuarioAtual!=null){
            btn_voltar.setVisibility(View.GONE);
            edt_cpf.setEnabled(false);
        }



        btn_proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                u = new Usuario();
                u.setNome(edt_nome.getText().toString());
                DadosCadUsuario.nome = edt_nome.getText().toString();
                u.setCpf(edt_cpf.getText().toString());
                DadosCadUsuario.cpf = edt_cpf.getText().toString();
                u.setNascimento(edt_data_nascimento.getText().toString());
                DadosCadUsuario.data_nascimento = edt_data_nascimento.getText().toString();
                u.setTelefone(edt_telefone.getText().toString());
                DadosCadUsuario.telefone = edt_telefone.getText().toString();


                if(TextUtils.isEmpty(u.getNome()) || TextUtils.isEmpty(u.getCpf()) ||  TextUtils.isEmpty(u.getNascimento())||  TextUtils.isEmpty(u.getTelefone())){
                    Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else if (ValidaCPF(u.getCpf()) == false) {

                    Snackbar snackbar = Snackbar.make(view, mensagens[3], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }else if (idade <= 12 || idade >=100) {

                    Snackbar snackbar = Snackbar.make(view, mensagens[4], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }else{
                    startActivity(new Intent(getApplicationContext(), FormCadastro3.class));
                }
            }
        });


        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DadosCadUsuario.nome = edt_nome.getText().toString();
                DadosCadUsuario.cpf = edt_cpf.getText().toString();
                DadosCadUsuario.data_nascimento = edt_data_nascimento.getText().toString();
                DadosCadUsuario.telefone = edt_telefone.getText().toString();
                finish();
            }
        });

        edt_data_nascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(edt_data_nascimento.getText())){
                    String[] data = edt_data_nascimento.getText().toString().split("/");
                    pegarData(Integer.parseInt(data[2]) ,Integer.parseInt(data[1])-1,Integer.parseInt(data[0]));
                }
                else{
                    pegarData(0,0,0);
                }
            }
        });
    }

    public static boolean ValidaCPF(String cpf) {

        int Position1= 3;
        int Position2= 6;
        int Position3= 9;

        cpf = cpf.substring(0,Position1) + cpf.substring(Position1+1);
        cpf = cpf.substring(0,Position2) + cpf.substring(Position2+1);
        cpf = cpf.substring(0,Position3) + cpf.substring(Position3+1);

        if (cpf.equals("00000000000") || cpf.equals("11111111111") ||
                cpf.equals("22222222222") || cpf.equals("33333333333") ||
                cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") ||
                cpf.equals("88888888888") || cpf.equals("99999999999") ||
                (cpf.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                num = (int)(cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48);
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);

            if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10)))
                return(true);
            else{
                return(false);
            }
        } catch (InputMismatchException erro) {
            return(false);
        }

    }
    public void pegarData(int anoN,int mesN,int diaN){

        Calendar calendar = Calendar.getInstance();
        if(anoN==0){
            anoN = calendar.get(Calendar.YEAR);
            mesN = calendar.get(Calendar.MONTH);
            diaN = calendar.get(Calendar.DAY_OF_MONTH);
        }


        DatePickerDialog datePickerDialog = new DatePickerDialog(FormCadastro2.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int anoN, int mesN, int diaN) {
                edt_data_nascimento.setText(diaN+"/"+(mesN+1)+"/"+anoN);
                calcularIdade(anoN,mesN,diaN);
            }
        }, anoN, mesN, diaN);
        datePickerDialog.show();
    }
    public void calcularIdade( int anoN, int mesN, int diaN){

        Calendar calendar = Calendar.getInstance();
        final int anoA = calendar.get(Calendar.YEAR);
        final int mesA = calendar.get(Calendar.MONTH);
        final int diaA = calendar.get(Calendar.DAY_OF_MONTH);

        idade = anoA - anoN;
        if(mesN>mesA){
            idade--;
        }else if(mesA == mesN){
            if(diaN>diaA){
                idade--;
            }
        }
    }

    private void IniciarComponentes(){
        edt_nome = findViewById(R.id.edt_nome);
        edt_cpf = findViewById(R.id.edt_cpf);
        edt_data_nascimento = findViewById(R.id.edt_data_nascimento);
        edt_telefone = findViewById(R.id.edt_telefone);
        btn_proximo = findViewById(R.id.btn_proximo);
        btn_voltar = findViewById(R.id.btn_voltar);

        database = FirebaseDatabase.getInstance();
    }
    public  static Activity activity = null;

}