package com.example.juegofinal;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.juegofinal.GameView.tile_size;

public class Enemy2 extends Enemy {


    private int dx,dy,speed;

    private void setGoal(Sprite s){
        int angle = getAngle(s);
        if(bulletsTimer < bulletsRate){
            bulletsTimer += 1;
        }
        if(bulletsTimer >= bulletsRate) {
            addBullet(angle);
            bulletsTimer = 0;
        }
    }

    Enemy2(GameView game, int x1, int y1, int w, int h, Animation a, Animation b){
        super(game,x1,y1,w,h,a,b, 300);
        dx = 0;
        dy = 0;
        speed = tile_size/17;
    }

    @Override
    public void update() {
        //update x and y and curr

        Iterator it = getBullets();
        Player player = game.getMap().getPlayer();

        List<Bullet> removeBullets = new ArrayList<Bullet>();

        while (it.hasNext()) {
            Bullet b = (Bullet) it.next();
            boolean col = b.update2();

            if (!col) {
                if (Rect.intersects(player.getCollisionShape(), b.getCollisionShape())) {
                    removeBullets.add(b);
                    player.hit();
                }
            }
            else{
                removeBullets.add(b);
            }
        }

        for(Bullet bi : removeBullets){
            removeBullet(bi);
        }

        if(isAttacking()) {
            setGoal(player);
        }
        //animation tick
        curr.update();

        /*float newX = x, newY = y;


        if(isAttacking()) {
            newX = x + dx;
            newY = y + dy;
        }

        //collision with tiles and end of map

        //x movement check
        Point tile = getTileCollision(newX, y);

        if (tile == null) {
            x = (int) newX;
        } else {
            // line up with the tile boundary
            if (dx > 0) {
                x = game.getRenderer().tilesToPixels(tile.x) - realW - ((getWidth() - realW) / 2);
            } else if (dx < 0) {
                x = game.getRenderer().tilesToPixels(tile.x + 1) - ((getWidth() - realW) / 2);
            }

            dx = 0;
        }

        //y movement check
        tile = getTileCollision(x, newY);

        if (tile == null) {
            y = (int) newY;
        } else {
            // line up with the tile boundary
            if (dy > 0) {
                y = game.getRenderer().tilesToPixels(tile.y) - realH - ((getHeight() - realH) / 2);
            } else if (dy < 0) {
                y = game.getRenderer().tilesToPixels(tile.y + 1) - ((getHeight() - realH) / 2);
            }

            dy = 0;
        }

*/
        if(dying && System.currentTimeMillis()>= time + curr.getLength()*curr.getSpeed() -20){  //dying animation is over -> remove
            readyToRemove = true;
        }

    }


}
