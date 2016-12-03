package com.isec.tetris.Tetrominos;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;

import com.isec.tetris.Tetromino;

/**
 * Created by Miguel on 14-11-2016.
 */

public class Block_I extends Tetromino {

    RectF rect;

    public final int STOP  = 0;
    public final int LEFT  = 1;
    public final int RIGHT = 2;

    int tetrominoMove;

    float screenX, screenY;
    float top, left, right, bot;

    //RED
    int color = Color.argb(255, 224, 35, 64);

    public Block_I(float screenX, float screenY) {
        super(screenX, screenY);
        this.screenX = screenX;
        this.screenY = screenY;
        left = screenX/2;
        bot = 256;
        top = 0;
        right = screenX/2+64;
        rect = new RectF(left, top, right, bot);
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
}
