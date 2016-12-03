package com.isec.tetris;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.isec.tetris.NoGame.MenuView;

public class MainActivity extends Activity {

    MenuView menuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point resolution = new Point();
        display.getSize(resolution);

        // And finally set the view for our game
        menuView = new MenuView(this, resolution.x, resolution.y);

        // Make our parallaxView the view for the Activity
        setContentView(menuView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        menuView.resume();
    }
}
