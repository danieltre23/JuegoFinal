package com.example.juegofinal;

import android.graphics.Canvas;
import android.util.Log;

import static com.example.juegofinal.GameView.tile_size;

public class Player extends Sprite {


    private Animation curr;
    private Animation last;
    private Animation []anims;

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
    public void draw(Canvas canvas) {
        canvas.drawBitmap(curr.getCurrentFrame(), getX(), getY(), game.getPaint());
    }
}
