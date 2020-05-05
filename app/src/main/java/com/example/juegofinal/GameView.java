package com.example.juegofinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    private Paint paint;
    private GameActivity activity;
    private Button pausebtn;
    private Button perdisteBtn;
    private Button ganasteBtn;
    private TileMapDraw renderer;
    private Bitmap bg;
    private TileMap map;
    private ResourceManager manager;
    private Button []joystick;

    public GameView(GameActivity activity, int screenX, int screenY)  {
        super(activity);

        this.activity = activity;

        this.screenX = screenX;
        this.screenY = screenY;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        /* declare bg */

        bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        Bitmap black = BitmapFactory.decodeResource(getResources(), R.drawable.black);

        renderer = new TileMapDraw(bg, black);



        // declare buttons

        pausebtn = new Button(getResources(), R.drawable.pause, 50,50);
        perdisteBtn = new Button(getResources(), R.drawable.sick, 200, 200 );
        ganasteBtn = new  Button(getResources(), R.drawable.smily, 800, 200);

        joystick = new Button[] {new Button(getResources(), R.drawable.cir, screenX-80*2,screenY -180*2),new Button(getResources(), R.drawable.cir, screenX-80*2,screenY-80*2),new Button(getResources(), R.drawable.cir, screenX -50*2,screenY-130*2),new Button(getResources(), R.drawable.cir, screenX - 110*2,screenY-130*2),new Button(getResources(), R.drawable.cir, 20,screenY-130*2)};

        //create manager
       manager = new ResourceManager(this);

        //load first map ?


        try {
            map = manager.loadMap(R.raw.map1, getResources());
        }
        catch (IOException ex) {
                // no maps to load!
                System.out.println("no maps");

        }



    }
    public TileMapDraw getRenderer(){
        return renderer;
    }

    public TileMap getMap(){
        return map;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public Paint getPaint() {
        return paint;
    }

    @Override
    public void run() {

        while (isPlaying) {


            update ();
            draw ();
            sleep ();

        }

    }

    private void update () {

        //update all assets from map

        map.getPlayer().update();



    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            //draw background
            //canvas.drawBitmap(bg,0,0,paint);

            renderer.draw(canvas, map, screenX, screenY);


            // draw buttons
            pausebtn.draw(canvas);
            for(int i=0; i<4;i++){
                joystick[i].draw(canvas);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (pausebtn.click(event)) {
                    goToPause();
                }

                if(joystick[0].click(event)){
                    map.getPlayer().setDx(0);
                    map.getPlayer().setDy(-1);
                }
                else if(joystick[1].click(event)){
                    map.getPlayer().setDx(0);
                    map.getPlayer().setDy(1);
                }
                else if(joystick[2].click(event)){
                    map.getPlayer().setDx(1);
                    map.getPlayer().setDy(0);
                }
                else if(joystick[3].click(event)){
                    map.getPlayer().setDx(-1);
                    map.getPlayer().setDy(0);
                }
                else if (joystick[4].click(event)){
                    map.getPlayer().setDx(0);
                    map.getPlayer().setDy(0);
                }
                /*if (perdisteBtn.click(event)) {
                    goToLose();
                }
                if (ganasteBtn.click(event)) {
                    goToWin();
                }*/
                break;
        }

        return true;
    }

    public void goToWin () {
        activity.startActivity(new Intent(activity, Ganaste.class));
        activity.finish();
    }

    public void goToLose () {
        activity.startActivity(new Intent(activity, Perdiste.class));
        activity.finish();
    }

    public void goToPause () {
        activity.startActivity(new Intent(activity, Pause.class));
        activity.finish();
    }

}