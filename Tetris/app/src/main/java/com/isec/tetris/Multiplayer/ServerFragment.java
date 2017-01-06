package com.isec.tetris.Multiplayer;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.isec.tetris.GameActivity;
import com.isec.tetris.MultiplayerActivity;
import com.isec.tetris.R;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;


public class ServerFragment extends Fragment {

    ServerSocket serverSocket;
    Socket socketGame;
    private static final int PORT = 10101;;

    String ip;

    Thread thread;

    TextView textView;
    Button buttonshare;
    Button buttonWifi;
    ProgressBar progressBar;

    public ServerFragment(){
        serverSocket = null;
        socketGame = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);

        ip = getLocalIpAddress();

        buttonWifi = (Button) view.findViewById(R.id.button_open_wifi);
        buttonshare = (Button) view.findViewById(R.id.button_share);
        textView = (TextView) view.findViewById(R.id.textViewIP);
        progressBar  = (ProgressBar) view.findViewById(R.id.progressBar);

        presentViewByNetworkState();

        buttonshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        buttonWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName component = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                intent.setComponent(component);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity( intent);
            }
        });

        waiting();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void presentViewByNetworkState() {
        SocketHandler app = (SocketHandler) getActivity().getApplication();
        boolean network = app.isNetworkAvailable();

        if(network){
            textView.setText(getResources().getString(R.string.ip) + ip);
            buttonWifi.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            buttonshare.setVisibility(View.VISIBLE);
        }else {
            textView.setText(getResources().getString(R.string.no_internet));

            buttonWifi.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            buttonshare.setVisibility(View.GONE);
        }
    }

    private void waiting() {
        thread = new Thread (new Runnable() {
            @Override
            public void run() {

            while (true) {
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(PORT));
                    socketGame = serverSocket.accept();
                    serverSocket.close();
                    serverSocket = null;
                    socketGame.setSoTimeout(10000);

                    SocketHandler app = (SocketHandler) getActivity().getApplication();
                    app.setSocket(socketGame);
                    app.setUser("Server");

                    getActivity().finish();
                    startActivity(new Intent(getActivity(), GameActivity.class));
                }catch (SocketTimeoutException e){
                        System.out.println("timeout");
                }
                catch(Exception e){
                    e.printStackTrace();
                    serverSocket = null;
                    socketGame = null;
                }
            }
            }
        });
        thread.start();
    }

    private void share(){

        String message = getResources().getString(R.string.come_play_with_me)+" "+ ip;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(share, getResources().getString(R.string.share)));
    }

    //COPY PASTE FROM CLASS EXAMPLE
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress()&& inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
