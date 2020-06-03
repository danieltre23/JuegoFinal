package com.example.juegofinal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;

import static com.example.juegofinal.GameView.tile_size;

public class Player extends Sprite {


    private boolean playing;
    private Animation curr;
    private Animation last;
    private Animation []anims;
    private double health;
    private double fullHealth;
    private Paint paint;
    private long timeHurting = 0;
    private long timeToHurt = 500;
    private boolean hurting;
    private double range = 22.5;
    private LinkedList<Bullet> bullets;
    private Bitmap bul;
    private int bulletsTimer;
    private int bulletsRate;
    private SoundPool soundPool;
    private int sound1,sound2,sound3,sound4;


    public double getHealth() {
        return health;
    }

    public void setHealth(int health1) {
        if(health1<health){
            GameView.play(soundPool,sound3,0.6);
        }
        health = health1;
        health = Math.max(health,0);

    }

    public double getFullHealth() {
        return fullHealth;
    }

    public void setFullHealth(int fullHealth) {
        this.fullHealth = fullHealth;
    }

    private int dx,dy;

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public void addBullet(int angle){
        GameView.play(soundPool,sound2,0.6);
        playing = false;
        bullets.add(new Bullet(x+getWidth()/2, y+getHeight()/2, game, angle, 65,bul,tile_size/2, (int)(((float)getHeight()/getWidth())*(tile_size/2))));
    }

    public void hit() {
        setHealth((int) (health-20));
    }

    private int dir;


    Player(GameView game, int x1, int y1, int w, int h, Animation []a){
        super(x1,y1,w,h, w/2,(int)(h*1.2/2.0),game);
        dx=0;
        dy=0;
        dir = 0;
        anims = a;
        curr = anims[dir];
        fullHealth = 500;
        health = fullHealth;
        paint = new Paint();
        hurting = false;
        bullets = new LinkedList();
        bulletsRate = 25;
        bulletsTimer = bulletsRate;
        bul = BitmapFactory.decodeResource(game.getResources(),R.drawable.aguja);

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        playing  = false;

        sound1 = soundPool.load(game.getActivity(), R.raw.walk, 1);  //walking
        sound2 = soundPool.load(game.getActivity(), R.raw.coin, 1);  //shooting
        sound3 = soundPool.load(game.getActivity(),R.raw.cough, 1);  //playerHurt
        sound4 = soundPool.load(game.getActivity(), R.raw.powerup2,1); //powerup
    }

    public SoundPool getSoundPool(){
        return soundPool;
    }

    public Animation getAnim(){
        return curr;
    }

    /*
    1 2 3
    4 0 5
    6 7 8


  */

    public void updateDir(int angle){

        if((angle>0 && angle<range) || (angle>315+range && angle<=359)){
            dir = 5;
        }
        else if(angle<270+range && angle>270-range){
            dir =7;
        }
        else if(angle>180-range && angle<180+range){
            dir=4;
        }
        else if(angle>90-range && angle<90+range){
            dir =2;
        }
        else if(angle>135-range && angle<135+range){
            dir =1;
        }
        else if(angle>45-range && angle<45+range){
            dir=3;
        }
        else if(angle>225-range && angle<225+range){
            dir = 6;
        }
        else if(angle>315-range && angle<315+range){
            dir=8;
        }

    }


    @Override
    public void update() {


        // if moving
        if((dx!=0 || dy!=0)){
            if(!playing) {
                GameView.play(soundPool,sound1,0.6);
                playing = true;
            }
        }else if(game.getMap().getEnemyN()==0){
            GameView.play(soundPool,sound2,0);
            playing = false;
        }

        //update hurting flag
        if(hurting && System.currentTimeMillis()-timeHurting>timeToHurt){
            hurting  = false;
        }
        //update x and y and curr

        float newX, newY;

        newX = x + dx * (tile_size / 3)/6;
        newY = y + dy * (tile_size / 3)/6;


        //collision with tiles and end of map

        //x movement check
        Point tile = getTileCollision(newX, y);

        if (tile == null) {
            x = (int) newX;
        } else {
            // line up with the tile boundary
            if (dx > 0) {
                x = game.getRenderer().tilesToPixels(tile.x) - realW - ((getWidth() - realW) / 2);
            } else if (dx < 0) {
                x = game.getRenderer().tilesToPixels(tile.x + 1) - ((getWidth() - realW) / 2);
            }

            dx = 0;
        }

        //y movement check
        tile = getTileCollision(x, newY);

        if (tile == null) {
            y = (int) newY;
        } else {
            // line up with the tile boundary
            if (dy > 0) {
                y = game.getRenderer().tilesToPixels(tile.y) - realH - ((getHeight() - realH) / 2);
            } else if (dy < 0) {
                y = game.getRenderer().tilesToPixels(tile.y + 1) - ((getHeight() - realH) / 2);
            }

            dy = 0;
        }

        //enemies collision check
        Iterator i = game.getMap().getEnemies();

        int minDistance = 2147483647;
        int newAngle = 0;
        Enemy e;

        while (i.hasNext()) {

            e = (Enemy) i.next();

            e.update();
            if (!hurting && !e.isDying() && Rect.intersects(e.getCollisionShape(), getCollisionShape())) {
                // posible sonido o animacion
                setHealth((int) (health-20));
                timeHurting = System.currentTimeMillis();
                hurting = true;
            }
            if(dx == 0 && dy == 0 && !e.isDying()){
                if(minDistance > getDistance(e)){
                    minDistance = getDistance(e);
                    newAngle = getAngle(e);
                }
            }
        }

        if(bulletsTimer < bulletsRate){
            bulletsTimer += 1;
        }

        if(dx == 0 && dy == 0) {
            updateDir(newAngle);
            if(bulletsTimer >= bulletsRate && !game.getMap().noEnemies()) {
                addBullet(newAngle);
                bulletsTimer = 0;
            }
            else if(game.getMap().noEnemies()){
                dir=0;
            }
        }

        curr = anims[dir];

        if (last != curr) {
            curr.start();
        }

        if(dx != 0 || dy != 0 || (dx==0 && dy==0 && game.getMap().noEnemies())) {
            curr.update();
            last = curr;
        }

        Iterator it = bullets.iterator();

        while (it.hasNext()) {
            Bullet b = (Bullet) it.next();
            boolean col = b.update2();

            Iterator ie = game.getMap().getEnemies();
            if (!col) {
                while (ie.hasNext()) {
                    Enemy en = (Enemy) ie.next();
                    if (Rect.intersects(en.getCollisionShape(), b.getCollisionShape())) {
                        bullets.remove(b);
                        en.damage(20.0 * 3);
                        if (en.getHealth() <= 0) {
                            en.kill();
                        }
                    }
                }

            }
            else{
                bullets.remove(b);
            }
        }

        //powerups


        Iterator itP = game.getMap().getPowerUps();


        while(itP.hasNext()){
            PowerUp P = (PowerUp)itP.next();
            if(Rect.intersects(getCollisionShape(),P.getCollisionShape())){
                GameView.play(soundPool,sound4,1);
                health += (int)((fullHealth - health)*0.7);
                game.getMap().removePowerUp(P);
            }
        }


    }


    @Override
    public void draw(Canvas g, int offsetX, int offsetY){
        int xP = Math.round(getX())+offsetX;
        int yP = Math.round(getY())+offsetY;

        g.drawBitmap(curr.getCurrentFrame(), xP, yP, game.getPaint());

        yP-=20;
        //health bar bg
        paint.setColor(Color.LTGRAY);
        g.drawRect(new Rect(xP,yP,xP+getWidth(), yP+ getHeight()/6),paint);

        // health bar
        double percentage = ((getHealth()*1.0)/getFullHealth())*100.0;

        paint.setColor(Color.rgb( (int)((percentage > 50 ? 1 - 2 * (percentage - 50) / 100.0 : 1.0) * 255), (int)((percentage > 50 ? 1.0 : 2 * percentage / 100.0) * 255),0));

        percentage/=100.0;
        g.drawRect(new Rect(xP,yP,xP+(int)(percentage*getWidth()), yP+ getHeight()/6),paint);

        //string health
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        //g.drawText(String.valueOf((int)health), xP+getWidth()/3, yP-10, paint);

        Iterator i = bullets.iterator();

        while (i.hasNext()) {
            Bullet e = (Bullet)i.next();

            e.draw(g,offsetX,offsetY);

        }

    }

}
