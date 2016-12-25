package com.isec.tetris.Multiplayer;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isec.tetris.GameActivity;
import com.isec.tetris.R;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;


public class ServerFragment extends Fragment {

    ServerSocket serverSocket= null;
    Socket socketGame = null;
    private static final int PORT = 10100;
    private static final int PORT_FOR_EMUL = 1011;

    Context context;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);

            textView = (TextView) view.findViewById(R.id.textViewIP);
            textView.setText(getResources().getString(R.string.ip) + " "+ getLocalIpAddress());

            context = getActivity();

            waiting();

        return view;
    }

    private void waiting() {
        Thread thread = new Thread (new Runnable() {
            @Override
            public void run() {
                try{
                    serverSocket = new ServerSocket(PORT);
                    socketGame = serverSocket.accept();
                    serverSocket.close();
                    serverSocket=null;

                    SocketHandler app = (SocketHandler) getActivity().getApplication();
                    app.setSocket(socketGame);
                    app.setUser("Server");


                    startActivity(new Intent(context, GameActivity.class));

                } catch(Exception e) {
                    e.printStackTrace();
                    socketGame = null;
                }
            }
        });
        thread.start();
    }


    //COPY PASTE FROM CLASS EXAMPLE
    public static String getLocalIpAddress() {
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
