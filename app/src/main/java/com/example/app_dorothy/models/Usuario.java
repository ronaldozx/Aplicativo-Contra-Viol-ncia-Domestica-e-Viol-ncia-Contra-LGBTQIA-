package com.example.app_dorothy.models;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;


public class Usuario {
    public static String id, email, senha, confirmar_senha, nome, cpf, nascimento, telefone;
    public static int genero, orientacao;

    public Usuario(String id, String email, String senha, String confirmar_senha, String nome, String cpf, String nascimento, String telefone, int genero, int orientacao) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.confirmar_senha = confirmar_senha;
        this.nome = nome;
        this.cpf = cpf;
        this.nascimento = nascimento;
        this.telefone = telefone;
        this.genero = genero;
        this.orientacao = orientacao;
    }

    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail(String email) {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha(String senha) {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Exclude
    public String getConfirmar_senha() {
        return confirmar_senha;
    }

    @Exclude
    public void setConfirmar_senha(String confirmar_senha) {
        this.confirmar_senha = confirmar_senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getGenero() {
        return genero;
    }

    public void setGenero(int genero) {
        this.genero = genero;
    }

    public int getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(int orientacao) {
        this.orientacao = orientacao;
    }

    public void salvarUsuario(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("usuario").child(getId()).setValue(this);
    }
}
