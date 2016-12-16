package com.isec.tetris.Tetrominos;

import android.graphics.Color;
import android.graphics.RectF;

import com.isec.tetris.Tetromino;
import com.isec.tetris.logic.TetrisMap;

/**
 * Created by Miguel on 15-11-2016.
 */

public class Block_O extends Tetromino{

    int myId;
    float unit;
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

    public Block_O(float screenX, float screenY, int myId, float unit){
        super(screenX, screenY, unit);

        this.unit = unit;
        this.screenX = screenX;
        this.screenY = screenY;
        left = screenX/4;

        this.unit = unit;
        bot = (unit*2);
        top = 0;
        right = screenX/4 + (unit*2);
        rect = new RectF(left, top, right, bot);

        this.myId = myId;
        startLogic();
    }

    private void startLogic() {
        logic = new int[][]{
                {myId, myId},
                {myId, myId}};
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
}
