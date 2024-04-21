package com.example.app_dorothy.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Alerta {
    String idUsuario;
    double  longitude, latitude;


    public Alerta() {
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Alerta(String id, double longitude, double latitude) {
        this.idUsuario = id;
        this.longitude = longitude;
        this.latitude = latitude;

    }

    public String getId() {
        return idUsuario;
    }

    public void setId(String id) {
        this.idUsuario = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void salvarAlerta(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("alerta").child(idUsuario).setValue(this);
    }
}
