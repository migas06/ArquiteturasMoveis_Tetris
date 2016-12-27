package com.isec.tetris.Tetrominoes;

import java.io.Serializable;

/**
 * Created by Miguel on 26-12-2016.
 */
public class PointsTetromino implements Serializable{

    static final long serialVersionUID = 18L;
    int x, y;

    public PointsTetromino(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
