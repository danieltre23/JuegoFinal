package com.example.juegofinal;

import android.graphics.Canvas;

public class Enemy extends Sprite {


    private Animation normal;
    private Animation left;
    private Animation right;
    private Animation up;
    private Animation down;

    private Animation curr;

    private GameView game;
    private boolean attacking;

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean a) {
        attacking = a;
    }



    public Animation getAnim(){
        return curr;
    }

    Enemy(GameView game, int x1, int y1, Animation[] anims){
        super(x1,y1,64,64);
        this.game = game;
        attacking = false;

        //create animations
        normal = anims[0];
        left = anims[1];
        right = anims[2];
        up = anims[3];
        down = anims[4];

    }


    @Override
    public void update() {
        //update x and y and curr

        /*Animation newAnimation;

        if(newAnimation!=curr){
            curr = newAnimation;
            curr.start();
        }
        */
        curr.update();

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(curr.getCurrentFrame(), getX(), getY(), game.getPaint());
    }
}
