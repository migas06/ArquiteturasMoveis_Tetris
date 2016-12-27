package com.isec.tetris.Tetrominoes;

import android.graphics.Color;

import com.isec.tetris.bad_Logic.TetrisMap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Miguel on 15-11-2016.
 */

public class Block_S extends Tetromino implements Serializable{

    static final long serialVersionUID = 18L;


    int myId;
    int finalId = 5;

    public final int STOP  = 0;
    public final int LEFT  = 1;
    public final int RIGHT = 2;
    public final int ROTATE = 3;

    int tetrominoMove;

    ArrayList<int[][]> rotations = new ArrayList<>();
    int [][] logic;
    ArrayList<PointsTetromino> size = new ArrayList<>();

    public Block_S(float screenX, float screenY, int myId, float unit) {
        super(screenX, screenY, unit);

        this.myId = myId+10;
        startLogic();
    }

    private void startLogic() {
        PointsTetromino pointsTetromino;

        logic = new int[][]{
                {0,    myId, myId},
                {myId, myId, 0}};

        rotations.add(logic);

        pointsTetromino = new PointsTetromino(3,2);
        size.add(pointsTetromino);

        logic = new int[][]{
                {myId,   0 },
                {myId, myId},
                {0,    myId}};

        rotations.add(logic);

        pointsTetromino = new PointsTetromino(2,3);
        size.add(pointsTetromino);
    }

    public void setMovement(int move){
        tetrominoMove = move;
    }

    @Override
    public boolean update(long fps, TetrisMap tetrisMap) {

            //IFSTATE IS LEFT
            if(tetrominoMove == LEFT){
                tetrisMap.setX(tetrisMap.getX()-1);
            }

            //IFSTATE IS RIGHT
            if(tetrominoMove == RIGHT) {
                tetrisMap.setX(tetrisMap.getX()+1);
            }

            if(tetrominoMove == ROTATE){
                tetrisMap.rotate();
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
