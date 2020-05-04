package com.example.juegofinal;

import android.graphics.Canvas;

public class Player extends Sprite {

    private Animation normal;
    private Animation left;
    private Animation right;
    private Animation up;
    private Animation down;

    private Animation curr;
    private GameView game;

    public Animation getAnim(){
        return curr;
    }

    Player(GameView game, int x1, int y1){
        super(x1,y1,game.getScreenY()/9 ,game.getScreenY()/9);
        this.game = game;

        //create animations

    }

    Player(GameView game){
        super(game.getScreenX()/2 - (game.getScreenY()/9)/2, game.getScreenY() - (game.getScreenY()/9)*3,game.getScreenY()/9 ,game.getScreenY()/9);
        this.game = game;

        //create animations
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
