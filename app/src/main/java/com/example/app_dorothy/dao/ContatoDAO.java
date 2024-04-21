package com.example.app_dorothy.dao;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.app_dorothy.models.Contato;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ContatoDAO {
    FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
    String id = usuarioAtual.getUid();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("contato").child(id);
    public interface CallbackContato {
       public void act(ArrayList<Contato> models);
    }

    public void BuscarContatos(final ArrayList<Contato> contatos,final  CallbackContato callback){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Contato contato = dataSnapshot.getValue(Contato.class);
                    contatos.add(contato);
                }
                callback.act(contatos);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
