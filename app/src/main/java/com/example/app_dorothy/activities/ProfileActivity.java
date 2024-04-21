package com.example.app_dorothy.activities;

import static com.example.app_dorothy.config.ConfiguracaoFirebase.usuarioAtual;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.app_dorothy.config.ConfiguracaoFirebase;
import com.example.app_dorothy.forms.FormAlterar;
import com.example.app_dorothy.forms.FormCadastro;
import com.example.app_dorothy.forms.FormCadastro2;
import com.example.app_dorothy.forms.FormLogin;
import com.example.app_dorothy.R;
import com.example.app_dorothy.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends DrawerBaseActivity {

    ActivityProfileBinding activityProfileBinding;
    private TextView tv_nome, tv_email, tv_cpf, tv_telefone, tv_data_nascimento, tv_genero, tv_orientacao;
    private Button btn_alterar, btn_deslogar, btn_galeria, btn_salvar_foto;
    private FloatingActionButton fabnt_addFoto;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private Uri imagemUri;
    private ImageView img_profile, ic_fechar;
    private StorageTask uploadTask;
    private String myUri="";
    private Dialog alertDialog;
    private boolean offline = false;

    public ProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("Perfil");
        getSupportActionBar();
        ativarOffline();
        IniciarConponentes();

        btn_deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                usuarioAtual =null;
                startActivity(new Intent(getApplicationContext(), FormLogin.class));
                finish();
            }
        });
        btn_alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FormCadastro2.class));
                finish();
            }
        });
        fabnt_addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {;
                Foto();
            }
        });
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onStart() {
        super.onStart();
        usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null) {
            String id = usuarioAtual.getUid();
            DatabaseReference databaseReference = database.getReference().child("usuario").child(id);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tv_nome.setText(snapshot.child("nome").getValue(String.class));
                    tv_email.setText(mAuth.getCurrentUser().getEmail());
                    tv_data_nascimento.setText(snapshot.child("nascimento").getValue(String.class));
                    tv_cpf.setText(snapshot.child("cpf").getValue(String.class));
                    tv_telefone.setText(snapshot.child("telefone").getValue(String.class));
                    try {
                        String[] generos = getResources().getStringArray(R.array.genero);
                        String[] orientacoes = getResources().getStringArray(R.array.orientacao);
                        tv_genero.setText( generos[snapshot.child("genero").getValue(Integer.class)]);
                        tv_orientacao.setText( orientacoes[snapshot.child("orientacao").getValue(Integer.class)]);
                    }
                    catch (Exception e){
                        tv_genero.setText(snapshot.child("genero").getValue(String.class));
                        tv_orientacao.setText(snapshot.child("orientacao").getValue(String.class));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imagemUri = data.getData();
            img_profile.setImageURI(imagemUri);
        }
    }

    private void Foto(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.alert_galeria);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setDismissWithAnimation(true);
        btn_galeria = bottomSheetDialog.findViewById(R.id.btn_galeria);
        btn_salvar_foto = bottomSheetDialog.findViewById(R.id.btn_salvar_foto);
        ic_fechar = bottomSheetDialog.findViewById(R.id.ic_fechar);

        btn_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference("imagem");
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });
        btn_salvar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Salvar();
                bottomSheetDialog.cancel();
            }
        });
        ic_fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    private void Salvar(){
        if(imagemUri != null){
            CadastrarImagem(imagemUri);
            Toast.makeText(ProfileActivity.this, "Salva", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ProfileActivity.this, "Imagem incorreta", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserInfo(){
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()> 0){
                    if(dataSnapshot.hasChild("imagem")){
                        String imagem = dataSnapshot.child("imagem").getValue().toString();
                        Picasso.get().load(imagem).into(img_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void CadastrarImagem(Uri uri) {

        final StorageReference fileRef = storageReference.child(mAuth.getCurrentUser().getUid() + ".jpg");
        uploadTask = fileRef.putFile(imagemUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileRef.getDownloadUrl();
            }

        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    myUri = downloadUrl.toString();
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("imagem", myUri);
                    databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
                }
            }
        });
    }

    private void IniciarConponentes(){
        tv_nome = findViewById(R.id.tv_nome);
        tv_email = findViewById(R.id.tv_email);
        tv_cpf = findViewById(R.id.tv_cpf);
        tv_telefone = findViewById(R.id.tv_telefone);
        tv_data_nascimento = findViewById(R.id.tv_data_nascimento);
        tv_genero = findViewById(R.id.tv_generoPerfil);
        tv_orientacao = findViewById(R.id.tv_orientacaoPerfil);
        img_profile = findViewById(R.id.img_profile);
        btn_alterar = findViewById(R.id.btn_alterar);
        btn_deslogar = findViewById(R.id.btn_deslogar);
        alertDialog = new Dialog(this);
        fabnt_addFoto = findViewById(R.id.fabtn_addFoto);
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        getUserInfo();
    }

    private void ativarOffline(){
        try {
            if(!offline){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                offline = true;
            }
        }catch (Exception e){}
    }


}