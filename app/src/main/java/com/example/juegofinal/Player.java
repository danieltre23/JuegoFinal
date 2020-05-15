package com.example.juegofinal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.Iterator;

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

    private int dir;


    Player(GameView game, int x1, int y1, int w, int h, Animation []a){
        super(x1,y1,w,h, w,h,game);
        dx=0;
        dy=0;
        dir = 0;
        anims = a;
        curr = anims[dir];
        fullHealth = 500;
        health = fullHealth;
        paint = new Paint();
        hurting = false;
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

        if(dx==0 && dy==0){
            dir=0;
        }
        else if((angle>0 && angle<range) || (angle>315+range && angle<=359)){
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

        while (i.hasNext()) {
            Enemy e = (Enemy) i.next();
            if (!hurting && Rect.intersects(e.getCollisionShape(), getCollisionShape())) {
                // posible sonido o animacion
                health-=20;
                health = Math.max(health,0);
                timeHurting = System.currentTimeMillis();
                hurting = true;
            }
        }

            curr = anims[dir];

            if (last != curr) {
                curr.start();
            }

            curr.update();
            last = curr;


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

    }

}
