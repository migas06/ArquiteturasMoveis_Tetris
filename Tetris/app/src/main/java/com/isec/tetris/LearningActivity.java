package com.isec.tetris;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

public class LearningActivity extends Activity {

    int fragmentNumber = 0;

    ImageView imageViewNext;
    ImageView imageViewBack;
    TextView  textViewRules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        imageViewBack = (ImageView) findViewById(R.id.back_fragment);
        imageViewNext = (ImageView) findViewById(R.id.next_fragment);
        textViewRules = (TextView)  findViewById(R.id.textview_rules);


    }

}
