package com.example.juegofinal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import static com.example.juegofinal.GameView.tile_size;

public class Tile extends Sprite {

    private Bitmap normal;
    private Animation curr;

    /**
     * if recieved an animation as an argument this tile would be the goal
     */

    Tile(GameView game, int x1, int y1, Bitmap a){
        super(x1,y1,tile_size ,tile_size, tile_size,tile_size, game);
        normal = a;
        curr = null;
    }

    Tile(GameView game, int x1, int y1, int w, int h, Animation a){
        super(x1,y1,w ,h, w,h, game);
        curr = a;
        normal = null;
    }

    /**
     * update the animation or the bitmap
     */

    @Override
    public void update() {
        // :)
        curr.update();
    }

    /**
     * draw the bitamp or the animation if this is the goal tile
     */
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
