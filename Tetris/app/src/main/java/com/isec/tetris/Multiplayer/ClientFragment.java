package com.isec.tetris.Multiplayer;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.isec.tetris.GameActivity;
import com.isec.tetris.R;

import java.net.Socket;

public class ClientFragment extends Fragment {

    Button join;
    EditText ip;

    Socket socketGame = null;
    private static final int PORT = 10100;

    Toast toast;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client, container, false);

        context = getActivity();
        createToast();
        join = (Button)  view.findViewById(R.id.joing);
        ip   = (EditText)view.findViewById(R.id.editTextIpServer);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runCli();
            }
        });

        return view;
    }
    private void runCli() {
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
}
