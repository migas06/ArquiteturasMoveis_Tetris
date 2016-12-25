package com.isec.tetris.Multiplayer;

import android.app.Application;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by Miguel on 25-12-2016.
 */

public class SocketHandler extends Application implements Serializable{

    private static final long serialVersionUID = 111L;

    Socket socket;
    String User;

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
}
