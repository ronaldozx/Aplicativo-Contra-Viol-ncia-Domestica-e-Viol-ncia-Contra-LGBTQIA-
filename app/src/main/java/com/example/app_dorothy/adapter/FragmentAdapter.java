package com.example.app_dorothy.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.app_dorothy.fragments.HomeFragment;
import com.example.app_dorothy.fragments.InfoFragment;
import com.example.app_dorothy.fragments.MapaFragment;

public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new MapaFragment();
            case 2:
                return new InfoFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
