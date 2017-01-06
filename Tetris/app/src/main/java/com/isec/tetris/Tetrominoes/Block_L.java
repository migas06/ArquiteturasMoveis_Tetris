package com.isec.tetris.Tetrominoes;

import android.graphics.Color;

import com.isec.tetris.bad_Logic.TetrisMap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Miguel on 15-11-2016.
 */

public class Block_L extends Tetromino implements Serializable {

    static final long serialVersionUID = 18L;
    int finalId = 3;

    int myId;

    public final int STOP  = 0;
    public final int LEFT  = 1;
    public final int RIGHT = 2;
    public final int ROTATE =3;
    public final int DOWN   =4;


    int tetrominoMove;

    ArrayList<int[][]> rotations = new ArrayList<>();
    int [][] logic;
    ArrayList<PointsTetromino> size = new ArrayList<>();

    public Block_L(float screenX, float screenY, int myId, float unit) {
        super(screenX, screenY, unit);

        this.myId = myId+10;
        startLogic();
    }

    private void startLogic() {
        PointsTetromino pointsTetromino;

        //ORIGINAL
        logic = new int[][]{
                {myId, 0   },
                {myId, 0   },
                {myId, myId}};

        rotations.add(logic);
        pointsTetromino = new PointsTetromino(2,3);
        size.add(pointsTetromino);

        //FIRST ROTATION
        logic = new int[][]{
                {myId, myId, myId},
                {myId, 0   , 0   }};

        rotations.add(logic);
        pointsTetromino = new PointsTetromino(3,2);
        size.add(pointsTetromino);

        //SECOND ROTATION
        logic = new int[][]{
                {myId, myId},
                {0,    myId},
                {0,    myId}};

        rotations.add(logic);
        pointsTetromino = new PointsTetromino(2,3);
        size.add(pointsTetromino);

        //LAST ROTATION
        logic = new int[][]{
                {0   , 0   , myId},
                {myId, myId, myId}};

        rotations.add(logic);
        pointsTetromino = new PointsTetromino(3,2);
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

            if(tetrominoMove == ROTATE){
                tetrisMap.rotate();
            }
            //IFSTATE IS RIGHT
            if(tetrominoMove == RIGHT){
                tetrisMap.setX(tetrisMap.getX()+1);
            }
            if (tetrominoMove ==DOWN){
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
