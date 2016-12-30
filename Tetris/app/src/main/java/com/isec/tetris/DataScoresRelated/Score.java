package com.isec.tetris.DataScoresRelated;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;


/**
 * Created by Miguel on 23-12-2016.
 */

//COMPARABLE TO ORDER ARRAY
public class Score implements Serializable, Comparable<Score> {
    int score;
    Calendar today;
    String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/scores.obj";
    ArrayList<Score> list = new ArrayList<>();

    public Score(int score){
        this.score = score;
        this.today = Calendar.getInstance();
        System.out.println(today);
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

    public int getScore() {
        return score;
    }

    public Calendar getToday() {
        return today;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int compareTo(Score score) {
        return ( score.getScore()- this.score);
    }
}
