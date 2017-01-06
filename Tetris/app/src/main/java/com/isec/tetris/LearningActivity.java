package com.isec.tetris;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LearningActivity extends Activity {

    int current = 0;

    ImageView back;
    ImageView imageViewNext;
    ImageView imageViewBack;
    ImageView currentImage;
    TextView textViewRules;
    TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        imageViewBack = (ImageView) findViewById(R.id.back_content);
        imageViewNext = (ImageView) findViewById(R.id.next_content);
        currentImage = (ImageView) findViewById(R.id.learn_image_view);
        textViewRules = (TextView) findViewById(R.id.textview_rules);
        textViewTitle = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);

        setThis();

        imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current++;
                setThis();
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current--;
                setThis();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setThis() {

        if(current==4)
            current = 0;
        if(current==-1)
            current=3;

        if(current == 0) {
            int id = getResources().getIdentifier("com.isec.tetris:drawable/tetromino", null, null);
            currentImage.setImageResource(id);

            textViewTitle.setText(getResources().getString(R.string.tetromino));
            textViewRules.setText(getResources().getString(R.string.text_one));
        }
        if(current == 1) {
            int id = getResources().getIdentifier("com.isec.tetris:drawable/line", null, null);
            currentImage.setImageResource(id);

            textViewTitle.setText(getResources().getString(R.string.line));
            textViewRules.setText(getResources().getString(R.string.text_two));
        }
        if(current == 2) {
            int id = getResources().getIdentifier("com.isec.tetris:drawable/mobile_rotate", null, null);
            currentImage.setImageResource(id);

            textViewTitle.setText(getResources().getString(R.string.controller));
            textViewRules.setText(getResources().getString(R.string.text_three));
        }
        if(current == 3) {
            int id = getResources().getIdentifier("com.isec.tetris:drawable/click", null, null);
            currentImage.setImageResource(id);

            textViewTitle.setText(getResources().getString(R.string.controller));
            textViewRules.setText(getResources().getString(R.string.text_four));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
