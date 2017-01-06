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
    public final int DOWN   = 4;

    int tetrominoMove;

    ArrayList<int[][]> rotations = new ArrayList<>();
    int [][] logic;
    ArrayList<PointsTetromino> size = new ArrayList<>();

    public Block_I(float screenX, float screenY, int myId, float unit) {
        super(screenX, screenY, unit);

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

            if(tetrominoMove == ROTATE){
                tetrisMap.rotate();
            }

            //IFSTATE IS LEFT
            if(tetrominoMove == LEFT){
                tetrisMap.setX(tetrisMap.getX()-1);
            }

            //IFSTATE IS RIGHT
            if(tetrominoMove == RIGHT){
                tetrisMap.setX(tetrisMap.getX()+1);
            }
            if (tetrominoMove == DOWN){
                tetrisMap.allDown();
            }


        return true;
    }

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
