package com.isec.tetris.Tetrominos;

import android.graphics.Color;
import android.graphics.RectF;

import com.isec.tetris.Tetromino;

/**
 * Created by Miguel on 15-11-2016.
 */

public class Block_J extends Tetromino {

    int myId;

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

    public Block_J(float screenX, float screenY, int myId) {
        super(screenX, screenY);
        this.screenX = screenX;
        this.screenY = screenY;

        top = 0;
        bot = 192;
        left = screenX/2;
        right = left+64;

        rect1 = new RectF(left, top, right, bot);

        top2 = top + 128;
        right2 = right - 128;
        rect2 = new RectF(left, top2, right2, bot);

        this.myId = myId;
        startLogic();
    }

    private void startLogic() {
        logic = new int[][]{
                {0, 0,    myId, 0},
                {0, 0,    myId, 0},
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
            top2 += 64;

            //IFSTATE IS LEFT
            if(tetrominoMove == LEFT){
                left  = left  - 64;
                right = right - 64;
                right2 = right2 -64;
            }

            //IFSTATE IS RIGHT
            if(tetrominoMove == RIGHT){
                left  = left  + 64;
                right = right + 64;
                right2 = right2 +64;
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