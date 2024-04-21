package com.example.app_dorothy.activities;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.app_dorothy.adapter.FragmentAdapter;
import com.example.app_dorothy.R;
import com.example.app_dorothy.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends DrawerBaseActivity {

    ActivityHomeBinding activityHomeBinding;
    ViewPager2 viewPager;
    TabLayout  tabLayout;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());
        allocateActivityTitle("Home");
        IniciarComponentes();
        tabLayoutMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new DrawerBaseActivity();
    }

    private void tabLayoutMenu(){
        FragmentManager FragmentManager=getSupportFragmentManager();
        adapter=new FragmentAdapter(FragmentManager,getLifecycle());
        viewPager.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setText("Inicio"));
        tabLayout.addTab(tabLayout.newTab().setText("Playlist"));
        tabLayout.addTab(tabLayout.newTab().setText("Sugestões"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    private void IniciarComponentes(){
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }
}