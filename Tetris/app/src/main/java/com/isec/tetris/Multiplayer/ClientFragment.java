package com.isec.tetris.Multiplayer;


import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.isec.tetris.GameActivity;
import com.isec.tetris.R;

import java.net.Socket;

public class ClientFragment extends Fragment {

    Button join;
    EditText ip;

    Socket socketGame = null;
    private static final int PORT = 10100;;

    ClientFragment clientFragment = this;
    Handler handler = new Handler();

    TextView textView;

    Toast toast;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client, container, false);

        context = getActivity();
        createToast();
        textView = (TextView) view.findViewById(R.id.textViewJoin);
        join = (Button)  view.findViewById(R.id.joing);
        ip   = (EditText)view.findViewById(R.id.editTextIpServer);

        presentViewByNetworkState();

        return view;
    }
    private void runClient() {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run() {
                try
                {
                    socketGame = new Socket (ip.getText().toString(), PORT);
                    socketGame.setSoTimeout(10000);

                    SocketHandler app = (SocketHandler) getActivity().getApplication();
                    app.setSocket(socketGame);
                    app.setUser("Client");

                    getActivity().finish();
                    startActivity(new Intent(context, GameActivity.class));

                } catch (Exception e) {
                    socketGame = null;
                    toast.show();
                }
            }
        });

        t.start();
    }

    private void createToast(){
        this.toast = Toast.makeText(getActivity(), getResources().getString(R.string.invalid_address), Toast.LENGTH_SHORT);
    }

    private void presentViewByNetworkState() {
        SocketHandler app = (SocketHandler) getActivity().getApplication();
        boolean network = app.isNetworkAvailable();

        if(network){
            textView.setText(getResources().getString(R.string.join_message));
            ip.setVisibility(View.VISIBLE);

            join.setText(getResources().getString(R.string.join));
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    runClient();
                }
            });

        }else {
            textView.setText(getResources().getString(R.string.no_internet));

            ip.setVisibility(View.GONE);
            join.setText(getResources().getString(R.string.connect));
            join.setOnClickListener(new View.OnClickListener() {
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
        }
    }
}
