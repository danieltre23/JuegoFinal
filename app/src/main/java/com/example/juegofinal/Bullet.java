package com.example.juegofinal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import static com.example.juegofinal.GameView.tile_size;

public class Bullet extends Sprite {

    private int dx;
    private int dy;
    private Bitmap b;

    /**
     * Set the initial values to create the item
     *
     * @param x      <b>x</b> position of the object
     * @param y      <b>y</b> position of the object
     * @param game
     */
    public Bullet(int x, int y, GameView game, int angle, int speed, Bitmap bul) {
        super(x,y,20,20, 20,20,game);
        dx = (int) (Math.cos(angle*3.14/180) * speed);
        dy = (int) (Math.sin(angle*3.14/180) * speed * -1);
        b = bul;
    }

    @Override
    public void update() {
        x += dx;
        y += dy;
    }

    public boolean update2() {
        float newX, newY;

        newX = x + dx ;
        newY = y + dy ;


        //collision with tiles and end of map

        //x movement check
        Point tile = getTileCollision(newX, y);

        if (tile == null) {
            x = (int) newX;
        } else {
          return true;
        }

        //y movement check
        tile = getTileCollision(x, newY);

        if (tile == null) {
            y = (int) newY;
        } else {
            // line up with the tile boundary
           return true;
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas, int offsetX, int offsetY) {
        int xP = Math.round(getX())+offsetX;
        int yP = Math.round(getY())+offsetY;

        canvas.drawBitmap(b, xP, yP, game.getPaint());
    }
}
