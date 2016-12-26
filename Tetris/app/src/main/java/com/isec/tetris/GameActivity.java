package com.isec.tetris;

import android.app.Activity;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.widget.Toast;

import com.isec.tetris.bad_Logic.TetrisGridView;

public class GameActivity extends Activity {

    TetrisGridView tetrisGrid;
    SensorManager sensorManager;
    Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

    @Override
    protected void onResume() {
        super.onResume();
        tetrisGrid.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tetrisGrid.onPause();
    }



}
