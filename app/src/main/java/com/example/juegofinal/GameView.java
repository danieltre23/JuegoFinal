package com.example.juegofinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    private Paint paint;
    private GameActivity activity;
    private Background background1;
    private Button pausebtn;
    private Button perdisteBtn;
    private Button ganasteBtn;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        this.screenX = screenX;
        this.screenY = screenY;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        // declare bg
        background1 = new Background(screenX, screenY, getResources());

        // declare buttons
        pausebtn = new Button(getResources(), R.drawable.pause, 50,50);
        perdisteBtn = new Button(getResources(), R.drawable.sick, 200, 200 );
        ganasteBtn =new  Button(getResources(), R.drawable.smily, 800, 200);


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

    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            //draw background
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);

            // draw buttons
            perdisteBtn.draw(canvas);
            ganasteBtn.draw(canvas);
            pausebtn.draw(canvas);

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
                if (perdisteBtn.click(event)) {
                    goToLose();
                }
                if (ganasteBtn.click(event)) {
                    goToWin();
                }
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