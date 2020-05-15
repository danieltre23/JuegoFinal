package com.example.juegofinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;
import java.io.IOException;


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
    public static int tile_size = 256;
    public static int tile_bit = 8;
    public String pac;

    public GameView(GameActivity activity, int screenX, int screenY)  {
        super(activity);


        this.activity = activity;
        pac = activity.getPackageName();

        this.screenX = screenX;
        this.screenY = screenY;

        tile_size = screenX/5;
       // tile_bit = Math.log(tile_size)/Math.log(2);

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

        //tile update in mapRenderer

        map.getPlayer().update();


        //int y = -1, w = map.getWidth(), x, h = map.getHeight();

       /* while(y++<h){
            x=-1;
            while(x++<w){
                Tile t = map.getTile(x,y);
                if(t!=null){
                    t.update();
                }
            }
        }*/





    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            //draw background
            //canvas.drawBitmap(bg,0,0,paint);

            renderer.draw(canvas, map, screenX, screenY);


            // draw buttons
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