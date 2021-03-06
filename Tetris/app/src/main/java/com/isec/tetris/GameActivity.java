package com.isec.tetris;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.isec.tetris.Multiplayer.SocketHandler;
import com.isec.tetris.bad_Logic.TetrisGridView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Random;

public class GameActivity extends Activity {

    TetrisGridView tetrisGrid;
    SensorManager sensorManager;
    Sensor sensor;

    MediaPlayer tetris;

    boolean song;

    final int REQUEST_CODE_CAPTURE_PERM = 1234;
    MediaProjectionManager mMediaProjectionManager;
    MediaProjection mMediaProjection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preference), Context.MODE_PRIVATE);
        song = sharedPreferences.getBoolean("song", false);

        if(song)
            letsDance();

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor == null) {
            Toast.makeText(this, getResources().getString((R.string.sensor_error)),
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        tetrisGrid = new TetrisGridView(this, point.x, point.y, sensor, sensorManager);

        //KEEP THE SCREEN ALWAYS ON
        tetrisGrid.setKeepScreenOn(true);
        setContentView(tetrisGrid);
    }

    private void letsDance() {
        tetris = MediaPlayer.create(this, R.raw.tetris);
        tetris.start();

    }

    private void stopDance(){
        tetris.stop();
        tetris.release();
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        tetrisGrid.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tetrisGrid.onPause();
        if(song)
            stopDance();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
