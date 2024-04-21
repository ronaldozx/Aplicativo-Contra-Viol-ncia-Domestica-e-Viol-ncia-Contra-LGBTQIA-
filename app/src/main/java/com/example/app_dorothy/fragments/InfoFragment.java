package com.example.app_dorothy.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_dorothy.R;

import java.util.Objects;

public class InfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public InfoFragment() {
    }

    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_info, container, false);
        CardView cd_100=(CardView)v.findViewById(R.id.cd_100);
        CardView cd_180=(CardView)v.findViewById(R.id.cd_180);
        CardView cd_181=(CardView)v.findViewById(R.id.cd_181);
        CardView cd_190=(CardView)v.findViewById(R.id.cd_190);


        cd_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                }else {
                    String number = "991308436";
                    Uri call = Uri.parse("tel:" + number);
                    Intent surf = new Intent(Intent.ACTION_CALL, call);
                    startActivity(surf);
                }
            }
        });

        cd_180.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                }else {
                    String number = "991308436";
                    Uri call = Uri.parse("tel:" + number);
                    Intent surf = new Intent(Intent.ACTION_CALL, call);
                    startActivity(surf);
                }
            }
        });

        cd_181.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                }else {
                    String number = "991308436";
                    Uri call = Uri.parse("tel:" + number);
                    Intent surf = new Intent(Intent.ACTION_CALL, call);
                    startActivity(surf);
                }
            }
        });

        cd_190.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                }else {
                    String number = "991308436";
                    Uri call = Uri.parse("tel:" + number);
                    Intent surf = new Intent(Intent.ACTION_CALL, call);
                    startActivity(surf);
                }
            }
        });
        return v;
    }
}