package com.example.juegofinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceView;
import java.io.IOException;

import static com.example.juegofinal.MainActivity.soundOn;


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
    private int winSound, loseSound;
    private SoundPool soundPool;
    private int nivel;

    public GameActivity getActivity(){
        return activity;
    }

    public GameView(GameActivity activity, int screenX, int screenY, int tileS, int nivel, int health)  {
        super(activity);

        this.nivel = nivel;
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
            switch (nivel){
                case 0:
                    map = manager.loadMap(R.raw.map0, getResources());
                    break;
                case 1:
                    map = manager.loadMap(R.raw.map1, getResources());
                    break;
                case 2:
                    map = manager.loadMap(R.raw.map2, getResources());
                    break;
                case 3:
                    map = manager.loadMap(R.raw.map3, getResources());
                    break;
                case 4:
                    map = manager.loadMap(R.raw.map4, getResources());
                    break;
                case 5:
                    map = manager.loadMap(R.raw.map5, getResources());
                    break;
                case 6:
                    map = manager.loadMap(R.raw.map6, getResources());
                    break;
                case 7:
                    map = manager.loadMap(R.raw.map7, getResources());
                    break;
                case 8:
                    map = manager.loadMap(R.raw.map8, getResources());
                    break;
                case 9:
                    map = manager.loadMap(R.raw.map9, getResources());
                    break;
                case 10:
                    map = manager.loadMap(R.raw.map10, getResources());
                    break;
            }
            map.setGrid();
            System.out.println("El health es " + health);
            map.getPlayer().setHealth(health);
        }
        catch (IOException ex) {
            // no maps to load!
            System.out.println("no maps");

        }

        renderer = new TileMapDraw(manager.getBackground());
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        winSound = soundPool.load(getActivity(), R.raw.win, 1);
        loseSound = soundPool.load(getActivity(),R.raw.lose,1);

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

    public static void play(SoundPool p , int id, double intensity){
        if(soundOn){
            p.play(id,(float)intensity,(float)intensity,1,0,1);
        }
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
        map.getPlayer().getSoundPool().autoPause();
        play(soundPool, winSound,0.6);
        Intent nextLevel = new Intent(activity, Ganaste.class);
        nextLevel.putExtra("nivel", nivel);
        System.out.println("El health es " + map.getPlayer().getHealth());
        nextLevel.putExtra("health", (int)map.getPlayer().getHealth());
        activity.startActivity(nextLevel);
        activity.finish();
    }

    public void goToLose () {
        map.getPlayer().getSoundPool().autoPause();
        play(soundPool, loseSound,0.6);
        activity.startActivity(new Intent(activity, Perdiste.class));
        activity.finish();
    }

}