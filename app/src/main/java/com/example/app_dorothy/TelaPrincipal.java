package com.example.app_dorothy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.app_dorothy.activities.DrawerBaseActivity;
import com.example.app_dorothy.activities.HomeActivity;
import com.example.app_dorothy.databinding.ActivityTelaPrincipalBinding;

public class TelaPrincipal extends DrawerBaseActivity {

    ActivityTelaPrincipalBinding activityTelaPrincipalBinding;
    private String primeiraTela = "yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTelaPrincipalBinding = ActivityTelaPrincipalBinding.inflate(getLayoutInflater());
        setContentView(activityTelaPrincipalBinding.getRoot());
        allocateActivityTitle("Home");
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if(!sharedPreferences.getBoolean(primeiraTela, false)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(primeiraTela, Boolean.TRUE);
            editor.apply();
        }else{
            TelaHome();
        }
    }

    private void TelaHome() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }
}
