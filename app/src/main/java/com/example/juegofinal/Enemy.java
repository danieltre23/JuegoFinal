package com.example.juegofinal;

import android.graphics.Canvas;
import static com.example.juegofinal.GameView.tile_size;

public class Enemy extends Sprite {

    private Animation curr;
    private Animation []anims;

    private boolean attacking;


    Enemy(GameView game, int x1, int y1, Animation []a){
        super(x1,y1,tile_size,tile_size, game);
        attacking = false;
        anims = a;

        curr = anims[0];
    }


    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean a) {
        attacking = a;
    }

    public Animation getAnim(){
        return curr;
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
