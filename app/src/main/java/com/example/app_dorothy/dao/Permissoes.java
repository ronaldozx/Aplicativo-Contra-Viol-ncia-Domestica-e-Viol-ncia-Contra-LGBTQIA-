package com.example.app_dorothy.dao;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissoes {
    static String[] appPermissoes = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET
    };
    public static final int Permissoes = 1;

   public static boolean verificarPermissoes(Activity tela) {
        List<String> permissoesRequeridas = new ArrayList<>();

        for (String permissao : appPermissoes) {
            if (ActivityCompat.checkSelfPermission(tela.getApplicationContext(), permissao) != PackageManager.PERMISSION_GRANTED) {
                permissoesRequeridas.add(permissao);
            }
        }
        if (!permissoesRequeridas.isEmpty()) {
            ActivityCompat.requestPermissions(tela, permissoesRequeridas.toArray(new String[permissoesRequeridas.size()]), Permissoes);
            return false;
        }
        return true;
    }
}
