package com.isec.tetris.FragmentsFromMultiplayer;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.isec.tetris.R;

public class Chose_Server_or_Client extends Fragment {

    Button buttonClient;
    Button buttonServer;

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chose__server_or__client, container, false);

        buttonClient = (Button) view.findViewById(R.id.button_client);
        buttonServer = (Button) view.findViewById(R.id.button_server);

        context = getActivity();

        buttonServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction  fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new ServerFragment());
                fragmentTransaction.commit();
            }
        });

        buttonClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction  fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new ClientFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
