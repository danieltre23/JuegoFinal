package com.example.juegofinal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import static com.example.juegofinal.GameView.tile_size;

public class Enemy extends Sprite {

    private Animation curr;
    private Animation []anims;
    private Paint paint;
    private double health;
    private double fullHealth;

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


    Enemy(GameView game, int x1, int y1, Animation []a){
        super(x1,y1,tile_size,tile_size, (int)(tile_size*0.714),tile_size,game);
        attacking = false;
        anims = a;

        curr = anims[0];
        paint = new Paint();
        fullHealth = 500;
        health = fullHealth*0.72;
    }


    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean a) {
        attacking = a;
    }

    public Animation getAnim(){
        return curr;
    }


    @Override
    public void update() {
        //update x and y and curr

        /*Animation newAnimation;

        if(newAnimation!=curr){
            curr = newAnimation;
            curr.start();
        }
        */
        curr.update();

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
