package com.example.app_dorothy.models;

import com.google.firebase.database.Exclude;

public class Contato {
    public String id, nome, email, telefone;
    public boolean selected;

    public Contato() {
    }

    public Contato(String id, String nome, String email, String telefone, boolean selected) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Exclude
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Exclude
    public boolean getSelected() {
        return selected;
    }

}
