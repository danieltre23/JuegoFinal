package com.example.juegofinal;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class PowerUp extends Sprite {

    private Bitmap b;

    PowerUp(int x, int y, int width, int height, Bitmap a, GameView game){
        super(x,y,width,height, width, height,game);
        b = a;
    }

    @Override
    public void update(){

    }

    @Override
    public  void draw(Canvas canvas, int offsetX, int offsetY){
        canvas.drawBitmap(b, x+ offsetX, y+offsetY,null);
    }

}
