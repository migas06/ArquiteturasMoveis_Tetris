package com.isec.tetris.Tetrominos;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;

import com.isec.tetris.Tetromino;
import com.isec.tetris.logic.TetrisMap;

import java.util.Arrays;

/**
 * Created by Miguel on 14-11-2016.
 */

public class Block_I extends Tetromino {

    RectF rect;
    int myId;
    float unit;

    public final int STOP   = 0;
    public final int LEFT   = 1;
    public final int RIGHT  = 2;
    public final int ROTATE = 3;

    int tetrominoMove;
    int nrRotation = 0;

    float screenX, screenY;
    float top, left, right, bot;

    //RED
    int color = Color.argb(255, 224, 35, 64);

    int [][] logic;

    public Block_I(float screenX, float screenY, int myId, float unit) {
        super(screenX, screenY, unit);
        this.screenX = screenX;
        this.screenY = screenY;
        this.unit = unit;

        left = screenX/4;
        bot = unit*4;
        top = 0;
        right = screenX/4+unit;
        rect = new RectF(left, top, right, bot);

        this.myId = myId;
        startLogic();
    }

    private void startLogic() {
        logic = new int[][]{
                {myId},
                {myId},
                {myId},
                {myId}};
    }

    public void setMovement(int move){
        tetrominoMove = move;
    }

    @Override
    public boolean update(long fps, TetrisMap tetrisMap) {
        if(!(bot>=screenY-50.0)) {

            //GENERAL FALL
            top += unit;
            bot += unit;


            if(tetrominoMove == ROTATE){

                nrRotation++;
                if(nrRotation % 2 == 0) {
                    right = left + (1 * unit);
                    bot = bot + (3 * unit);
                }else {
                    right = left + (4 * unit);
                    bot = bot - (3 * unit);
                }
            }

            //IFSTATE IS LEFT
            if(tetrominoMove == LEFT){
                if(tetrisMap.setX(tetrisMap.getX()-1)) {
                    left = left - unit;
                    right = right - unit;
                }

            }

            //IFSTATE IS RIGHT
            if(tetrominoMove == RIGHT){
                if(tetrisMap.setX(tetrisMap.getX()+1)) {
                    left = left + unit;
                    right = right + unit;
                }
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

    public boolean pressRect(float x, float y){
        return getRect().contains(x, y);
    }
}
