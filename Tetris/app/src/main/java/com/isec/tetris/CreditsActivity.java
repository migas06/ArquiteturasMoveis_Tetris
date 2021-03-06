package com.isec.tetris;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isec.tetris.Multiplayer.SocketHandler;

import java.io.IOException;

public class CreditsActivity extends Activity {

    Animation animation;
    LinearLayout linearLayout;

    Context context = this;

    MediaPlayer intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        letsDance();

        animation = AnimationUtils.loadAnimation(this, R.anim.credits);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layoutcredits);
        linearLayout.startAnimation(animation);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopDance();
            }
        });

        //CREATE AN HANDLER TO CLOSE CREDITS ACTIVITY AFTER
        //THE AMAZING SONGS ENDS

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 90000);
    }

    private void letsDance() {
        intro= MediaPlayer.create(this, R.raw.intro);
        intro.start();

    }

    private void stopDance(){
        intro.stop();
        intro.release();
        finish();
    }

    @Override
    public void onBackPressed() {
        if(intro != null)
            stopDance();
        finish();
    }
}
