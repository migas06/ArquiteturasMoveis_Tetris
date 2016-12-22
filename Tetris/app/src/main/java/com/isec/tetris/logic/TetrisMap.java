package com.isec.tetris.logic;

import android.util.Log;

import com.isec.tetris.Tetromino;
import com.isec.tetris.Tetrominos.Block_I;
import com.isec.tetris.Tetrominos.Block_J;
import com.isec.tetris.Tetrominos.Block_L;
import com.isec.tetris.Tetrominos.Block_O;
import com.isec.tetris.Tetrominos.Block_S;
import com.isec.tetris.Tetrominos.Block_T;
import com.isec.tetris.Tetrominos.Block_Z;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Miguel on 02-12-2016.
 */

public class TetrisMap implements Serializable{

    int x=0, y=0;
    int map[][] = new int [22][16];

    int[][] next;
    int yVar;
    int xVar;
    int yVarOld;
    int xVarOld;

    Tetromino tetromino;

    boolean rotation = false;
    boolean rotation1stForm = false;
    boolean gameOver = false;

    //CREATE MAP
    //0  - DEFINE THE EMPTY ZONE
    //-1 - DEFINES THE SPACE SORROUNDING THE EMPTY SPACE
    //     TETROMINOES CANNOT PASS THAT ZONE
    public TetrisMap() {
        for(int i = 0; i<22; i++){
            for(int j = 0; j<16; j++){

                if(j==0 || j==1 || j==2 || j==13 || j==14 || j==15)
                    map[i][j]=-1;
            }
        }

    }

    //EVERY TETROMINO HAVE A DIFERENTE SIZE OF BOOLEAN THEN
    //WE GET THEIR BIDIMENSIONAL SIZE WITH setYVar and setXVar

    public void setYVar(){
        yVarOld = yVar;
        if(!rotation1stForm) {
            if (tetromino instanceof Block_Z || tetromino instanceof Block_S || tetromino instanceof Block_T || tetromino instanceof Block_O) {
                yVar = 2;
            } else if (tetromino instanceof Block_J || tetromino instanceof Block_L) {
                yVar = 3;
            } else {
                yVar = 4;
            }
        }else{
            yVar =1;
        }
    }

    public void setXVar(){
        xVarOld = xVar;
        if(!rotation1stForm) {
            if(tetromino instanceof Block_J || tetromino instanceof Block_L || tetromino instanceof Block_O)
                xVar = 2;
            else if(tetromino instanceof Block_S || tetromino instanceof Block_T || tetromino instanceof Block_Z)
                xVar = 3;
            else
                xVar = 1;
        }else {
            xVar = 4;
        }
    }

    /*
    * METHODS
    * */

    public void setNext(Tetromino tetromino){next = tetromino.getLogic();}

    public void setTetromino(Tetromino tetromino) {this.tetromino = tetromino;}

    public boolean update(){

        int countI = 0;
        int countJ = 0;
        setYVar();
        setXVar();

        //WILL SEE IF ITS POSSIBLE THE NEXT POSITION OTHERWISE IT RETURN NULL
        for(int i=y; i<y+yVar; i++){
            for(int j=x; j<x+xVar; j++) {
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

        //IF IT GETS THIS POINT, YEAH ITS POSSIBLE TETROMINO BE THERE SO
        //LETS CLEAN OUR TRAIL
        try {
            if (!rotation)
                clearTrail();
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("aqui!");
        }

        countI = 0;
        countJ = 0;

        //CREATE THE NEW TETROMINO ON THIS NEW POSITION
        for(int i=y; i<y+yVar; i++) {
            for (int j = x; j < x + xVar; j++) {
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

    //CLEAR TRAIL
    private void clearTrailOld() {
        for(int i=y-1; i<(y-1)+yVarOld; i++){
            for (int j=x; j<x+xVarOld; j++){
                map[i][j] = 0;
            }
        }
    }
    private void clearTrail() {
        for(int i=y-1; i<(y-1)+yVar; i++){
            for (int j=x; j<x+xVar; j++){
                map[i][j] = 0;
            }
        }
    }

    public int getNext(int y, int x){
        return tetromino.getLogic()[y][x];
    }


    //VERIFIES IF THE THE LINE IS FULLFILLED AND THEN PUTS A GAME OVER
    public boolean isGameOver() {
        int count=0;

        for(int i=x; i<x+xVar;i++){
            if(tetromino instanceof Block_I){
                if(map[2][i]!=0 && getNext(0, count)==tetromino.getId()) {
                    if(map[2][i] != tetromino.getId() && getNext(0, count)==tetromino.getId())
                        return true;
                }
            }
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

    public boolean setX(int position) {
        int wannabe = position;
        int countI=0, countJ=0;

        for(int i = y; i<y+yVar; i++){
            for(int j = wannabe; j<wannabe+xVar; j++){
                try {
                    if (map[i][j] != 0 && getNext(countI, countJ) == tetromino.getId()) {
                        if (map[i][j] != tetromino.getId() && getNext(countI, countJ) == tetromino.getId()){
                            return false;
                        }
                    }
                    countJ++;
                }catch (ArrayIndexOutOfBoundsException e){
                    return false;
                }
            }
            countI++;
            countJ=0;
        }

        for(int i=y-1; i<y+yVar; i++){
            for (int j=getX(); j<getX()+xVar; j++){
                try{
                    map[i][j] = 0;
                }catch (ArrayIndexOutOfBoundsException e){
                    continue;}
            }
        }

        this.x = wannabe;
        return true;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRotation1stForm(boolean rotation1stForm) {
        this.rotation1stForm = rotation1stForm;
    }

    public void setRotation(boolean rotation) {this.rotation = rotation;}

    //LOG PRINT
    public void print(){
        for(int i=0; i<22; i++){
            if(i<10)
                System.out.print("0");
            System.out.print(i+ "| ");

            for(int j=0; j<16; j++){
                System.out.print(map[i][j] + " ");
            }
            System.out.println("");
        }

        System.out.println("------------------------");

    }
}
