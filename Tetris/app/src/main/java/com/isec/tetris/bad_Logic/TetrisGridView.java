package com.isec.tetris.bad_Logic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.isec.tetris.DataScoresRelated.Score;
import com.isec.tetris.Multiplayer.SocketHandler;
import com.isec.tetris.R;
import com.isec.tetris.Tetrominoes.Tetromino;
import com.isec.tetris.Tetrominoes.Block_I;
import com.isec.tetris.Tetrominoes.Block_J;
import com.isec.tetris.Tetrominoes.Block_L;
import com.isec.tetris.Tetrominoes.Block_O;
import com.isec.tetris.Tetrominoes.Block_S;
import com.isec.tetris.Tetrominoes.Block_T;
import com.isec.tetris.Tetrominoes.Block_Z;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
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
    boolean lostConnection = false;

    boolean accelerometer;

    //TEMP BUTTON FOR ROTATTING
    RectF rotate;

    ///USED TO "draw()"
    ///PICASSO AND MICHELANGELO TOOLS
    Canvas canvas;
    Paint paint;

    ///FPS FOR ANIMATIONS
    long fps;

    //NEXT TETROMINO
    Tetromino next;

    //(bad) Logic for the game
    TetrisMap tetrisMap;
    TetrisMap oponnentMap;
    int playNr;

    int MY_PERMISSIONS_REQUEST = 0;

    DrawNext drawNext;

    //ALL PAST TETROMINOES
    ArrayList<Tetromino> pastTetrominos = new ArrayList<>();

    Bitmap bitmapRotate;
    Bitmap bitmapBackground;
    Bitmap bitmapGrid;
    Bitmap bitmapOne;
    Bitmap bitmapTwo;
    Bitmap bitmapThree;
    Bitmap bitmapFour;
    Bitmap bitmapFive;
    Bitmap bitmapSix;
    Bitmap bitmapSeven;

    int level;
    Score score;

    SensorManager sensorManager;
    Sensor sensor;

    //MESURE CREATED FOR LATERAL COMPONENTS
    int component;
    int progress;

    SocketHandler app;
    String msgSocket = "nothing";

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
        this.unit = screenY/25;

        tetrisMap = new TetrisMap();
        oponnentMap=new TetrisMap();

        playNr=0;

        bitmapRotate = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotate);
        bitmapBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_app);
        bitmapGrid  = BitmapFactory.decodeResource(context.getResources(), R.drawable.grid_background);


        ArrayList<Bitmap> bitmapsList = new ArrayList<>();

        bitmapOne   = BitmapFactory.decodeResource(context.getResources(), R.drawable.one);
        bitmapTwo   = BitmapFactory.decodeResource(context.getResources(), R.drawable.two);
        bitmapThree = BitmapFactory.decodeResource(context.getResources(), R.drawable.three);
        bitmapFour  = BitmapFactory.decodeResource(context.getResources(), R.drawable.four);
        bitmapFive  = BitmapFactory.decodeResource(context.getResources(), R.drawable.five);
        bitmapSix   = BitmapFactory.decodeResource(context.getResources(), R.drawable.six);
        bitmapSeven = BitmapFactory.decodeResource(context.getResources(), R.drawable.seven);

        bitmapsList.add(bitmapOne);
        bitmapsList.add(bitmapTwo);
        bitmapsList.add(bitmapThree);
        bitmapsList.add(bitmapFour);
        bitmapsList.add(bitmapFive);
        bitmapsList.add(bitmapSix);
        bitmapsList.add(bitmapSeven);

        drawNext = new DrawNext(bitmapsList, unit*2/3);

        pastTetrominos.add(randomTetromino(tetrisMap.getActualTetromino()));
        tetrisMap.setRotation(0);
        tetrisMap.setNext(randomTetromino(tetrisMap.getActualTetromino()));
        tetrisMap.setTetromino(randomTetromino(tetrisMap.getActualTetromino()));
        tetrisMap.setY(0);
        tetrisMap.setX(7);

        createBit();
        sharedPreferenceValues();

        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSPARENT);

        app = (SocketHandler) context.getApplicationContext();

        //MESURE FOR LATERAL COMPONENTS
        component =  (int) (((screenX-unit*10)/2) + unit*10);

        //RECTANGLES FOR INTERSECT
        rotate = new RectF(component, unit*21, screenX, unit*22);
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
                Thread.sleep(level*100*1,5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {

        //tetrisMap.print();
        //oponnentMap.print();
        if(app.getSocket()!=null){
            createOpponentGrid();
        }

        pastTetrominos.get(pastTetrominos.size()-1).update(fps, tetrisMap);

        if (!tetrisMap.update()) {

            if(oponnentMap.getLinesDelete()>0){
                tetrisMap.deleteSpaces(oponnentMap.getLinesDelete());
            }

            tetrisMap.verifyLines();

            tetrisMap.setActualTetromino(tetrisMap.getNextTetromino());
            tetrisMap.setNextTetromino(tetrisMap.random());

            pastTetrominos.add(randomTetromino(tetrisMap.getActualTetromino()));
            tetrisMap.setRotation(0);
            tetrisMap.setNext(randomTetromino(tetrisMap.getActualTetromino()));
            tetrisMap.setTetromino(randomTetromino(tetrisMap.getActualTetromino()));
            tetrisMap.setY(0);
            tetrisMap.setX(7);
        }

        if(tetrisMap.isGameOver() || lostConnection){
            if(app.getSocket()!=null){
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(app.getSocket().getOutputStream());
                    objectOutputStream.writeObject(getResources().getString(R.string.win));
                    objectOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!lostConnection)
                    msgSocket = getResources().getString(R.string.lose);
                else
                    msgSocket = getResources().getString(R.string.lost_connection);

                try {
                    app.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            gameOver=true;
            running=false;
            draw();
            score = new Score((progress*playNr)+tetrisMap.getScore());
            if(app.getSocket()==null)
                writeScoreIntoFile();
        }
    }

    public void draw() {

        // Make sure our drawing surface is valid or we crash
        if (surfaceHolder.getSurface().isValid()) {
            ///LOCK THE CANVAS TO DRAW

            canvas = surfaceHolder.lockCanvas();

            bitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, (int) screenX, (int) screenY, true);
            canvas.drawBitmap(bitmapBackground, 0, 0, paint);

            //Draw the Grid
            drawGrid(canvas);

            //Draw Next Tetrominó
            paint.setTextSize(unit/2);
            canvas.drawText(context.getResources().getString(R.string.next)+": ", component-unit, unit*14 , paint);
            drawNext.drawNextTetromino(canvas, tetrisMap.getNextTetromino(),  component- (int) unit/2, (int) unit*15);


            if(app.getSocket()!=null)
                drawOponnentGrid(canvas);

            //DRAW SCORE
            paint.setColor(Color.WHITE);
            paint.setTextSize(unit/2);
            paint.setStrokeMiter(25);
            canvas.drawText(context.getResources().getString(R.string.score)+": ", component-unit, unit*9 , paint);
            paint.setTextSize(unit);
            canvas.drawText(""+((progress*playNr)+tetrisMap.getScore()), component-unit, unit*10 , paint);

            //rotate
            canvas.drawBitmap(bitmapRotate, component, unit*21, null);

            //GAME OVER THINGS
            //CLOSE SOCKET HERE TOO
            if (gameOver){
                canvas.drawColor(Color.argb(100, 0, 0, 0));
                paint.setColor(Color.WHITE);
                paint.setTextSize(unit*2);
                paint.setStrokeMiter(25);

                if(app.getSocket()==null)
                    canvas.drawText(context.getString(R.string.game_over), unit*2, screenY/2, paint);
                else {
                    canvas.drawText(msgSocket, unit * 2, screenY / 2, paint);
                }
            }

            ///DRAW EVERYTHING ON SCREEN
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawGrid(Canvas canvas) {

        boolean control = false;

        bitmapGrid = Bitmap.createScaledBitmap(bitmapGrid, (int) unit, (int) unit, true);
        bitmapOne   = Bitmap.createScaledBitmap(bitmapOne, (int) unit, (int) unit, true);
        bitmapTwo   = Bitmap.createScaledBitmap(bitmapTwo, (int) unit, (int) unit, true);
        bitmapThree = Bitmap.createScaledBitmap(bitmapThree, (int) unit, (int) unit, true);
        bitmapFour  = Bitmap.createScaledBitmap(bitmapFour, (int) unit, (int) unit, true);
        bitmapFive  = Bitmap.createScaledBitmap(bitmapFive, (int) unit, (int) unit, true);
        bitmapSix   = Bitmap.createScaledBitmap(bitmapSix, (int) unit, (int) unit, true);
        bitmapSeven = Bitmap.createScaledBitmap(bitmapSeven, (int) unit, (int) unit, true);

        int [][] map = tetrisMap.getMap();
        int top = 25;
        int left = 25;

        for(int i = 0; i<22; i++){

            for(int j=3; j<13; j++){

                if(map[i][j]==0)
                    canvas.drawBitmap(bitmapGrid, left, top, null);

                if(map[i][j]>10) {
                    map[i][j] = pastTetrominos.get(pastTetrominos.size() - 1).getFId();
                    control = true;
                }
                    if (map[i][j] == 1)
                        canvas.drawBitmap(bitmapOne, left, top, null);
                    if (map[i][j] == 2)
                        canvas.drawBitmap(bitmapTwo, left, top, null);
                    if (map[i][j] == 3)
                        canvas.drawBitmap(bitmapThree, left, top, null);
                    if (map[i][j] == 4)
                        canvas.drawBitmap(bitmapFour, left, top, null);
                    if (map[i][j] == 5)
                        canvas.drawBitmap(bitmapFive, left, top, null);
                    if (map[i][j] == 6)
                        canvas.drawBitmap(bitmapSix, left, top, null);
                    if (map[i][j] == 7)
                        canvas.drawBitmap(bitmapSeven, left, top, null);


                    if(control){
                        map[i][j]=playNr+10;
                        control = false;
                    }
                    left+=unit;
                }
                left=25;
                top+=unit;

            }



    }


    //DEPENDING ON USER _ CLIENT OR SERVER
    //SERVER ALWAYS START SENDING TETRISMAP (FROM LOGIC)
    //NEXT SERVER AWAITS FOR CLIENT MAP
    //CLIENT FOR HIS TURN RECEIVE THE MAP, AND AFTER RECEIVE
    //SENDS HIS OWN
    private void createOpponentGrid() {

        if(app.getUser().equals("Client")){

            //READ MAP FROM SERVER;

            try{
                ObjectInputStream objectInputStream = new ObjectInputStream(app.getSocket().getInputStream());
                Object objectReceived = objectInputStream.readObject();


                if(objectReceived instanceof TetrisMap)
                    oponnentMap = (TetrisMap) objectReceived;
                else{
                    msgSocket = (String) objectReceived;
                    gameOver=true;
                }

            }catch(ClassNotFoundException e) {
                System.out.println("Exception e: "+e);
            } catch (InterruptedIOException e){
                lostConnection = true;
            } catch (IOException e) {
                System.out.println("Receive Map Exception e: " +e);
            }

            //WRITE MAP TO SERVER
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(app.getSocket().getOutputStream());
                objectOutputStream.writeObject(tetrisMap);
                objectOutputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(app.getUser().equals("Server")){


            //WRITE MAP TO CLIENT
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(app.getSocket().getOutputStream());
                objectOutputStream.writeObject(tetrisMap);
                objectOutputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

            //READ MAP FROM CLIENT;
            try{
                ObjectInputStream objectInputStream = new ObjectInputStream(app.getSocket().getInputStream());
                Object objectReceived = objectInputStream.readObject();

                if(objectReceived instanceof TetrisMap)
                    oponnentMap = (TetrisMap) objectReceived;
                else{
                    msgSocket = (String) objectReceived;
                    gameOver=true;
                }
            }catch(ClassNotFoundException e) {
                System.out.println("Exception e: " + e);
            }catch (InterruptedIOException e){
                    lostConnection = true;
            } catch (IOException e) {
                System.out.println("Receive Map Exception e: " +e);
            }
        }
    }

    //MULTIPLAYER BOARD DRAW
    private void drawOponnentGrid(Canvas canvas) {

        Bitmap oBitmapGrid = Bitmap.createScaledBitmap(bitmapGrid, (int) unit/5, (int) unit/5, true);
        Bitmap oBitmapOne   = Bitmap.createScaledBitmap(bitmapOne, (int) unit/5, (int) unit/5, true);
        Bitmap oBitmapTwo   = Bitmap.createScaledBitmap(bitmapTwo, (int) unit/5, (int) unit/5, true);
        Bitmap oBitmapThree = Bitmap.createScaledBitmap(bitmapThree, (int) unit/5, (int) unit/5, true);
        Bitmap oBitmapFour  = Bitmap.createScaledBitmap(bitmapFour, (int) unit/5, (int) unit/5, true);
        Bitmap oBitmapFive  = Bitmap.createScaledBitmap(bitmapFive, (int) unit/5, (int) unit/5, true);
        Bitmap oBitmapSix   = Bitmap.createScaledBitmap(bitmapSix, (int) unit/5, (int) unit/5, true);
        Bitmap oBitmapSeven = Bitmap.createScaledBitmap(bitmapSeven, (int) unit/5, (int) unit/5, true);

        int [][] omap = oponnentMap.getMap();
        int top = 25;
        int left = component;

        for(int i = 0; i<22; i++){
            for(int j=3; j<13; j++){

                if(omap[i][j]==0 || omap[i][j] > 7)
                    canvas.drawBitmap(oBitmapGrid, left, top, null);

                if (omap[i][j] == 1)
                    canvas.drawBitmap(oBitmapOne , left, top, null);
                if (omap[i][j] == 2)
                    canvas.drawBitmap(oBitmapTwo, left, top, null);
                if (omap[i][j] == 3)
                    canvas.drawBitmap(oBitmapThree, left, top, null);
                if (omap[i][j] == 4)
                    canvas.drawBitmap(oBitmapFour, left, top, null);
                if (omap[i][j] == 5)
                    canvas.drawBitmap(oBitmapFive, left, top, null);
                if (omap[i][j] == 6)
                    canvas.drawBitmap(oBitmapSix, left, top, null);
                if (omap[i][j] == 7)
                    canvas.drawBitmap(oBitmapSeven, left, top, null);

                left+=unit/5;
            }
            left = component;
            top+=unit/5;
        }

    }

    //METHOD THAT RANDOMS THE TETROMINO
    public Tetromino randomTetromino(int idBlock){
        playNr++;

        if(idBlock==0) {
            return new Block_I(screenX, screenY, playNr, unit);
        }if(idBlock==1) {
            return new Block_O(screenX, screenY, playNr, unit);
        }if(idBlock==2) {
            return new Block_L(screenX, screenY, playNr, unit);
        }if(idBlock==3) {
            return new Block_J(screenX, screenY, playNr, unit);
        }if(idBlock==4) {
            return new Block_T(screenX, screenY, playNr, unit);
        }if(idBlock==5) {
            return new Block_S(screenX, screenY, playNr, unit);
        }if(idBlock==6) {
            return new Block_Z(screenX, screenY, playNr, unit);
        }
        return null;
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
                        RectF rectF = new RectF(event.getX(), event.getY(), event.getX()+1, event.getY()+1);
                        //IF THE USER PRESS TETROMINO
                        if (rectF.intersect(rotate)) {
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

                            if (gameOver){
                                onResume();
                                try {
                                    app.getSocket().close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        try {
                            app.setSocket(null);
                            ((Activity) context).finish();
                        } catch (Exception e) {
                            System.out.println("" + e);
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
            Toast.makeText(context, getResources().getString(R.string.control_error), Toast.LENGTH_SHORT).show();
            return true;

    }

    //CREATE A BACKGROUND IMAGE FOR WITH SCREEN DIMENSIONS
    public void createBit(){
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, bitmapBackground.getWidth(), bitmapBackground.getHeight()),
                new RectF(0, 0, screenX*2, screenY), Matrix.ScaleToFit.CENTER);
        bitmapBackground = Bitmap.createBitmap(bitmapBackground, 0, 0, bitmapBackground.getWidth(), bitmapBackground.getHeight(), matrix, true);

    }

    //GET USER SETTINGS
    private void sharedPreferenceValues() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getResources().getString(R.string.shared_preference), Context.MODE_PRIVATE);

        accelerometer = sharedPreferences.getBoolean("accelerometer", false);
        progress = sharedPreferences.getInt("level", 1);

        int c=1;
        for(int i=10; i>0; i--){
            if(c==progress) {
                if (i != 10) {
                    c = i * i;
                }else {
                    c = i;
                }
                break;
            }
        }
        level = c;
    }

    private void writeScoreIntoFile() {

        //Toast.makeText(context, getResources().getString(R.string.request), Toast.LENGTH_LONG).show();

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
            }
        }

        ArrayList<Score> list = new ArrayList<Score>();

        if(score.readScore() != null)
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

    ///ON RESUME WE CREATE A THREAD
    public void onResume(){
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
        sensorManager.registerListener(this, sensor,SensorManager.SENSOR_STATUS_ACCURACY_LOW);
    }

    //CODE RELATIVE TO ACCELEROMETER
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
