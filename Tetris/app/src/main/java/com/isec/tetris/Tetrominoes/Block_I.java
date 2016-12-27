package com.isec.tetris.Tetrominoes;

import android.graphics.Color;

import com.isec.tetris.bad_Logic.TetrisMap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Miguel on 14-11-2016.
 */

public class Block_I extends Tetromino implements Serializable{

    static final long serialVersionUID = 18L;
    int finalId = 1;

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

    ArrayList<int[][]> rotations = new ArrayList<>();
    int [][] logic;
    ArrayList<PointsTetromino> size = new ArrayList<>();

    public Block_I(float screenX, float screenY, int myId, float unit) {
        super(screenX, screenY, unit);
        this.screenX = screenX;
        this.screenY = screenY;
        this.unit = unit;

        left = screenX/4;
        bot = unit*4;
        top = 0;
        right = screenX/4+unit;
        //rect = new RectF(left, top, right, bot);

        this.myId = myId+10;
        startLogic();
    }

    private void startLogic() {

        PointsTetromino pointsTetromino;

        logic = new int[][]{
                {myId},
                {myId},
                {myId},
                {myId}};
        rotations.add(logic);
        pointsTetromino = new PointsTetromino(1,4);
        size.add(pointsTetromino);

        logic = new int[][]{{myId, myId, myId, myId}};
        rotations.add(logic);
        pointsTetromino = new PointsTetromino(4,1);
        size.add(pointsTetromino);
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
                tetrisMap.rotate();
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

            //rect.set(left, top, right, bot);
            return true;
        }

        bot=screenY-50;
        return false;
    }

    @Override
    public int getColor(){return color;}

    /*@Override
    public RectF getRect(){return rect;}*/

    @Override
    public ArrayList<int[][]> getLogic(){
        return rotations;
    }

    @Override
    public int getId(){return myId;}

    @Override
    public int getFId(){return finalId;}

    public ArrayList<PointsTetromino> getSize() {
        return size;
    }
}
