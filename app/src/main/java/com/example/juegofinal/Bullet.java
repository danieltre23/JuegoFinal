package com.example.juegofinal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

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
    public Bullet(int x, int y, GameView game, int angle) {
        super(x,y,1,1, 1,1,game);
        dx = (int) (Math.cos(angle*3.14/180) * 35);
        dy = (int) (Math.sin(angle*3.14/180) * 35 * -1);
        b = BitmapFactory.decodeResource(game.getResources(), R.drawable.black);
        b = Bitmap.createScaledBitmap(b, 20, 20,false);
    }

    @Override
    public void update() {
        x += dx;
        y += dy;
    }

    @Override
    public void draw(Canvas canvas, int offsetX, int offsetY) {
        int xP = Math.round(getX())+offsetX;
        int yP = Math.round(getY())+offsetY;

        canvas.drawBitmap(b, xP, yP, game.getPaint());
    }
}
