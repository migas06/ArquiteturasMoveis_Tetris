package com.isec.tetris;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.isec.tetris.DataScoresRelated.Score;
import com.isec.tetris.Tetrominoes.Block_I;
import com.isec.tetris.Tetrominoes.Block_J;
import com.isec.tetris.Tetrominoes.Block_L;
import com.isec.tetris.Tetrominoes.Block_O;
import com.isec.tetris.Tetrominoes.Block_S;
import com.isec.tetris.Tetrominoes.Block_T;
import com.isec.tetris.Tetrominoes.Block_Z;
import com.isec.tetris.bad_Logic.TetrisMap;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Miguel on 14-11-2016.
 */

public class TetrisGridView extends SurfaceView implements Runnable, SensorEventListener {

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

    boolean accelerometer;

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

    int level;
    Score score;

    SensorManager sensorManager;
    Sensor sensor;
    float sensorX = 0;
    float sensorY = 0;



    //constructor
    public TetrisGridView(Context context, int screenX, int screenY, Sensor sensor, SensorManager sensorManager) {
        super(context);

        this.context = context;
        surfaceHolder = getHolder();

        paint = new Paint();

        this.sensorManager = sensorManager;
        this.sensor = sensor;

        this.screenX = screenX;
        this.screenY = screenY;
        this.unit = screenX/15;

        tetrisMap = new TetrisMap();
        playNr=0;
        randomTetromino();

        bitmapRotate = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotate);
        bitmapBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_app);
        createBit();
        sharedPreferenceValues();

        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSPARENT);

        rotate = new RectF(unit*11+10, unit*22, screenX, unit*24);


        System.out.println(screenX +" "+ screenY);
    }

    @Override
    public void run() {
        while(running){
            if(gameOver == true)
                break;

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
                Thread.sleep(level*50);
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
            gameOver=true;
            draw();
            score = new Score(playNr);
            writeScoreIntoFile();
        }
    }

    public void draw() {

        // Make sure our drawing surface is valid or we crash
        if (surfaceHolder.getSurface().isValid()) {
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

    //I CHOOSE USE ::onTouchEvent FROM VIEW INSTEAD OF
    //GESTURELISTENER FROM THE CLASSES DUE TO EXTENSION OF THE CODE
    //AND UNECESSARY OVERRIDE METHODS
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!accelerometer) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                //PLAYER TOUCH SCREEN
                case MotionEvent.ACTION_DOWN:
                    pause = false;

                    if (!gameOver) {

                        RectF rectF = new RectF(event.getX(), event.getY(), event.getX(), event.getY());

                        //IF THE USER PRESS TETROMINO
                        if (rotate.intersect(rectF)) {
                            pastTetrominos.get(pastTetrominos.size() - 1).setMovement(3);
                        }
                        //IF THE USER PRESS RIGHT SIDE OF THE SCREEN
                        //'2' REPRESENTS RIGHT ON CLASS
                        else if (event.getX() > screenX / 2) {
                            pastTetrominos.get(pastTetrominos.size() - 1).setMovement(2);
                            //IF THE USER PRESS LEFT SIDE OF THE SCREEN
                            //'1' REPRESENTS LEFT ON CLASS
                        } else {
                            pastTetrominos.get(pastTetrominos.size() - 1).setMovement(1);
                            if (gameOver)
                                onResume();
                        }
                    } else {
                        try {
                            context.startActivity(new Intent(context, MainActivity.class));
                        } catch (Exception e) {
                            System.out.println("e --->" + e);
                        }
                    }

                    break;

                //PLAYER REMOVE THE FINGER FROM SCREEN
                case MotionEvent.ACTION_UP:
                    pastTetrominos.get(pastTetrominos.size() - 1).setMovement(0);
                    break;
            }

        }
        else
            Toast.makeText(context, getResources().getString(R.string.control_error), Toast.LENGTH_LONG).show();
            return true;

    }

    public void createBit(){
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, bitmapBackground.getWidth(), bitmapBackground.getHeight()),
                new RectF(0, 0, screenX*2, screenY), Matrix.ScaleToFit.CENTER);
        bitmapBackground = Bitmap.createBitmap(bitmapBackground, 0, 0, bitmapBackground.getWidth(), bitmapBackground.getHeight(), matrix, true);

    }

    private void sharedPreferenceValues() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getResources().getString(R.string.shared_preference), Context.MODE_PRIVATE);

        accelerometer = sharedPreferences.getBoolean("accelerometer", false);
        int progress = sharedPreferences.getInt("level", 1);

        int c=1;
        for(int i=10; i>0; i--){
            if(c==progress) {
                c = i;
                break;
            }
        }
        level = c;
    }

    private void writeScoreIntoFile() {

        ArrayList<Score> list = new ArrayList<Score>();
        list = score.readScore();

        try {
            OutputStream fOutputStream = new FileOutputStream(score.getPath());
            OutputStream outputStream = new BufferedOutputStream(fOutputStream);
            ObjectOutput objectOutput = new ObjectOutputStream(outputStream);

            list.add(score);

            objectOutput.writeObject(list);
            objectOutput.close();

        } catch (IOException e) {
            System.out.println(e);
            System.out.println("ERROR! SCORE WILL NOT BE SAVED IN FILE");
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
        sensorManager.registerListener(this, sensor,SensorManager.SENSOR_STATUS_ACCURACY_LOW);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(accelerometer){
            //IF Y AXIS IS -4
            //THE USER PUTS THE DEVICE BUTT IN THE AIR
            if(sensorEvent.values[1]<-0.5){
                //ROTATE
                pastTetrominos.get(pastTetrominos.size() - 1).setMovement(3);

            }

            //IF X AXIS IS OR LESS THAN -5
            //THE USER TILT THE DEVICE FOR IS RIGHT
            else if(sensorEvent.values[0]<-2.5)
                pastTetrominos.get(pastTetrominos.size() - 1).setMovement(2);

            //IF X AXIS IS 5 OR BIGGER
            //THE USER TILT THE DEVICE FOR IS OTHER RIGHT (LEFT OFC)
            else if(sensorEvent.values[0]>2.5)
                pastTetrominos.get(pastTetrominos.size() - 1).setMovement(1);

            //THE USER TILTS THE DEVICE BUTT INTO DOWN
            else if(sensorEvent.values[1]>2.5)
                pause = false;

            //REGULAR MOVEMENT
            else
                pastTetrominos.get(pastTetrominos.size() - 1).setMovement(0);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
