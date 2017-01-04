package com.isec.tetris.bad_Logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.isec.tetris.Tetrominoes.Block_I;
import com.isec.tetris.Tetrominoes.Block_J;
import com.isec.tetris.Tetrominoes.Block_L;
import com.isec.tetris.Tetrominoes.Block_O;
import com.isec.tetris.Tetrominoes.Block_S;
import com.isec.tetris.Tetrominoes.Block_T;
import com.isec.tetris.Tetrominoes.Block_Z;

import java.util.ArrayList;

/**
 * Created by Miguel on 04-01-2017.
 */
public class DrawNext {

    Block_I bi;
    Block_J bj;
    Block_L bl;
    Block_O bo;
    Block_Z bz;
    Block_S bs;
    Block_T bt;

    ArrayList<Bitmap> bitmaplist = new ArrayList<>();
    int unit;

    public DrawNext(ArrayList<Bitmap> list, float unit) {
        this.unit = (int) unit;

        ArrayList<Bitmap> listBit = list;

        bi = new Block_I(0, 0, 1, 0);
        bj = new Block_J(0, 0, 1, 0);
        bl = new Block_L(0, 0, 1, 0);
        bo = new Block_O(0, 0, 1, 0);
        bz = new Block_Z(0, 0, 1, 0);
        bs = new Block_S(0, 0, 1, 0);
        bt = new Block_T(0, 0, 1, 0);

        for(int i=0; i<listBit.size(); i++){
            bitmaplist.add(Bitmap.createScaledBitmap(listBit.get(i), this.unit, this.unit, true));
        }
    }


    public void drawNextTetromino(Canvas canvas, int nextTetromino, int l, int t) {

        int left = l;
        int top  = t;

        if(nextTetromino == 0){
            for(int i = 0; i< bi.getSize().get(0).getY(); i++){
                for(int j = 0; j< bi.getSize().get(0).getX(); j++) {
                    if(bi.getLogic().get(0)[i][j]!=0){
                        canvas.drawBitmap(bitmaplist.get(0), left, top, null);
                    }
                    left+=unit;
                }
                left=l;
                top+=unit;
            }
        }
        if(nextTetromino == 1){
            for(int i = 0; i< bo.getSize().get(0).getY(); i++){
                for(int j = 0; j< bo.getSize().get(0).getX(); j++) {
                    if(bo.getLogic().get(0)[i][j]!=0){
                        canvas.drawBitmap(bitmaplist.get(1), left, top, null);
                    }
                    left+=unit;
                }
                left=l;
                top+=unit;
            }
        }
        if(nextTetromino == 2){
            for(int i = 0; i< bl.getSize().get(0).getY(); i++){
                for(int j = 0; j< bl.getSize().get(0).getX(); j++) {
                    if(bl.getLogic().get(0)[i][j]!=0){
                        canvas.drawBitmap(bitmaplist.get(2), left, top, null);
                    }
                    left+=unit;
                }
                left=l;
                top+=unit;
            }
        }
        if(nextTetromino == 3){
            for(int i = 0; i< bo.getSize().get(0).getY(); i++){
                for(int j = 0; j< bo.getSize().get(0).getX(); j++) {
                    if(bo.getLogic().get(0)[i][j]!=0){
                        canvas.drawBitmap(bitmaplist.get(3), left, top, null);
                    }
                    left+=unit;
                }
                left=l;
                top+=unit;
            }
        }
        if(nextTetromino == 4){
            for(int i = 0; i< bj.getSize().get(0).getY(); i++){
                for(int j = 0; j< bj.getSize().get(0).getX(); j++) {
                    if(bj.getLogic().get(0)[i][j]!=0){
                        canvas.drawBitmap(bitmaplist.get(4), left, top, null);
                    }
                    left+=unit;
                }
                left=l;
                top+=unit;
            }
        }
        if(nextTetromino == 5){
            for(int i = 0; i< bt.getSize().get(0).getY(); i++){
                for(int j = 0; j< bt.getSize().get(0).getX(); j++) {
                    if(bt.getLogic().get(0)[i][j]!=0){
                        canvas.drawBitmap(bitmaplist.get(5), left, top, null);
                    }
                    left+=unit;
                }
                left=l;
                top+=unit;
            }
        }
        if(nextTetromino == 6){
            for(int i = 0; i< bz.getSize().get(0).getY(); i++){
                for(int j = 0; j< bz.getSize().get(0).getX(); j++) {
                    if(bz.getLogic().get(0)[i][j]!=0){
                        canvas.drawBitmap(bitmaplist.get(6), left, top, null);
                    }
                    left+=unit;
                }
                left=l;
                top+=unit;
            }
        }

    }
}
