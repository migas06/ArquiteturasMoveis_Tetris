package com.isec.tetris.Tetrominoes;

import android.graphics.Color;
import android.graphics.RectF;

import com.isec.tetris.Tetromino;
import com.isec.tetris.bad_Logic.TetrisMap;

/**
 * Created by Miguel on 15-11-2016.
 */

public class Block_L extends Tetromino {

    //IN L BLOCK WE HAVE TO DRAW AND JOIN 2 BLOCKS
    RectF rect1;
    RectF rect2;

    int myId;
    int nrRotation;
    float unit;

    public final int STOP  = 0;
    public final int LEFT  = 1;
    public final int RIGHT = 2;
    public final int ROTATE =3;

    int color = Color.argb(255, 0, 0, 0);

    int tetrominoMove;

    float screenX, screenY;
    float top, left, right, bot;

    float right2, top2, left2, bot2;

    int [][] logic;

    public Block_L(float screenX, float screenY, int myId, float unit) {
        super(screenX, screenY, unit);
        this.screenX = screenX;
        this.screenY = screenY;

        this.unit = unit;

        top = 0;
        bot = (unit*3);
        left = screenX/4;
        right = left+unit;

        rect1 = new RectF(left, top, right, bot);

        top2 = top + (unit*2);
        right2 = right + unit;
        bot2  = bot;
        left2 = right;
        rect2 = new RectF(left2, top2, right2, bot2);

        this.myId = myId;
        startLogic();
    }

    private void startLogic() {
        logic = new int[][]{
                {myId,    0},
                {myId,    0},
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
            bot2 += unit;

            //IFSTATE IS LEFT
            if(tetrominoMove == LEFT){
                if(tetrisMap.setX(tetrisMap.getX()-1)) {
                    left = left - unit;
                    left2 = left2 - unit;
                    right = right - unit;
                    right2 = right2 - unit;
                }
            }

            if(tetrominoMove == ROTATE){
                rotate();
            }

            //IFSTATE IS RIGHT
            if(tetrominoMove == RIGHT){
                if(tetrisMap.setX(tetrisMap.getX()+1)) {
                    left = left + unit;
                    left2 = left2 + unit;
                    right = right + unit;
                    right2 = right2 + unit;
                }
            }

            rect1.set(left, top, right, bot);
            rect2.set(left2, top2, right2, bot2);
            return true;
        }

        bot=screenY-50;
        return false;
    }

    private void rotate() {
        nrRotation++;

        if(nrRotation % 2 == 0) {
            left+=unit;
            right-=unit;
            bot+=unit;
            top-=unit;
            if(nrRotation==2){
                bot2-=2*unit;
                top2-=2*unit;
            }if(nrRotation ==4){
                nrRotation=0;
                top2+=2*unit;
                bot2+=2*unit;
            }

        }else {
            left-=unit;
            right+=unit;
            bot-=unit;
            top+=unit;

            if(nrRotation == 1){
                left2-=2*unit;
                right2-=2*unit;
            }
            if(nrRotation == 3){
                left2+=2*unit;
                right2+=2*unit;
            }
        }
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