package com.example.juegofinal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;

import static com.example.juegofinal.GameView.tile_size;

public class Player extends Sprite {


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
    private int bulletsTimer;
    private int bulletsRate;


    public double getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
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
        bullets.add(new Bullet(x, y, game, angle, 65));
    }

    private int dir;


    Player(GameView game, int x1, int y1, int w, int h, Animation []a){
        super(x1,y1,w,h, w,h,game);
        dx=0;
        dy=0;
        dir = 0;
        anims = a;
        curr = anims[dir];
        fullHealth = 550;
        health = fullHealth;
        paint = new Paint();
        hurting = false;
        bullets = new LinkedList();
        bulletsRate = 25;
        bulletsTimer = bulletsRate;
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

    public int getDistance(int tx, int ty) {
        return (int) Math.sqrt((ty - y) * (ty - y) + (tx - x) * (tx - x));
    }

    public int getAngle(int tx, int ty) {
        float angle = (float) Math.toDegrees(Math.atan2(y - ty, tx - x));

        if(angle < 0){
            angle += 360;
        }

        return (int) angle;
    }

    @Override
    public void update() {
        //update hurting flag
        if(hurting && System.currentTimeMillis()-timeHurting>timeToHurt){
            hurting  = false;
        }
        //update x and y and curr

        float newX, newY;

        newX = x + dx * (tile_size / 10)/5;
        newY = y + dy * (tile_size / 10)/5;


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

        while (i.hasNext()) {
            Enemy e = (Enemy) i.next();
            e.update();
            if (!hurting && !e.isDying() && Rect.intersects(e.getCollisionShape(), getCollisionShape())) {
                // posible sonido o animacion
                health-=20;
                health = Math.max(health,0);
                timeHurting = System.currentTimeMillis();
                hurting = true;
            }
            if(dx == 0 && dy == 0 && !e.isDying()){
                if(minDistance > getDistance(e.getX(), e.getY())){
                    minDistance = getDistance(e.getX(), e.getY());
                    newAngle = getAngle(e.getX(), e.getY());
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
        }

        curr = anims[dir];

        if (last != curr) {
            curr.start();
        }

        if(dx != 0 || dy != 0) {
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

    }


    @Override
    public void draw(Canvas g, int offsetX, int offsetY){
        int xP = Math.round(getX())+offsetX;
        int yP = Math.round(getY())+offsetY;

        g.drawBitmap(curr.getCurrentFrame(), xP, yP, game.getPaint());

        yP-=20;
        //health bar bg
        paint.setColor(Color.LTGRAY);
        g.drawRect(new Rect(xP,yP,xP+tile_size, yP+ tile_size/6),paint);

        // health bar
        double percentage = ((getHealth()*1.0)/getFullHealth())*100.0;

        paint.setColor(Color.rgb( (int)((percentage > 50 ? 1 - 2 * (percentage - 50) / 100.0 : 1.0) * 255), (int)((percentage > 50 ? 1.0 : 2 * percentage / 100.0) * 255),0));

        percentage/=100.0;
        g.drawRect(new Rect(xP,yP,xP+(int)(percentage*tile_size), yP+ tile_size/6),paint);

        //string health
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        g.drawText(String.valueOf((int)health), xP+tile_size/3, yP-10, paint);

        Iterator i = bullets.iterator();

        while (i.hasNext()) {
            Bullet e = (Bullet)i.next();

            e.draw(g,offsetX + width/2,offsetY + height/2);

        }

    }

}
