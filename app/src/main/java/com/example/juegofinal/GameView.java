package com.example.juegofinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import java.io.IOException;


public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    private Paint paint;
    private GameActivity activity;
    private TileMapDraw renderer;
    private Bitmap bg;
    private TileMap map;
    private ResourceManager manager;
    public static int tile_size = 256;
    public static int tile_bit = 8;
    public String pac;

    public GameView(GameActivity activity, int screenX, int screenY, int tileS)  {
        super(activity);


        this.activity = activity;
        pac = activity.getPackageName();

        this.screenX = screenX;
        this.screenY = screenY;

        tile_size = tileS;
       // tile_bit = Math.log(tile_size)/Math.log(2);

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);


        //create manager
        manager = new ResourceManager(this);

        //load first map ?


        try {
            map = manager.loadMap(R.raw.map1, getResources());
            map.setGrid();
        }
        catch (IOException ex) {
            // no maps to load!
            System.out.println("no maps");

        }

        renderer = new TileMapDraw(manager.getBackground());

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
        if(map.getEnemyN()==0 && Rect.intersects(map.getPlayer().getCollisionShape(), map.getGoal().getCollisionShape())){
            goToWin();
        }
        if(map.getPlayer().getHealth()<=0){
            goToLose();
        }


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

    public void goToWin () {
        activity.startActivity(new Intent(activity, Ganaste.class));
        activity.finish();
    }

    public void goToLose () {
        activity.startActivity(new Intent(activity, Perdiste.class));
        activity.finish();
    }

}