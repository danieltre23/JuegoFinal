package com.example.juegofinal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import static com.example.juegofinal.GameView.tile_size;

public class Tile extends Sprite {

    private Bitmap normal;
    private boolean destroyed;
    private Animation curr;


    Tile(GameView game, int x1, int y1, Bitmap a){
        super(x1,y1,tile_size ,tile_size, tile_size,tile_size, game);
        destroyed = false;
        normal = a;
        curr = null;
    }

    Tile(GameView game, int x1, int y1, int w, int h, Animation a){
        super(x1,y1,w ,h, w,h, game);
        destroyed = false;
        curr = a;
        normal = null;
    }


    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }


    @Override
    public void update() {
        // :)
        curr.update();
    }

    @Override
    public void draw(Canvas canvas, int xT, int yT) {
        if (normal != null) {
            canvas.drawBitmap(normal, xT, yT, game.getPaint());
        }
        else{
            canvas.drawBitmap(curr.getCurrentFrame(), xT, yT, game.getPaint());
        }
    }
}
