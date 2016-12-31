package com.isec.tetris;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;

import com.isec.tetris.Multiplayer.Chose_Server_or_Client;

public class MultiplayerActivity extends Activity {

    boolean networkAvaliable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        Fragment fragment = new Chose_Server_or_Client();

        Bundle bundle = new Bundle();
        bundle.putBoolean("network", networkAvaliable );
        fragment.setArguments(bundle);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }



    @Override
    public void onBackPressed() {
        finish();
    }
}
