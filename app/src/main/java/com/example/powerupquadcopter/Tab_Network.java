package com.example.powerupquadcopter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.powerupquadcopter.NetworkHandler.readUDPPacket;
import static com.example.powerupquadcopter.NetworkHandler.sendUDP;
import static com.example.powerupquadcopter.NetworkHandler.tcpOutput;
import static com.example.powerupquadcopter.NetworkHandler.udpNetworkSetup;

public class Tab_Network extends Fragment {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    Button btnTCPConnect;
    Button btnTCPSend;
    Button btnUDPSend;
    EditText editTCPMessage;
    EditText editUDPMessage;

    TextView textViewTCP;
    TextView textViewUDP;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Runnable loopTCP = this::loopTCP;
        Runnable loopUDP = this::loopUDP;
        scheduler.scheduleAtFixedRate(loopUDP, 0, 5, TimeUnit.MILLISECONDS);

        new Thread(loopTCP).start();

        Log.i("Network", "OnCreate");
    }

    void loopTCP() {
        Looper.prepare();
        while(true) {
            try {
                String TCPReceive = NetworkHandler.readTCPLine();
                if (TCPReceive != null) {
                    Log.i("TCP RECEIVE", TCPReceive);

                    Date time = Calendar.getInstance().getTime();
                    int hours = time.getHours();
                    int minutes = time.getMinutes();
                    int seconds = time.getSeconds();
                    String timestamp = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                    String consoleLog = "[" + timestamp + "]" + TCPReceive + "\n";
                    Log.i("Printed", consoleLog);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  only scroll if at end
                            int scrollAmount = textViewTCP.getLayout().getLineTop(
                                    textViewTCP.getLineCount()) - textViewTCP.getHeight();
                            boolean scroll = textViewTCP.getScrollY() == scrollAmount
                                    || textViewTCP.getScrollY() <= 0;
                            textViewTCP.append(consoleLog);

                            scrollAmount = textViewTCP.getLayout().getLineTop(
                                    textViewTCP.getLineCount()) - textViewTCP.getHeight();
                            // if there is no need to scroll, scrollAmount will be <=0
                            scrollAmount = Math.max(scrollAmount, 0);
                            if(scroll) textViewTCP.scrollTo(0, scrollAmount);
                        }
                    });
                    Thread.sleep(50);
                } else {
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void loopUDP() {
        try {
            String UDPReceive = readUDPPacket();
            if (UDPReceive != null) {
                Log.i("UDP RECEIVE", UDPReceive);
                textViewUDP.append(UDPReceive);
            } else {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnHandler_TCPConnect(View view) {
        NetworkHandler.tcpNetworkSetup();
    }

    public void btnHandler_TCPSend(View view) {
        String toSend = editTCPMessage.getText().toString();

        //  replace \n with new lines
        toSend = toSend.replaceAll(";", "\n");

        Log.i("TCP", "Sending: " + toSend);
        NetworkHandler.sendTCP(toSend.toCharArray());
    }

    public void btnHandler_UDPSend(View view) {
        udpNetworkSetup();
        sendUDP("udp test\n".getBytes());
    }

    public static Tab_Settings newInstance() {
        return new Tab_Settings();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_network, container, false);
        btnTCPConnect = view.findViewById(R.id.btnTCPConnect);
        btnTCPSend = view.findViewById(R.id.btnTCPSend);
        btnUDPSend = view.findViewById(R.id.btnUDPSend);
        textViewTCP = view.findViewById(R.id.textViewTCP);
        textViewUDP = view.findViewById(R.id.textViewUDP);
        editTCPMessage = view.findViewById(R.id.editTCPMessage);
        editUDPMessage = view.findViewById(R.id.editUDPMessage);

        btnTCPConnect.setOnClickListener(this::btnHandler_TCPConnect);
        btnTCPSend.setOnClickListener(this::btnHandler_TCPSend);
        btnUDPSend.setOnClickListener(this::btnHandler_UDPSend);

        textViewTCP.setMovementMethod(new ScrollingMovementMethod());
        textViewUDP.setMovementMethod(new ScrollingMovementMethod());
        textViewTCP.setText("");
        textViewUDP.setText("");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Tab_Control.OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
