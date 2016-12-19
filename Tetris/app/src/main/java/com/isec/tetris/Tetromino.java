package com.isec.tetris;

import android.graphics.RectF;

import com.isec.tetris.logic.TetrisMap;

/**
 * Created by Miguel on 14-11-2016.
 */

public class Tetromino {
    //COORDINATES
    private float x;
    private float y;
    private float unit;

    private float tetrominoSpeed;

    float screenX, screenY;

    int myId;

    //MOVEMENTS
    public final int STOP  = 0;
    public final int LEFT  = 1;
    public final int RIGHT = 2;

    int tetrominoMove = STOP;

    public Tetromino(float screenX, float screenY, float unit){
        this.x = screenX/2;
        this.y = 0;
        this.screenX = screenX;
        this.screenY = screenY;

        this.unit = unit;

        ///to do CHANGEBLE VALUE/
        tetrominoSpeed = 1000;
    }


    public int getColor(){return 0;}
    public void setMovement(int move){
        tetrominoMove = move;
    }
    public RectF getRect(){return null;}
    public RectF getRect2(){ return new RectF(0,0,0,0);}

    public boolean update(long fps, TetrisMap tetrisMap){
        return false;
    }

    /*
    * GETTER & SETTER
    * */
    public float getX() {return x;    }
    public float getY() {return y;    }

    public int[][] getLogic() {
        return new int[0][];
    }
    public int getId(){ return myId;}
    public void drawRotate(){return;}
    public boolean pressRect(float x, float y) {
        return true;
    }
}
