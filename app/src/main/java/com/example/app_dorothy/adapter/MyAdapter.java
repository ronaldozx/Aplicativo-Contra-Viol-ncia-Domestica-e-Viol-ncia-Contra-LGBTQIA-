package com.example.app_dorothy.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_dorothy.R;
import com.example.app_dorothy.models.Contato;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Contato> mLista;
    private ArrayList<String> keys;
    private Context context;

    public ArrayList<Contato> getmLista() {
        return mLista;
    }

    public MyAdapter(Context context, ArrayList<Contato> mLista, ArrayList<String> keys){
        this.mLista = mLista;
        this.context = context;
        this.keys = keys;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.contato_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Contato contato = mLista.get(position);
        holder.tv_nome.setText(contato.getNome());
        holder.tv_email.setText(contato.getEmail());
        holder.tv_telefone.setText(contato.getTelefone());
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    public void deleteContato(int position) {
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        String id = usuarioAtual.getUid();
        String key = keys.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("contato").child(id);
        databaseReference.child(key).removeValue();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mLista.size());
        Toast.makeText(context, "Contato Excluido com Sucesso", Toast.LENGTH_SHORT).show();


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_nome, tv_email, tv_telefone;
        ImageView delete;
        DatabaseReference databaseReference;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_nome = itemView.findViewById(R.id.tv_nomeContato);
            tv_email = itemView.findViewById(R.id.tv_emailContato);
            tv_telefone = itemView.findViewById(R.id.tv_telefoneContato);
            delete = itemView.findViewById(R.id.img_contato);
            databaseReference = FirebaseDatabase.getInstance().getReference("contato");
        }
    }
}
