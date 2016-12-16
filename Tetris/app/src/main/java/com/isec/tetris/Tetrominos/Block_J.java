package com.isec.tetris.Tetrominos;

import android.graphics.Color;
import android.graphics.RectF;

import com.isec.tetris.Tetromino;
import com.isec.tetris.logic.TetrisMap;

/**
 * Created by Miguel on 15-11-2016.
 */

public class Block_J extends Tetromino {

    int myId;
    float unit;

    //IN L BLOCK WE HAVE TO DRAW AND JOIN 2 BLOCKS
    RectF rect1;
    RectF rect2;

    public final int STOP  = 0;
    public final int LEFT  = 1;
    public final int RIGHT = 2;

    int color = Color.argb(255, 235, 131, 209);

    int tetrominoMove;

    float screenX, screenY;
    float top, left, right, bot;

    float right2, top2;

    int [][] logic;

    public Block_J(float screenX, float screenY, int myId, float unit) {
        super(screenX, screenY, unit);
        this.screenX = screenX;
        this.screenY = screenY;
        this.unit = unit;

        top = 0;
        bot = (unit*3);
        left = (screenX/4)+unit;
        right = left+unit;

        rect1 = new RectF(left, top, right, bot);

        top2 = top + (unit*2);
        right2 = right - (unit*2);
        rect2 = new RectF(left, top2, right2, bot);

        this.myId = myId;
        startLogic();
    }

    private void startLogic() {
        logic = new int[][]{
                {0,    myId},
                {0,    myId},
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
            top2 += unit;

            //IFSTATE IS LEFT
            if(tetrominoMove == LEFT){
                if(tetrisMap.setX(tetrisMap.getX()-1)) {
                    left = left - unit;
                    right = right - unit;
                    right2 = right2 - unit;
                }
            }

            //IFSTATE IS RIGHT
            if(tetrominoMove == RIGHT){
                if(tetrisMap.setX(tetrisMap.getX()+1)) {
                    left = left + unit;
                    right = right + unit;
                    right2 = right2 + unit;
                }
            }

            rect1.set(left, top, right, bot);
            rect2.set(left, top2, right2, bot);
            return true;
        }

        bot=screenY-50;
        return false;
    }

    @Override
    public int getColor(){return color;}

    @Override
    public RectF getRect(){return rect1;}

    @Override
    public RectF getRect2(){return rect2;}

    @Override
    public int[][] getLogic(){return logic;}

    @Override
    public int getId(){return myId;}
}