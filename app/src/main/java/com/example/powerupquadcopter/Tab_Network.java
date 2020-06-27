package com.example.powerupquadcopter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Tab_Network extends Fragment {

    private Tab_Control.OnFragmentInteractionListener mListener;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    Button btnConnect;
    Button btnSend;

    EditText textBox;

    NetworkHandler network;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        btnConnect = findViewById(R.id.btnConnect);
//        btnSend = findViewById(R.id.btnSend);
//        textBox = findViewById(R.id.textBox);

//        btnConnect.setOnClickListener(this::btnHandler_connect);
//        btnSend.setOnClickListener(this::btnHandler_send);

//        network = new NetworkHandler();

//        Runnable loop = this::loop;
//        scheduler.scheduleAtFixedRate(loop, 0, 5, TimeUnit.MILLISECONDS);
    }

    void loop() {
//        try {
//            if(udpSocket == null) return;
//            byte[] buffer = new byte[50];
//            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(IP), port);
////            udpSocket.receive(packet);
////            Log.i("RECEIVED: ", packet.toString());
//        } catch (Exception e) {
//            Log.i("LOOP ERROR", e.toString());
//        }

        Log.i("TCP INPUT", network.readTCPLine());
    }

    public void btnHandler_connect(View view) {
//        udpNetworkSetup();
        network.tcpNetworkSetup();
    }

    public void btnHandler_send(View view) {
//        sendUDP("udp test\n".getBytes());
        network.sendTCP("tcp test\n".toCharArray());
    }

    public static Tab_Settings newInstance() {
        return new Tab_Settings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_network, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Tab_Control.OnFragmentInteractionListener) {
            mListener = (Tab_Control.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
