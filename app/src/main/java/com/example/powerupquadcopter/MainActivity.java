package com.example.powerupquadcopter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.spec.ECField;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    Button btnConnect;
    BottomNavigationView navigationView;

    //  wifi stuff
    Socket socket;
    BufferedReader input;
    PrintWriter output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setOnNavigationItemSelectedListener(item -> navigationMenuHandler(item));

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String IP = "192.168.1.109";
                int port = 5414;

                try {
                    socket = new Socket(IP, port);
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    output = new PrintWriter(socket.getOutputStream());

                    toast("lmao we gucci?");
                } catch (Exception e) {
                    toast(e.toString());
                }
            }
        });

        Runnable loop = new Runnable() {
            @Override
            public void run() {
                loop();
            }
        };

        scheduler.scheduleAtFixedRate(loop, 0, 5, TimeUnit.MILLISECONDS);
    }

    void loop() {
        output.print(118);
    }

    public boolean navigationMenuHandler(MenuItem item) {

        if (item.getItemId() == R.id.controlMenuItem) {
            toast("Control");
        } else if (item.getItemId() == R.id.cameraMenuItem) {
            toast("Camera");
        } else if (item.getItemId() == R.id.GPSMenuItem) {
            toast("GPS");
        } else if (item.getItemId() == R.id.settingsMenuItem) {
            toast("Settings");
        }
        return true;
    }

    void buttonHandler() {

    }

    void toast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, message, duration).show();
    }
}