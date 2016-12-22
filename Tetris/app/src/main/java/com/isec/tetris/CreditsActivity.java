package com.isec.tetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreditsActivity extends Activity {

    Animation animation;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        animation = AnimationUtils.loadAnimation(this, R.anim.credits);

        linearLayout = (LinearLayout) findViewById(R.id.linear_layoutcredits);

        linearLayout.startAnimation(animation);
    }
}
