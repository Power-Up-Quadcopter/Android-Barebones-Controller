package com.example.powerupquadcopter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
        Tab_Control.OnFragmentInteractionListener,
        Tab_Camera.OnFragmentInteractionListener,
        Tab_GPS.OnFragmentInteractionListener,
        Tab_Tuning.OnFragmentInteractionListener,
        Tab_Network.OnFragmentInteractionListener,
        Tab_Settings.OnFragmentInteractionListener {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        ControllerHandler.initialize();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Control").setIcon(android.R.drawable.ic_media_play));
        tabLayout.addTab(tabLayout.newTab().setText("Camera").setIcon(android.R.drawable.ic_menu_camera));
        tabLayout.addTab(tabLayout.newTab().setText("GPS").setIcon(android.R.drawable.ic_menu_mylocation));
        tabLayout.addTab(tabLayout.newTab().setText("Tuning").setIcon(R.drawable.ic_baseline_tune_24));
        tabLayout.addTab(tabLayout.newTab().setText("Network").setIcon(R.drawable.ic_baseline_wifi_24));
        tabLayout.addTab(tabLayout.newTab().setText("Settings").setIcon(R.drawable.ic_baseline_settings_24));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        Runnable loop = this::loop;
        scheduler.scheduleAtFixedRate(loop, 0, 5, TimeUnit.MILLISECONDS);
    }

    void loop() {
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return ControllerHandler.motionEventHandler(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return ControllerHandler.dispatchEventHandler(event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    void toast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, message, duration).show();
    }
}