package com.example.juegofinal;

import android.graphics.Canvas;
import static com.example.juegofinal.GameView.tile_size;

public class Tile extends Sprite {

    private Animation normal;
    private boolean destroyed;


    Tile(GameView game, int x1, int y1, Animation a){
        super(x1,y1,tile_size ,tile_size, game);
        destroyed = false;
        normal = a;

        //create animation

    }

    public Animation getAnim() {
        return normal;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }


    @Override
    public void update() {
        //update animation
        normal.update();
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(normal.getCurrentFrame(), getX(), getY(), game.getPaint());
    }
}
