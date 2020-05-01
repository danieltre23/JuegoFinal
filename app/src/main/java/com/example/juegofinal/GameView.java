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
    private PauseButton pausebtn;
    private perdisteButton perdisteBtn;
    private ganasteButton ganasteBtn;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        this.screenX = screenX;
        this.screenY = screenY;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        background1 = new Background(screenX, screenY, getResources());
        pausebtn = new PauseButton(getResources());
        perdisteBtn = new perdisteButton(getResources());
        ganasteBtn =new  ganasteButton(getResources());


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
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(pausebtn.pause, pausebtn.x, pausebtn.y, paint);
            canvas.drawBitmap(ganasteBtn.ganaste, ganasteBtn.x, ganasteBtn.y, paint);
            canvas.drawBitmap(perdisteBtn.perdiste, perdisteBtn.x, perdisteBtn.y, paint);

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
                if (event.getX() > pausebtn.x && event.getX() < pausebtn.x + 100 && event.getY() > pausebtn.y && event.getY() < pausebtn.y + 100) {
                    goToPause();
                }
                if (event.getX() > perdisteBtn.x && event.getX() < perdisteBtn.x + 100 && event.getY() > perdisteBtn.y && event.getY() < perdisteBtn.y + 100) {
                    goToLose();
                }
                if (event.getX() > ganasteBtn.x && event.getX() < ganasteBtn.x + 100 && event.getY() > ganasteBtn.y && event.getY() < ganasteBtn.y + 100) {
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