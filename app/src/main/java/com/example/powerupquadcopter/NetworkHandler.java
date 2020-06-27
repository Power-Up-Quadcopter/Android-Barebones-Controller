package com.example.powerupquadcopter;

import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class NetworkHandler {

    static String IP = "192.168.137.1";
    static int port = 5414;
    // TCP
    static boolean tcpConnectionInProgress;
    static Socket tcpSocket;
    static BufferedReader tcpInput;
    static PrintWriter tcpOutput;
    // UDP
    static DatagramSocket udpSocket;

    static boolean sendTCP(char[] buffer) {
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
                e.printStackTrace();
            }
        }).start();
        return true;
    }

    static boolean sendUDP(byte[] buffer) {
        if(udpSocket == null) return false;

        new Thread(() -> {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(IP), port);
                udpSocket.send(packet);
                StringBuilder s = new StringBuilder();
                for (byte b : buffer) s.append((char) b);
                Log.i("UDPNetworkSetupThread", "UDP send: \"" + s + "\"");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return true;
    }

    static String readTCPLine() {
        if(tcpSocket == null) return null;
        try {
            return tcpInput.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public void tcpNetworkSetup() {
        if(tcpConnectionInProgress || (tcpSocket != null && tcpSocket.isConnected())) return;

        Log.i("TCPNetworkSetupThread", "tryna do a TCP setup");
        tcpConnectionInProgress = true;
        new Thread(() -> {
            Looper.prepare();
            try {
                tcpSocket = new Socket(IP, port);
                tcpInput = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
                tcpOutput = new PrintWriter(tcpSocket.getOutputStream());

                Log.i("TCPNetworkSetupThread", "TCP is gucci");
            } catch (ConnectException e) {
                Log.i("TCPNetworkSetupThread", "TCP connection timed out");
            } catch (Exception e) {
                e.printStackTrace();
            }
            tcpConnectionInProgress = false;
        }).start();
    }

    static public void udpNetworkSetup() {
        if(udpSocket != null) return;

        new Thread(() -> {
            Looper.prepare();
            try {
                Log.i("UDPNetworkSetupThread", "tryna do a UDP setup");
                udpSocket = new DatagramSocket();
                Log.i("UDPNetworkSetupThread", "UDP is gucci");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
