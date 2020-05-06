package com.example.juegofinal;

import android.graphics.Canvas;

public class Player extends Sprite {


    private Animation curr;
    private Animation []anims;
    private int dx,dy;

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }





    Player(GameView game, int x1, int y1, Animation []a){
        super(x1,y1,128,128, game);
        anims = a;
        curr = anims[0];
        dx=0;
        dy=0;

    }

    Player(GameView game, Animation []a){
        super(game.getScreenX()/2 - 64, game.getScreenY() - 200,128,128, game);
        anims = a;
        curr = anims[0];
        dx=0;
        dy=0;
    }

    public Animation getAnim(){
        return curr;
    }


    @Override
    public void update() {
        //update x and y and curr
        x+=dx*5;
        y+=dy*5;

        x = Math.max(0,x);
        x = Math.min(x, game.getRenderer().tilesToPixels(game.getMap().getWidth())-getWidth());

        y = Math.max(y,0);
        y = Math.min(y,game.getRenderer().tilesToPixels(game.getMap().getHeight())-getHeight());


        /*Animation newAnimation;

        if(newAnimation!=curr){
            curr = newAnimation;
            curr.start();
        }
        */

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(curr.getCurrentFrame(), getX(), getY(), game.getPaint());
    }
}
