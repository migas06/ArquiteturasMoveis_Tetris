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
    int map[][] = new int [22][14];

    int[][] next;
    int yVar;

    Tetromino tetromino;

    boolean gameOver = false;

    public TetrisMap() {
        for(int i = 0; i<22; i++){
            for(int j = 0; j<14; j++){

                if(j==0 || j==1 || j==12 || j==13)
                    map[i][j]=-1;
            }
        }

    }

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

                System.out.println("coords: "+ i + " e "+ j);

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

    public void clearPast(int x) {

        for(int i=x; i<x+4; i++){
            for(int j=y; j<y+yVar; j++){
                map[i][j] = 0;
            }
        }
    }

    public int getNext(int y, int x){
        return next[y][x];
    }

    public void print(){
        for(int i=0; i<22; i++){
            if(i<10)
                System.out.print("0");
            System.out.print(i+ "| ");

            for(int j=0; j<14; j++){
                System.out.print(map[i][j] + " ");
            }
            System.out.println("");
        }

        System.out.println("------------------------");

    }

    public boolean isGameOver() {
        System.out.println(x+" "+y+" ");
        int count=0;

        for(int i=x; i<x+4;i++){
            if(map[1][i]!=0 && getNext(0, count)==tetromino.getId()) {
                if(map[1][i] != tetromino.getId() && getNext(0, count)==tetromino.getId())
                    return true;
            }
            count++;
        }
        return gameOver;
    }

    /*
    * Getter and Setter Coordinates
    */

    public int getX() {
        return x;
    }

    public void setX(int x) {
        int wannabe = x;
        int countI=0, countJ=0;

        for(int i = y; i<yVar+4; i++){
            for(int j = wannabe; j<wannabe+4; j++){
                try {
                    if (map[i][j] != 0 && getNext(countI, countJ) != 0) {
                        if (map[i][j] != tetromino.getId() && getNext(countI, countJ) !=0)
                            System.out.println("returned");
                            return;
                    }
                    countJ++;
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("returned");
                    return;
                }
            }
            countI++;
            countJ=0;
        }

        this.x = wannabe;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setGameOver(boolean gameOver){this.gameOver = gameOver;}

    public boolean getGameOver(){return  gameOver;}

}
