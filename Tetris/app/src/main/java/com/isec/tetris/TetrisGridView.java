package com.isec.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.isec.tetris.Tetrominos.Block_I;
import com.isec.tetris.Tetrominos.Block_J;
import com.isec.tetris.Tetrominos.Block_L;
import com.isec.tetris.Tetrominos.Block_O;
import com.isec.tetris.Tetrominos.Block_S;
import com.isec.tetris.Tetrominos.Block_T;
import com.isec.tetris.Tetrominos.Block_Z;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Miguel on 14-11-2016.
 */

public class TetrisGridView extends SurfaceView implements Runnable {

    Thread gameThread = null;

    ///USED WHEN WE paint and canvas IN THREAD
    SurfaceHolder surfaceHolder;

    float screenX, screenY;

    boolean running = false;
    boolean pause   = true;

    ///USED TO "draw()"
    Canvas canvas;
    Paint paint;

    ///FPS FOR ANIMATIONS
    long fps;

    ArrayList<Tetromino> pastTetrominos = new ArrayList<>();

    //constructor
    public TetrisGridView(Context context, int screenX, int screenY) {
        super(context);

        surfaceHolder = getHolder();
        paint = new Paint();

        this.screenX = screenX;
        this.screenY = screenY;

        randomTetromino();
    }

    @Override
    public void run() {
        while(running){

            long startFrame = System.currentTimeMillis();

            //Update the frame
            if(!pause)
                update();

            //Draw the frame
            draw();
            long timeThisFrame = System.currentTimeMillis() - startFrame;
            //Calculate the fps
            if(timeThisFrame >=1)
                fps = 1000 / timeThisFrame;

            //GAVE THE DOWN INTERRUPTION ANIMATION
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        if (!pastTetrominos.get(pastTetrominos.size()-1).update(fps)) {
            randomTetromino();
        }
    }

    public void draw() {

        // Make sure our drawing surface is valid or we crash
        if (surfaceHolder.getSurface().isValid()) {
            ///LOCK THE CANVAS TO DRAW
            canvas = surfaceHolder.lockCanvas();

            ///BACKGROUND COLOR
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            //Draw the tetromino
            paint.setColor(pastTetrominos.get(pastTetrominos.size()-1).getColor());
            canvas.drawRect(pastTetrominos.get(pastTetrominos.size()-1).getRect(), paint);

            if(!(pastTetrominos.get(pastTetrominos.size()-1) instanceof Block_I || pastTetrominos.get(pastTetrominos.size()-1) instanceof Block_O))
                canvas.drawRect(pastTetrominos.get(pastTetrominos.size()-1).getRect2(), paint);


            if(pastTetrominos.size()>0) {


                //DRAW PASSED TETROMINOS
                for(int i=0; i<pastTetrominos.size(); i++){
                    paint.setColor(pastTetrominos.get(i).getColor());
                    canvas.drawRect(pastTetrominos.get(i).getRect2(), paint);
                    canvas.drawRect(pastTetrominos.get(i).getRect(), paint);
                }
            }
            ///DRAW EVERYTHING ON SCREEN
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    //METHOD THAT RANDOMS THE TETROMINO
    public void randomTetromino(){

        Random random = new Random();
        int idBlock = random.nextInt(7);

        if(idBlock==0) {
            pastTetrominos.add( new Block_I(screenX, screenY));
        }if(idBlock==1) {
            pastTetrominos.add( new Block_O(screenX, screenY));
        }if(idBlock==2) {
            pastTetrominos.add( new Block_L(screenX, screenY));
        }if(idBlock==3) {
            pastTetrominos.add( new Block_J(screenX, screenY));
        }if(idBlock==4) {
            pastTetrominos.add( new Block_T(screenX, screenY));
        }if(idBlock==5) {
            pastTetrominos.add( new Block_S(screenX, screenY));
        }if(idBlock==6) {
            pastTetrominos.add( new Block_Z(screenX, screenY));
        }
    }

    ///ON PAUSE WE CLOSE THE THREAD
    public void onPause(){
        running = false;
        try{
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    ///ON RESUME WE CREATE ANOTHER THREAD
    public void onResume(){
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    //I CHOOSE USE ::onTouchEvent FROM SURFACEVIEW INSTEAD OF
    //GESTURELISTENER FROM THE CLASSES DUE TO EXTENSION OF CODE
    //AND UNECESSARY OVERRIDE METHODS
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //PLAYER TOUCH SCREEN
            case MotionEvent.ACTION_DOWN:
                pause = false;
                //IF THE USER PRESS RIGHT SIDE OF THE SCREEN
                //'2' REPRESENTS RIGHT ON CLASS
                if(event.getX() > screenX/2)
                    pastTetrominos.get(pastTetrominos.size()-1).setMovement(2);
                //IF THE USER PRESS LEFT SIDE OF THE SCREEN
                //'1' REPRESENTS LEFT ON CLASS
                else
                    pastTetrominos.get(pastTetrominos.size()-1).setMovement(1);

                break;

            //PLAYER REMOVE THE FINGER FROM SCREEN
            case MotionEvent.ACTION_UP:
                pastTetrominos.get(pastTetrominos.size()-1).setMovement(0);
                break;
        }
        return true;
    }

}
