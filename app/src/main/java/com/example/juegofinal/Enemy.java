package com.example.juegofinal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.Iterator;
import java.util.LinkedList;

import static com.example.juegofinal.GameView.tile_size;

public abstract class Enemy extends Sprite {

    protected Animation curr;
    protected Animation dyingA;
    protected Paint paint;
    protected double health;
    protected double fullHealth;
    protected boolean dying;
    protected long time;
    private LinkedList<Bullet> bullets;
    public int bulletsTimer;
    public int bulletsRate;
    private Bitmap bul;
    protected boolean readyToRemove = false;
    private SoundPool soundPool;
    private int sound1,sound2;

    public boolean isReadyToRemove(){
        return readyToRemove;
    }

    public boolean isDying() {
        return dying;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getFullHealth() {
        return fullHealth;
    }

    public void setFullHealth(double fullHealth) {
        this.fullHealth = fullHealth;
    }

    private boolean attacking;

    public void damage(double damage) {
        this.health -= damage;
    }

    public void addBullet(int angle){
        bullets.add(new Bullet(x+getWidth()/2, y+getHeight()/2, game, angle, 50,bul,20,20));
    }

    public Iterator getBullets() {
        return bullets.iterator();
    }

    public void removeBullet(Bullet b) {
        bullets.remove(b);
    }

    /**
     * enemy constructor
     */
    Enemy(GameView game, int x1, int y1, int w, int h, Animation a, Animation b, int healT){
        super(x1,y1,w,h,w,h,game);
        attacking = false;

        curr = a;
        dyingA = b;

        paint = new Paint();
        fullHealth = healT;
        health = fullHealth;
        dying=false;

        bullets = new LinkedList();
        bulletsRate = 25;
        bulletsTimer = bulletsRate;

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sound1 = soundPool.load(game.getActivity(), R.raw.kill, 1);  //killed

        bul = ResourceManager.createImage(50,50,Color.GREEN);
    }


    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean a) {
        attacking = a;
    }

    /**
     * start the countdown of the dying animation
     */
    public void kill(){
        GameView.play(soundPool,sound1,0.4);
        dying=true;
        curr = dyingA;
        curr.start();
        time = System.currentTimeMillis();
    }

    public Animation getAnim(){
        return curr;
    }


      /**
     enemy draw. offsetX and offsetY needed. enemy x and y are from map, not screen
       enemies have a health bar
     */

    @Override
    public void draw(Canvas g, int offsetX, int offsetY){
        int xP = Math.round(getX())+offsetX;
        int yP = Math.round(getY())+offsetY;


        g.drawBitmap(curr.getCurrentFrame(), xP, yP, game.getPaint());

        yP-=20;
        //health bar bg
        if(!dying) {
            paint.setColor(Color.LTGRAY);
            g.drawRect(new Rect(xP, yP, xP + width, yP + height / 6), paint);

            // health bar
            double percentage = ((getHealth() * 1.0) / getFullHealth()) * 100.0;
            paint.setColor(Color.rgb((int) ((percentage > 50 ? 1 - 2 * (percentage - 50) / 100.0 : 1.0) * 255), (int) ((percentage > 50 ? 1.0 : 2 * percentage / 100.0) * 255), 0));

            percentage /= 100.0;
            g.drawRect(new Rect(xP, yP, xP + (int) (percentage * width), yP + height / 6), paint);

            //string health
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
           // g.drawText(String.valueOf((int) health), xP + width / 3, yP - 10, paint);
        }
        Iterator i = bullets.iterator();

        while (i.hasNext()) {
            Bullet e = (Bullet)i.next();

            e.draw(g,offsetX,offsetY);

        }
    }
}
