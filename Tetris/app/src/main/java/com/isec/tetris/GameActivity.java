package com.isec.tetris;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity {

    TetrisGridView tetrisGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);


        tetrisGrid = new TetrisGridView(this, point.x, point.y);
        tetrisGrid.setZOrderOnTop(true);
        tetrisGrid.getHolder().setFormat(PixelFormat.TRANSPARENT);
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
