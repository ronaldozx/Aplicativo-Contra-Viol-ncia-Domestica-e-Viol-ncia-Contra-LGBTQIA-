package com.example.app_dorothy.fragments;

import static android.content.Context.AUDIO_SERVICE;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.app_dorothy.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MapaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btn_alertaPlay, btn_alertaStop, btn_manual;
    private MediaPlayer mediaPlayer;
    private String mParam1;
    private String mParam2;

    public MapaFragment() {
    }

    public static MapaFragment newInstance(String param1, String param2) {
        MapaFragment fragment = new MapaFragment();
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
        View v = inflater.inflate(R.layout.fragment_mapa, container, false);
        btn_alertaPlay = v.findViewById(R.id.btn_alertaPlay);
        btn_alertaStop = v.findViewById(R.id.btn_alertaStop);
        btn_manual = v.findViewById(R.id.btn_manual);
        btn_alertaPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_alertaPlay.setVisibility(View.GONE);
                btn_alertaStop.setVisibility(View.VISIBLE);
                PlayAlerta();
            }
        });
        btn_alertaStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_alertaStop.setVisibility(View.GONE);
                btn_alertaPlay.setVisibility(View.VISIBLE);
                StopAlerta();
            }
        });
        btn_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Manual();
            }
        });
        return v;
    }

    private void PlayAlerta(){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.song);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    StopAlerta();
                }
            });
        }
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        volumeMaximo();
        Toast.makeText(getContext(), "Tocando", Toast.LENGTH_SHORT).show();
    }

    private void StopAlerta() {
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
        }
    }

    private void volumeMaximo(){

        final AudioManager mAudioManager = (AudioManager) getActivity().getSystemService(AUDIO_SERVICE);
        final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        MediaPlayer mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.start();
        int origionalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

    }

    private void Manual(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_alert_help, (LinearLayout)getView().findViewById(R.id.bottom));
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}