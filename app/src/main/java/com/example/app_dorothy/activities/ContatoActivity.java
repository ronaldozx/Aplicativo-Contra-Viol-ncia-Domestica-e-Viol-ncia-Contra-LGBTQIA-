package com.example.app_dorothy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_dorothy.adapter.MyAdapter;
import com.example.app_dorothy.forms.FormCadastroContato;
import com.example.app_dorothy.R;
import com.example.app_dorothy.databinding.ActivityContatoBinding;
import com.example.app_dorothy.models.Contato;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ContatoActivity extends DrawerBaseActivity {


    private FloatingActionButton floatingActionButton;
    private MyAdapter adapter;
    private ArrayList<Contato> list;
    private boolean offline = false;
    ActivityContatoBinding activityContatoBinding;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    List<Contato> contatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContatoBinding = ActivityContatoBinding.inflate(getLayoutInflater());
        setContentView(activityContatoBinding.getRoot());
        allocateActivityTitle("Contatos");
        getSupportActionBar();
        IniciarConponentes();
        ativarOffline();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        String id = usuarioAtual.getUid();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> keys = new ArrayList<String>();
        list = new ArrayList<Contato>();
        adapter = new MyAdapter(this, list, keys);
        recyclerView.setAdapter(adapter);
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Contato contato = dataSnapshot.getValue(Contato.class);
                    list.add(contato);
                    keys.add(dataSnapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                list = new ArrayList<Contato>();
                adapter.getmLista().remove(viewHolder.getAdapterPosition());
                adapter.deleteContato(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.size()>=5){
                    Toast.makeText(ContatoActivity.this, "Maximo de contatos", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(getApplicationContext(), FormCadastroContato.class));
                    finish();
                }
            }
        });
    }

    private void IniciarConponentes(){
        bottomNavigationView = findViewById(R.id.bnv);
        floatingActionButton = findViewById(R.id.fabtn_addContato);
        recyclerView = findViewById(R.id.rv_lista);
        databaseReference = FirebaseDatabase.getInstance().getReference("contato");
        mAuth = FirebaseAuth.getInstance();
    }

    private void ativarOffline(){
        try {
            if(!offline){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                offline = true;
            }else{
                databaseReference.keepSynced(true);
            }
        }catch (Exception e){}
    }
}
