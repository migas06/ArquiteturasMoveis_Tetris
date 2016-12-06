package com.isec.tetris.logic;

import android.util.Log;

import com.isec.tetris.Tetromino;
import com.isec.tetris.Tetrominos.Block_J;
import com.isec.tetris.Tetrominos.Block_L;
import com.isec.tetris.Tetrominos.Block_O;
import com.isec.tetris.Tetrominos.Block_S;
import com.isec.tetris.Tetrominos.Block_T;
import com.isec.tetris.Tetrominos.Block_Z;

import java.util.ArrayList;

/**
 * Created by Miguel on 02-12-2016.
 */

public class TetrisMap {

    int x=0, y=0;
    int map[][] = new int [22][10];

    int[][] next;
    int yVar;

    Tetromino tetromino;

    /*
    * METHODS
    * */

    public void setYVar(){

        if(tetromino instanceof Block_Z || tetromino instanceof Block_S || tetromino instanceof Block_T || tetromino instanceof Block_O)
            yVar=2;
        else if(tetromino instanceof Block_J || tetromino instanceof Block_L)
            yVar=3;
        else
            yVar=4;
    }

    public void setNext(Tetromino tetromino){next = tetromino.getLogic();}

    public void setTetromino(Tetromino tetromino) {this.tetromino = tetromino;}

    public boolean update(){

        int countI = 0;
        int countJ = 0;
        setYVar();

        for(int i=y; i<y+yVar; i++){
            for(int j=x; j<x+4; j++) {
                try{
                    if(map[i][j] != 0 && getNext(countI, countJ) == tetromino.getId()){
                        if(map[i][j] != tetromino.getId() && getNext(countI, countJ) == tetromino.getId())
                            return false;
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    return false;
                }
                countJ++;
            }
            countJ=0;
            countI++;
        }
        try {
            clearTrail();
        }catch (ArrayIndexOutOfBoundsException e){}

        countI = 0;
        countJ = 0;

        for(int i=y; i<y+yVar; i++) {
            for (int j = x; j < x + 4; j++) {
                if(getNext(countI, countJ) == tetromino.getId())
                    map[i][j] = getNext(countI, countJ);
                countJ++;
            }

            countJ=0;
            countI++;
        }

        y++;
        return true;
    }

    private void clearTrail() {
        for(int i=y-1; i<(y-1)+yVar; i++){
            for (int j=x; j<x+4; j++){
                map[i][j] = 0;
            }
        }
    }

    public int getNext(int y, int x){
        return next[y][x];
    }

    public void print(){
        for(int i=0; i<22; i++){
            System.out.print(i+ " ");
            for(int j=0; j<10; j++){
                System.out.print(map[i][j] + " ");
            }
            System.out.println("");
        }

        System.out.println("------------------------");

    }

    /*
    * Getter and Setter Coordinates
    */

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
