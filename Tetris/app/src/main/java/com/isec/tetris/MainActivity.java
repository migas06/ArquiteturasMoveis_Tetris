package com.isec.tetris;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.isec.tetris.Multiplayer.SocketHandler;

public class MainActivity extends Activity {

    Button buttonPlay;
    Button buttonMultiplayer;
    Button buttonSettings;
    Button buttonScores;
    Button buttonCredits;
    //Button buttonLearn;

    Intent intent;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocketHandler app = (SocketHandler) getApplication();
        app.setSocket(null);

        buttonPlay        = (Button) findViewById(R.id.button_play);
        buttonMultiplayer = (Button) findViewById(R.id.button_multiplayer);
        buttonSettings    = (Button) findViewById(R.id.button_settings);
        buttonScores      = (Button) findViewById(R.id.button_scores);
        buttonCredits     = (Button) findViewById(R.id.button_credits);
        //buttonLearn       = (Button) findViewById(R.id.button_learning);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, GameActivity.class);

                startActivity(intent);
            }
        });

        buttonCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, CreditsActivity.class);
                startActivity(intent);
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
            }
        });

        buttonScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, ScoresActivity.class);
                startActivity(intent);
            }
        });

        buttonMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, MultiplayerActivity.class);
                startActivity(intent);
            }
        });
    }
}
