package com.isec.tetris.Tetrominos;

import android.graphics.Color;
import android.graphics.RectF;

import com.isec.tetris.Tetromino;

/**
 * Created by Miguel on 15-11-2016.
 */

public class Block_O extends Tetromino{

    int myId;

    //ORANGE
    int color = Color.argb(255,  249, 129, 0);

    RectF rect;

    public final int STOP  = 0;
    public final int LEFT  = 1;
    public final int RIGHT = 2;

    //MOVEMENT
    int tetrominoMove;

    float screenX, screenY;
    float top, left, right, bot;

    int [][] logic;

    public Block_O(float screenX, float screenY, int myId){
        super(screenX, screenY);

        this.screenX = screenX;
        this.screenY = screenY;
        left = screenX/2;
        bot = 128;
        top = 0;
        right = screenX/2 + 128;
        rect = new RectF(left, top, right, bot);

        this.myId = myId;
        startLogic();
    }

    private void startLogic() {
        logic = new int[][]{
                {0, myId, myId, 0},
                {0, myId, myId, 0}};
    }

    public void setMovement(int move){
        tetrominoMove = move;
    }

    @Override
    public boolean update(long fps) {
        if(!(bot>=screenY-50.0)) {

            //GENERAL FALL
            top += 64;
            bot += 64;

            //IFSTATE IS LEFT
            if(tetrominoMove == LEFT){
                left  = left  - 64;
                right = right - 64;
            }

            //IFSTATE IS RIGHT
            if(tetrominoMove == RIGHT){
                left  = left  + 64;
                right = right + 64;
            }

            rect.set(left, top, right, bot);
            return true;
        }

        bot=screenY-50;
        return false;
    }

    @Override
    public int getColor(){return color;}

    @Override
    public RectF getRect(){return rect;}

    @Override
    public int[][] getLogic(){return logic;}

    @Override
    public int getId(){return myId;}
}
