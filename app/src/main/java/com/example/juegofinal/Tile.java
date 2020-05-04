package com.example.juegofinal;

import android.graphics.Canvas;

public class Tile extends Sprite {

    private Animation normal;

    public Animation getAnim() {
        return normal;
    }


    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    private boolean destroyed;

    private GameView game;

    Tile(GameView game, int x1, int y1, Animation a){
        super(x1,y1,game.getScreenY()/9 ,game.getScreenY()/9);
        this.game = game;
        destroyed = false;
        normal = a;

        //create animation

    }

    Tile(GameView game, int x1, int y1, boolean des){
        super(x1,y1,game.getScreenY()/9 ,game.getScreenY()/9);
        this.game = game;
        destroyed = des;

        //create animation

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
