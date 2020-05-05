package com.example.juegofinal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.graphics.Paint;

public class Button {

    private int x;
    private int y;
    private int width;
    private int height;

    private Paint paint;

    Bitmap button;

    Button (Resources res, int id, int x1, int y1) {

        this( res,  id, x1,  y1, 100, 100);

    }

    Button (Resources res, int id,int x1, int y1, int w, int h) {
        x = x1;
        y = y1;
        width = w;
        height = h;

        button = BitmapFactory.decodeResource(res, id);
        button = Bitmap.createScaledBitmap(button, w, h, false);

        paint  = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

    }

    public boolean click(MotionEvent event){
        return event.getX() > getX() && event.getX() < getX()  + getWidth() && event.getY() > getY() && event.getY() < getY() + getHeight();
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(button,getX(),getY(), paint);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


}
