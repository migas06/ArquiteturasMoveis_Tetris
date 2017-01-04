package com.isec.tetris.Multiplayer;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.isec.tetris.R;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by Miguel on 25-12-2016.
 */

public class SocketHandler extends Application implements Serializable{

    private static final long serialVersionUID = 111L;

    Socket socket;
    String User;

    MediaPlayer backgroundSong;

    public SocketHandler(){
        this.socket = null;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
}
