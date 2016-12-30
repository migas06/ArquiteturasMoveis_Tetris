package com.isec.tetris;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.isec.tetris.MainActivity;
import com.isec.tetris.R;

public class SettingsActivity extends Activity {

    CheckBox checkBox;
    ImageView imageView;

    TextView textView;

    SeekBar seekBar;
    int progressSeekBar;

    Context context = this;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preference), Context.MODE_PRIVATE);

        boolean isChecked  = sharedPreferences.getBoolean("accelerometer", false);
        int actualProgress = sharedPreferences.getInt("level", 1);

        textView  = (TextView)  findViewById(R.id.seek_hint);
        seekBar   = (SeekBar)   findViewById(R.id.seekBar);
        checkBox  = (CheckBox)  findViewById(R.id.checkBox);
        imageView = (ImageView) findViewById(R.id.save_and_back);

        seekBar.setProgress(actualProgress);
        seekBar.setMax(10);

        progress(seekBar.getProgress());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressSeekBar = i;
                progress(progressSeekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        checkBox.setChecked(isChecked);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("accelerometer", checkBox.isChecked());
                editor.putInt("level", progressSeekBar);
                editor.commit();

                Toast.makeText(context, getString(R.string.saved), Toast.LENGTH_LONG).show();
                startActivity(new Intent(context, MainActivity.class));
            }
        });



    }

    private void progress(int progress) {
        String hint="";

        if(progress <=4){
            hint = getResources().getString(R.string.easy);
        }
        if(progress>4 && progress<=6){
            hint = getResources().getString(R.string.medium);
        }
        if(progress>6 && progress<=9){
            hint = getResources().getString(R.string.hard);
        }
        if(progress == 9){
            hint = getResources().getString(R.string.ninja);
        }
        if(progress == 10){
            hint = getResources().getString(R.string.asian);
        }

        textView.setText(getResources().getString(R.string.level) +" "+ progress + ", "+ hint);

    }
}
