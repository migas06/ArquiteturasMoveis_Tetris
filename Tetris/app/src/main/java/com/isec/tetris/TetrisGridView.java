package com.isec.tetris;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
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
import com.isec.tetris.logic.TetrisMap;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Miguel on 14-11-2016.
 */

public class TetrisGridView extends SurfaceView implements Runnable {

    Thread gameThread = null;

    ///USED WHEN WE paint and canvas IN THREAD
    SurfaceHolder surfaceHolder;
    Context context;

    //DISPLAY SETTINGS
    float screenX, screenY;
    float unit;

    boolean running = false;
    boolean pause   = true;
    boolean gameOver = false;

    //TEMP BUTTON FOR ROTATTING
    RectF rotate;

    ///USED TO "draw()"
    ///PICASSO AND MICHELANGELO TOOLS
    Canvas canvas;
    Paint paint;

    ///FPS FOR ANIMATIONS
    long fps;

    //(bad) Logic for the game
    TetrisMap tetrisMap;
    int playNr;

    //ALL PAST TETROMINOES
    ArrayList<Tetromino> pastTetrominos = new ArrayList<>();

    Bitmap bitmapRotate;
    Bitmap bitmapBackground;

    //constructor
    public TetrisGridView(Context context, int screenX, int screenY) {
        super(context);

        this.context = context;
        surfaceHolder = getHolder();

        paint = new Paint();

        this.screenX = screenX;
        this.screenY = screenY;
        this.unit = screenX/15;

        tetrisMap = new TetrisMap();
        playNr=0;
        randomTetromino();

        bitmapRotate = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotate);
        bitmapBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_app);
        createBit();

        rotate = new RectF(unit*11+10, unit*22, screenX, unit*24);
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
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {

        tetrisMap.print();

        pastTetrominos.get(pastTetrominos.size()-1).update(fps, tetrisMap);

        if (!tetrisMap.update()) {
            randomTetromino();
        }

        if(tetrisMap.isGameOver()){
            try {
                gameOver=true;
                draw();
                gameThread.join();
                context.startActivity(new Intent(context, MainActivity.class));
            } catch (InterruptedException e) {
                System.out.println(e);
            }

        }
    }

    public void draw() {

        // Make sure our drawing surface is valid or we crash
        if (surfaceHolder.getSurface().isValid()) {

            System.out.println(screenX +" "+ screenY);
            ///LOCK THE CANVAS TO DRAW
            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(bitmapBackground, 0,0, null);

            //Draw the Grid
            RectF grid = new RectF(25,0,unit*11, unit*24);
            paint.setColor(Color.argb(200, 26, 128, 182));
            canvas.drawRect(grid, paint);

            //rotate
            canvas.drawBitmap(bitmapRotate, unit*12, unit*22, null);

            paint.setColor(pastTetrominos.get(pastTetrominos.size()-1).getColor());
            canvas.drawRect(pastTetrominos.get(pastTetrominos.size()-1).getRect(), paint);

            //THE RECTF WHO HAVE MORE THAN 1 RECT
            if (!(pastTetrominos.get(pastTetrominos.size() - 1) instanceof Block_I || pastTetrominos.get(pastTetrominos.size() - 1) instanceof Block_O))
                canvas.drawRect(pastTetrominos.get(pastTetrominos.size() - 1).getRect2(), paint);

            if(pastTetrominos.size()>0) {

                //DRAW PASSED TETROMINOS
                for(int i=0; i<pastTetrominos.size(); i++){
                    paint.setColor(pastTetrominos.get(i).getColor());
                    canvas.drawRect(pastTetrominos.get(i).getRect2(), paint);
                    canvas.drawRect(pastTetrominos.get(i).getRect(), paint);
                }
            }

            if (gameOver){
                canvas.drawColor(Color.argb(100, 0, 0, 0));
                paint.setColor(Color.WHITE);
                paint.setTextSize(unit*2);
                paint.setStrokeMiter(25);
                canvas.drawText(context.getString(R.string.game_over), unit*2, screenY/2, paint);
            }

            ///DRAW EVERYTHING ON SCREEN
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    //METHOD THAT RANDOMS THE TETROMINO
    public void randomTetromino(){
        playNr++;
        Random random = new Random();
        int idBlock = random.nextInt(7);

        idBlock=0;

        Tetromino tetromino = null;
        if(idBlock==0) {
            tetromino= new Block_I(screenX, screenY, playNr, unit);
        }if(idBlock==1) {
            tetromino= new Block_O(screenX, screenY, playNr, unit);
        }if(idBlock==2) {
            tetromino= new Block_L(screenX, screenY, playNr, unit);
        }if(idBlock==3) {
            tetromino= new Block_J(screenX, screenY, playNr, unit);
        }if(idBlock==4) {
            tetromino= new Block_T(screenX, screenY, playNr, unit);
        }if(idBlock==5) {
            tetromino= new Block_S(screenX, screenY, playNr, unit);
        }if(idBlock==6) {
            tetromino= new Block_Z(screenX, screenY, playNr, unit);
        }

        pastTetrominos.add(tetromino);
        tetrisMap.setNext(tetromino);
        tetrisMap.setTetromino(tetromino);
        tetrisMap.setY(0);
        tetrisMap.setX(6);

    }

    //I CHOOSE USE ::onTouchEvent FROM SURFACEVIEW INSTEAD OF
    //GESTURELISTENER FROM THE CLASSES DUE TO EXTENSION OF THE CODE
    //AND UNECESSARY OVERRIDE METHODS
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //PLAYER TOUCH SCREEN
            case MotionEvent.ACTION_DOWN:
                pause = false;

                RectF rectF = new RectF(event.getX(), event.getY(), event.getX(), event.getY());

                //IF THE USER PRESS TETROMINO
                if(rotate.intersect(rectF)){
                    pastTetrominos.get(pastTetrominos.size() - 1).setMovement(3);
                }
                //IF THE USER PRESS RIGHT SIDE OF THE SCREEN
                //'2' REPRESENTS RIGHT ON CLASS
                else if(event.getX() > screenX/2) {
                    pastTetrominos.get(pastTetrominos.size() - 1).setMovement(2);
                    //IF THE USER PRESS LEFT SIDE OF THE SCREEN
                    //'1' REPRESENTS LEFT ON CLASS
                }else {
                    pastTetrominos.get(pastTetrominos.size() - 1).setMovement(1);
                    if(gameOver)
                        onResume();
                }
                break;

            //PLAYER REMOVE THE FINGER FROM SCREEN
            case MotionEvent.ACTION_UP:
                pastTetrominos.get(pastTetrominos.size()-1).setMovement(0);
                break;
        }
        return true;
    }

    public void createBit(){
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, bitmapBackground.getWidth(), bitmapBackground.getHeight()),
                new RectF(0, 0, screenX*2, screenY), Matrix.ScaleToFit.CENTER);
        bitmapBackground = Bitmap.createBitmap(bitmapBackground, 0, 0, bitmapBackground.getWidth(), bitmapBackground.getHeight(), matrix, true);

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

}
