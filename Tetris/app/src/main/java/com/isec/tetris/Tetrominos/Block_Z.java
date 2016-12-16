package com.isec.tetris.Tetrominos;

import android.graphics.Color;
import android.graphics.RectF;

import com.isec.tetris.Tetromino;
import com.isec.tetris.logic.TetrisMap;

/**
 * Created by Miguel on 15-11-2016.
 */

public class Block_Z extends Tetromino {

    //IN L BLOCK WE HAVE TO DRAW AND JOIN 2 BLOCKS
    RectF rect1;
    RectF rect2;

    float unit;

    int myId;

    public final int STOP  = 0;
    public final int LEFT  = 1;
    public final int RIGHT = 2;

    int color = Color.argb(255, 255, 246, 0);

    int tetrominoMove;

    float screenX, screenY;
    float top, left, right, bot;

    float bot2, right2, left2, top2;

    int [][] logic;

    public Block_Z(float screenX, float screenY, int myId, float unit) {
        super(screenX, screenY, unit);
        this.screenX = screenX;
        this.screenY = screenY;

        this.unit = unit;

        top = 0;
        bot = 64;
        left = (screenX/2);
        right = left+128;

        rect1 = new RectF(left, top, right, bot);

        bot2 = bot + 64;
        right2 = right + 64;
        left2 = left + 64;
        top2 = top + 64;

        rect2 = new RectF(left2, top2, right2, bot2);

        this.myId = myId;
        startLogic();
    }

    private void startLogic() {
        logic = new int[][]{
                {myId, myId, 0},
                {0,    myId, myId}};
    }

    public void setMovement(int move){
        tetrominoMove = move;
    }

    @Override
    public boolean update(long fps, TetrisMap tetrisMap) {
        if(!(bot2>=screenY-50.0)) {

            //GENERAL FALL
            top += 64;
            bot += 64;
            bot2 += 64;
            top2 += 64;

            //IFSTATE IS LEFT
            if(tetrominoMove == LEFT){
                if(tetrisMap.setX(tetrisMap.getX()-1)) {
                    left = left - 64;
                    right = right - 64;
                    right2 = right2 - 64;
                    left2 = left2 - 64;
                }
            }

            //IFSTATE IS RIGHT
            if(tetrominoMove == RIGHT){
                if(tetrisMap.setX(tetrisMap.getX()+1)) {
                    left = left + 64;
                    right = right + 64;
                    right2 = right2 + 64;
                    left2 = left2 + 64;
                }
            }

            rect1.set(left, top, right, bot);
            rect2.set(left2, top2, right2, bot2);
            return true;
        }

        bot2=screenY-50;
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
