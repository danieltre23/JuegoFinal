package com.example.juegofinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.SurfaceView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);


        //create manager
        manager = new ResourceManager(this);

        //load current map with the manager
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
            // create Grid for the pathfinding
            map.setGrid();

            //set player health with the current value
            map.getPlayer().setHealth(health);
        }
        catch (IOException ex) {
            // no maps to load!
            System.out.println("no maps");

        }

        //create map renderer
        renderer = new TileMapDraw(manager.getBackground());

        //win and lose sounds
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
    public Paint getPaint() {
        return paint;
    }

    /**
     game run, while playing calls the update and draw
     */
    @Override
    public void run() {

        while (isPlaying) {


            update ();
            draw ();
            sleep ();

        }

    }

    /**
     game update needs to update every enemy and the main player
     */
    private void update () {

        //update all assets from map

        Iterator i = map.getEnemies();

        Enemy e;
        List<Enemy> removeEnemies = new ArrayList<Enemy>();

        //enemies update
        while (i.hasNext()) {
            e = (Enemy) i.next();
            e.update();
            if(e.isReadyToRemove()){
                removeEnemies.add(e);
            }
        }

        // safely remove from the linkedlist to not cause an exception
        for(Enemy en : removeEnemies){
            map.removeEnemy(en);
        }

        //player update
        map.getPlayer().update();

        //if player gets to goal with no enemies -> win
        if(map.getEnemyN()==0 && Rect.intersects(map.getPlayer().getCollisionShape(), map.getGoal().getCollisionShape())){
            goToWin();
        }
        //if player dies -> lose
        if(map.getPlayer().getHealth()<=0){
            goToLose();
        }
    }

    /**
    the game draw is made mainly by the TileMapDraw Class so we just call its draw function with the current map
     */
    private void draw () {

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            //use renderer
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

    /**
     resume game
     */
    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    /**
     Play any sound once give the soundpool
     */
    public static void play(SoundPool p , int id, double intensity){
        if(soundOn){
            p.play(id,(float)intensity,(float)intensity,1,0,1);
        }
    }
    /**
     pause game
     */
    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
    start the win activity (screen)
     */
    public void goToWin () {
        map.getPlayer().getSoundPool().autoPause();
        play(soundPool, winSound,0.6);
        Intent nextLevel = new Intent(activity, Ganaste.class);
        nextLevel.putExtra("nivel", nivel);
        nextLevel.putExtra("health", (int)map.getPlayer().getHealth());
        activity.startActivity(nextLevel);
        activity.finish();
    }

    /**
      start the lose activity (screen)
     */
    public void goToLose () {
        map.getPlayer().getSoundPool().autoPause();
        play(soundPool, loseSound,0.6);
        activity.startActivity(new Intent(activity, Perdiste.class));
        activity.finish();
    }

}