package com.example.app_dorothy.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_dorothy.R;
import com.example.app_dorothy.adapter.TextoAdapter;
import com.example.app_dorothy.dao.ContatoDAO;
import com.example.app_dorothy.models.Alerta;
import com.example.app_dorothy.models.Contato;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView img_profile;
    private Button btn_voltar, btn_proximo;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private Context mContext;
    private String mParam1;
    private String mParam2;
    private double latitude;
    private double longitude;
    public ArrayList<Contato> contatos;
    private TextView tv_dica;
    private String link;
    private ContatoDAO contatoDAO = new ContatoDAO();

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IniciarComponentes();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Localizacao();

        
    }

    private void IniciarComponentes() {
        databaseReference = FirebaseDatabase.getInstance().getReference("alerta");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        img_profile = v.findViewById(R.id.img_profile);
        tv_dica = v.findViewById(R.id.tv_dica);
        btn_proximo = v.findViewById(R.id.btn_proximo);
        btn_voltar = v.findViewById(R.id.btn_voltar);


        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_dica.setText(TextoAdapter.Voltar());
            }
        });


        btn_proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_dica.setText(TextoAdapter.Proximo());
            }
        });

        tv_dica.setText(TextoAdapter.texto_random());



        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                            final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("Alerta");
                            alert.setMessage("O contador chegando a ZERO, será enviado um alerta para seus contatos");
                            alert.setCancelable(false);
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            View layout = inflater.inflate(R.layout.alert_help, null);
                            alert.setView(layout);
                            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            if (verificarLocalizacao()) {
                                final AlertDialog alertDialog = alert.create();
                                alertDialog.show();
                                new CountDownTimer(10000, 1000) {
                                    boolean cancelar = false;

                                    @Override
                                    public void onTick(long l) {
                                        alertDialog.setMessage("Contatos sendo avisados em: " + l / 1000);
                                        if (!alertDialog.isShowing()) {
                                            onStop();
                                            cancelar = true;
                                        }
                                    }

                                    @Override
                                    public void onFinish() {
                                        try {
                                            CadastrarAlerta();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if (!cancelar) {
                                            BuscarContatos();
                                        }
                                        alertDialog.dismiss();
                                        }
                                }.start();
                            }else {
                                Toast.makeText(getContext(), "Não foi possivel pegar a localização", Toast.LENGTH_SHORT).show();
                            }



            }
        });
        return v;
    }

    private  void BuscarContatos(){
        ArrayList<Contato> contatos;

        contatos = new ArrayList<>();
        contatoDAO.BuscarContatos(contatos, new ContatoDAO.CallbackContato() {
            @Override
            public void act (ArrayList<Contato> contatos) {
                if(contatos.size()>0){
                    enviarSms(contatos);
                }
                else {
                    Toast.makeText(getContext(), "Não á contatos de emergencia cadastrado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean permitidoLocalizacao=false;
    boolean verificarLocalizacao (){
        if(longitude != 0 && latitude != 0){
            return true;
        }
        if(!permitidoLocalizacao){
            Localizacao();
        }
        return  false;

    }

    private void Localizacao() {
        try {
            LocationManager gps = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            };

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            permitidoLocalizacao = true;
            gps.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (Exception e) {
        }
    }

    private void CadastrarAlerta() throws IOException {

        String id = mAuth.getUid();

        Alerta a = new Alerta();
        a.setLatitude(latitude);
        a.setLongitude(longitude);

        databaseReference.child(id).push().setValue(a);
    }



    private void enviarSms(ArrayList<Contato> list) {

        link = "https://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
        
        SmsManager smsManager = SmsManager.getDefault();
        for (Contato contato: list) {
            smsManager.sendTextMessage(contato.telefone, null, contato.nome + ", Preciso de Ajuda\n" + link, null, null);

        }
        Toast.makeText(getContext(), "Pedido Enviado", Toast.LENGTH_SHORT).show();
    }

    
}
