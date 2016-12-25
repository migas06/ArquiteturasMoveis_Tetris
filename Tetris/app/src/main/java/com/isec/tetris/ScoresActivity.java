package com.isec.tetris;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.isec.tetris.DataScoresRelated.Score;
import com.isec.tetris.DataScoresRelated.ScoreAdapter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ScoresActivity extends Activity {

    ListView listViewScores;
    ImageView imageViewBack;
    Context context = this;

    ArrayList<Score> list = new ArrayList<>();
    String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/scores.obj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        imageViewBack  = (ImageView) findViewById(R.id.back);
        listViewScores = (ListView) findViewById(R.id.scoresListView);
        list = readScore();

        if(list!=null) {
            ArrayAdapter<Score> arrayAdapter = new ScoreAdapter(this, list);
            listViewScores.setAdapter(arrayAdapter);
        }

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MainActivity.class));
            }
        });
    }

    public ArrayList<Score> readScore()  {

        try{
            InputStream file = new FileInputStream(path);
            InputStream inputStream = new BufferedInputStream(file);
            ObjectInput objectInput = new ObjectInputStream(inputStream);

            list = (ArrayList<Score>) objectInput.readObject();

        } catch (FileNotFoundException e){
            return null;
        } catch (IOException e){
            Log.d("FILE", "ERROR WHILE READ FILE");
        } catch (ClassNotFoundException e) {
            Log.d("FILE", "CLASS IS NOT RECOGNIZED");
        }

        return list;
    }
}
