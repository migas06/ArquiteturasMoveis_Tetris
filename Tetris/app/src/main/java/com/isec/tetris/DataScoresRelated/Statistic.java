package com.isec.tetris.DataScoresRelated;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.isec.tetris.DataScoresRelated.Score;
import com.isec.tetris.MainActivity;
import com.isec.tetris.R;

import static com.isec.tetris.R.string.score;

public class Statistic extends Activity {

    TextView simple;
    TextView doubleL;
    TextView triple;
    TextView clear;
    TextView time;

    ImageView imageView;

    Button share;

    Score score;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        score = (Score) getIntent().getSerializableExtra("score");

        imageView = (ImageView) findViewById(R.id.button_back);
        simple  = (TextView) findViewById(R.id.simple);
        doubleL = (TextView) findViewById(R.id.double_line);
        triple  = (TextView) findViewById(R.id.triple);
        clear   = (TextView) findViewById(R.id.clear);
        time    = (TextView) findViewById(R.id.time);

        share = (Button) findViewById(R.id.button_share);

        simple.setText(simple.getText().toString() + " " + score.getSimpleLine());
        doubleL.setText(doubleL.getText().toString() + " " + score.getDoubleLine());
        triple.setText(triple.getText().toString() + " " + score.getTripleLine());
        clear.setText(clear.getText().toString() + " " + score.getClear());
        time.setText(time.getText().toString() + " " + score.getTime() +" "+ getResources().getString(R.string.secs));

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void share(){

        String message = String.format(getResources().getString(R.string.best_score),score.getScore() , score.getTime()) ;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(share, getResources().getString(R.string.share)));
    }
}
