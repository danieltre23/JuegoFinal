package com.example.juegofinal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import static com.example.juegofinal.GameView.tile_size;

public class Player extends Sprite {


    private Animation curr;
    private Animation last;
    private Animation []anims;
    private double health;
    private double fullHealth;
    private Paint paint;

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

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    private int dir;

    /*
       1
     2 0 4
       3
     */


    Player(GameView game, int x1, int y1, Animation []a){
        super(x1,y1,tile_size,tile_size, game);
        dx=0;
        dy=0;
        dir = 0;
        anims = a;
        curr = anims[dir];
        fullHealth = 400;
        health = fullHealth;
        paint = new Paint();
    }


    public Animation getAnim(){
        return curr;
    }

    private void updateDirection(){
        switch(dir){
            case 0:
                dx=0;dy=0;
                break;
            case 1:
                dx=0;dy=-1;
                break;
            case 2:
                dx=-1; dy=0;
                break;
            case 3:
                dx=0; dy=1;
                break;
            case 4:
                dx=1; dy=0;
                break;
        }
    }



    @Override
    public void update() {
        //update x and y and curr
        updateDirection();

        x+=dx*(tile_size/10);
        y+=dy*(tile_size/10);

        x = Math.max(0,x);
        x = Math.min(x, game.getRenderer().tilesToPixels(game.getMap().getWidth())-getWidth());

        y = Math.max(y,0);
        y = Math.min(y,game.getRenderer().tilesToPixels(game.getMap().getHeight())-getHeight());


        curr = anims[dir];


        if(last!=curr){
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
