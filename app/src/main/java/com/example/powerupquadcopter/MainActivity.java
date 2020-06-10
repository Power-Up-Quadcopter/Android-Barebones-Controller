package com.example.powerupquadcopter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    Button btnConnect;
    Button btnSend;

    BottomNavigationView navigationView;

    //  wifi stuff
    String IP = "192.168.137.1";
    int port = 5414;
    // TCP
    boolean tcpConnectionInProgress;
    Socket tcpSocket;
    BufferedReader tcpInput;
    PrintWriter tcpOutput;
    // UDP
    DatagramSocket udpSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        btnConnect = findViewById(R.id.btnConnect);
        btnSend = findViewById(R.id.btnSend);
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setOnNavigationItemSelectedListener(this::navigationMenuHandler);

        btnConnect.setOnClickListener(this::btnHandler_connect);
        btnSend.setOnClickListener(this::btnHandler_send);

        Runnable loop = this::loop;

        scheduler.scheduleAtFixedRate(loop, 0, 5, TimeUnit.MILLISECONDS);
    }

    void loop() {

    }

    public void btnHandler_connect(View view) {
//        udpNetworkSetup();
        tcpNetworkSetup();
    }

    public void btnHandler_send(View view) {
//        sendUDP("udp test".getBytes());
        sendTCP("tcp test\n".toCharArray());
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

    void toast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, message, duration).show();
    }

    boolean sendTCP(char[] buffer) {
        if(tcpSocket == null) return false;
        new Thread(() -> {
            try {
                tcpOutput.write(buffer);
                tcpOutput.flush();
                StringBuilder s = new StringBuilder();
                for (char c : buffer) s.append(c);
                Log.i("TCP Thread", "TCP send: \"" + s + "\"");
            }
            catch (Exception e) {
                toast(e.toString());
                e.printStackTrace();
            }
        }).start();
        return true;
    }

    boolean sendUDP(byte[] buffer) {
        if(udpSocket == null) return false;

        new Thread(() -> {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(IP), port);
                udpSocket.send(packet);
                StringBuilder s = new StringBuilder();
                for (byte b : buffer) s.append((char) b);
                Log.i("UDPNetworkSetupThread", "UDP send: \"" + s + "\"");
            } catch (Exception e) {
                toast(e.toString());
                e.printStackTrace();
            }
        }).start();
        return true;
    }

    public void tcpNetworkSetup() {
        if(tcpConnectionInProgress || (tcpSocket != null && tcpSocket.isConnected())) return;

        Log.i("TCPNetworkSetupThread", "tryna do a TCP setup");
        tcpConnectionInProgress = true;
        new Thread(() -> {
            Looper.prepare();
            try {
                toast("Attempting TCP connection...");
                tcpSocket = new Socket(IP, port);
                tcpInput = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
                tcpOutput = new PrintWriter(tcpSocket.getOutputStream());

                Log.i("TCPNetworkSetupThread", "TCP is gucci");
            } catch (ConnectException e) {
                Log.i("TCPNetworkSetupThread", "TCP connection timed out");
                toast("TCP connection timed out");
            } catch (Exception e) {
                toast(e.toString());
                e.printStackTrace();
            }
            tcpConnectionInProgress = false;
        }).start();
    }

    public void udpNetworkSetup() {
        if(udpSocket != null) return;

        new Thread(() -> {
            Looper.prepare();
            try {
                Log.i("UDPNetworkSetupThread", "tryna do a UDP setup");
                udpSocket = new DatagramSocket(port);
            } catch (Exception e) {
                toast(e.toString());
                e.printStackTrace();
            }
        }).start();
    }

}