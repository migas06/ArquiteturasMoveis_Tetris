package com.isec.tetris.bad_Logic;

import com.isec.tetris.Tetrominoes.Tetromino;
import com.isec.tetris.Tetrominoes.Block_I;
import com.isec.tetris.Tetrominoes.Block_J;
import com.isec.tetris.Tetrominoes.Block_L;
import com.isec.tetris.Tetrominoes.Block_O;
import com.isec.tetris.Tetrominoes.Block_S;
import com.isec.tetris.Tetrominoes.Block_T;
import com.isec.tetris.Tetrominoes.Block_Z;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Miguel on 02-12-2016.
 */

public class TetrisMap implements Serializable{

    static final long serialVersionUID = 19L;

    int x=0, y=0;
    int map[][] = new int [22][16];

    int[][] next;
    int yVar;
    int xVar;

    int score;
    int rotation;

    int linesDelete;


    //STATISTIC
    int simpleLine;
    int doubleLine;
    int tripleLine;
    int clear;

    Tetromino tetromino;

    boolean gameOver = false;

    int nextTetromino;
    int actualTetromino;

    //CREATE MAP
    //0  - DEFINE THE EMPTY ZONE
    //-1 - DEFINES THE SPACE SORROUNDING THE EMPTY SPACE
    //     TETROMINOES CANNOT PASS THAT ZONE
    public TetrisMap() {
        this.score = 0;
        simpleLine = 0;
        doubleLine = 0;
        tripleLine = 0;
        clear = 0;

        for(int i = 0; i<22; i++){
            for(int j = 0; j<16; j++){

                if(j==0 || j==1 || j==2 || j==13 || j==14 || j==15)
                    map[i][j]=-1;
            }
        }

        this.rotation = 0;

        nextTetromino = random();
        actualTetromino = random();
    }

    //EVERY TETROMINO HAVE A DIFERENTE SIZE OF BOOLEAN THEN
    //WE GET THEIR BIDIMENSIONAL SIZE WITH setYVar and setXVar

    public void setYVar(){
       yVar = tetromino.getSize().get(rotation).getY();
    }

    public void setXVar(){
        xVar = tetromino.getSize().get(rotation).getX();
    }

    /*
    * METHODS
    * */

    public void setNext(Tetromino tetromino){next = tetromino.getLogic().get(rotation);}

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
                        if(map[i][j] != tetromino.getId() && getNext(countI, countJ) == tetromino.getId()) {
                            finalId();
                            return false;
                        }
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    finalId();
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
            clearTrail();
        }catch (ArrayIndexOutOfBoundsException e){
            System.err.println(e);
        }

        countI = 0;
        countJ = 0;

        //CREATE THE NEW TETROMINO ON THIS NEW POSITION
        for(int i=y; i<y+yVar; i++) {
            for (int j = x; j < x+xVar; j++) {
                if(getNext(countI, countJ) == tetromino.getId() && getNext(countI, countJ) != 0)
                    map[i][j] = getNext(countI, countJ);
                countJ++;
            }
            countJ=0;
            countI++;
        }
        y++;
        return true;
    }

    //Clear last line of tetromono
    private void clearTrail() {
        for(int i=y-1; i<(y-1)+yVar; i++){
            for (int j=x; j<x+xVar; j++){
                if(!(map[i][j] >0 && map[i][j]<8))
                    map[i][j] = 0;
            }
        }
    }

    //FIRST ROTATION, NORMAL ROTATION
    public int getNext(int y, int x){
        return tetromino.getLogic().get(rotation)[y][x];
    }


    //VERIFIES IF THE THE LINE IS FULLFILLED AND THEN PUTS A GAME OVER
    public boolean isGameOver() {
        int count=0;

        for(int i=x; i<x+xVar;i++){
            if(tetromino instanceof Block_I){
                try {
                    if (map[2][i] != 0 && getNext(0, count) == tetromino.getId()) {
                        if (map[2][i] != tetromino.getId() && getNext(0, count) == tetromino.getId())
                            return true;
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    return true;
                }
            }
            try{

                if(map[1][i]!=0 && getNext(0, count)==tetromino.getId()) {
                    if(map[1][i] != tetromino.getId() && getNext(0, count)==tetromino.getId())
                        return true;
                }
            }catch (ArrayIndexOutOfBoundsException e){
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


        //CHECKS THE WANNABE TETROMINO
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

        //IF HE CAN BE
        for(int i=y-1; i<y-1+yVar; i++){
            for (int j=getX(); j<getX()+xVar; j++){
                try{
                    if(map[i][j] > 8)
                        map[i][j] = 0;
                }catch (ArrayIndexOutOfBoundsException e){
                    continue;}
            }
        }

        this.x = wannabe;
        return true;
    }

    private void finalId() {
        for(int i=0; i<22; i++){
            for(int j=0; j<16; j++){
                if(map[i][j] == tetromino.getId())
                    map[i][j] = tetromino.getFId();
            }
        }
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

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

    //VERIFY IF LINES ARE READY TO DELETE
    public void verifyLines() {
        linesDelete = 0;
        int countCell=0;

        for(int i=0; i<22;i++){
            countCell=0;
            for(int j=0; j<16; j++){
                if(map[i][j]!=0)
                    countCell++;
            }
            if(countCell==16){
                linesDelete++;
                deleteLine(i);
            }
        }

        if(linesDelete==1) {
            simpleLine++;
            score += 100;
        }
        if(linesDelete==2){
            doubleLine++;
            score+=300;
        }

        if(linesDelete==3){
            tripleLine++;
            score+=500;
        }

        if(linesDelete==4){
            clear++;
            score+=800;
        }
    }

    private void deleteLine(int lineNumber) {

        for(int i=lineNumber; i>=0 ;i-- ){
            for(int j=0; j<16; j++){
                if(i!=0)
                    map[i][j] = map[i-1][j];
                else{
                    if(j==0 || j==1 || j==2 || j==13 || j==14 || j==15){
                        map[i][j] = -1;
                    }
                }
            }
        }
    }

    public int[][] getMap() {
        return map;
    }

    public int getScore() {return score;}

    public int getLinesDelete() {
        return linesDelete;
    }

    public void deleteSpaces(int delete) {
        Random random = new Random();

        for(int i=0; i< delete; i++){

            while(true) {
                int y = random.nextInt(21);
                int x = 3 + (int) (Math.random() * 12);

                if(map[y][x] != 0){
                    break;
                }
            }
            map[y][x] = 0;
        }
    }

    public void rotate() {

        int wannabe = rotation;
        try{
            tetromino.getSize().get(wannabe+1);
        }catch (IndexOutOfBoundsException e){
            wannabe = -1;
        }

        //VERIFIES IF HE CAN ROTATE
        for (int i = 0; i < tetromino.getSize().get(wannabe+1).getY(); i++) {
            for (int j = 0; j < tetromino.getSize().get(wannabe+1).getX(); j++) {
                try {
                    if ((map[y+i][j+x] > 0 && map[y+i][j+x] < 8 && map[y+i][j+x] != -1)) {
                        return;
                    }

                    //LEFT SIDE IT BACKS A UNIT BACK
                    while(true) {
                        if (map[y+i][j+x] == -1) {
                            if (x > 5) {
                                setX(x-1);
                            } else {
                                setX(x+1);
                            }
                        }
                        else
                            break;
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    return;
                }
            }
        }

        //CLEAN THE OLD ONE
        for (int i = -1; i < tetromino.getSize().get(rotation).getY(); i++) {
            for (int j = 0; j < tetromino.getSize().get(rotation).getX(); j++) {
                try {
                    if (map[y+i][j+x] > 8)
                        map[y+i][j+x] = 0;
                }catch (ArrayIndexOutOfBoundsException e){
                    rotation=wannabe+1;
                    return;
                }
            }
        }

        rotation=wannabe+1;
    }

    public int random(){
        Random random = new Random();
        int idBlock = random.nextInt(7);

        return idBlock;
    }

    public int getNextTetromino() {
        return nextTetromino;
    }

    public void setNextTetromino(int nextTetromino) {
        this.nextTetromino = nextTetromino;
    }

    public int getActualTetromino() {
        return actualTetromino;
    }

    public void setActualTetromino(int actualTetromino) {
        this.actualTetromino = actualTetromino;
    }

    public int getSimpleLine() { return simpleLine; }

    public int getDoubleLine() {
        return doubleLine;
    }

    public int getTripleLine() {
        return tripleLine;
    }

    public int getClear() {
        return clear;
    }

    public void allDown() {
        while(true){
            if(!update()){
                break;
            }
        }
    }
}
