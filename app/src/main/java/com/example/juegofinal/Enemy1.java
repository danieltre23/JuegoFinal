package com.example.juegofinal;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;

import java.util.List;

import static com.example.juegofinal.GameView.tile_size;

public class Enemy1 extends Enemy {


    private int dx,dy,speed;

    private void setGoal(Sprite s){
        int angle = getAngle(s);
        dx = (int) (Math.cos(angle*3.14/180) * speed);
        dy = (int) (Math.sin(angle*3.14/180) * speed * -1);
    }

    Enemy1(GameView game, int x1, int y1, int w, int h, Animation a, Animation b){
        super(game,x1,y1,w,h,a,b, 300);
        dx = 0;
        dy = 0;
        speed = tile_size/19;
    }

    private void updateDXDY(Point2D p1, Point2D p2){
        if(p2.x>p1.x){
            dx=1;
        }else if(p1.x>p2.x){
            dx=-1;
        }
        else{
            dx=0;
        }

        if(p2.y>p1.y){
            dy=1;
        }else if(p1.y>p2.y){
            dy=-1;
        }
        else{
            dy=0;
        }

        dx*=speed;
        dy*=speed;
    }


    @Override
    public void update() {
        //update x and y and curr

        Point2D start  = new Point2D(closestTile(x).first, closestTile(y).first);
        Point2D target = new Point2D(closestTile(game.getMap().getPlayer().getX()).first, closestTile(game.getMap().getPlayer().getY()).first);

        Log.i("path", " "+closestTile(x).first+ " " + + closestTile(y).first+ " "+ closestTile(x).second + " " + closestTile(y).second);

        // Last argument will make this search be 4 directional if false

        if(isAttacking()) {
            List<Point2D> path = PathFinding.findPath(game.getMap().getGrid(), start, target, true);

            if (path.iterator().hasNext()) {
                if ((closestTile(x).second >= 99 && closestTile(y).second >= 99) || (dx==0 && dy==0)) {
                    updateDXDY(start, path.iterator().next());
                }
            } else {
                updateDXDY(start, start);
            }
        }
        //animation tick
        curr.update();

        float newX = x, newY = y;


        if(isAttacking() && !Rect.intersects(game.getMap().getPlayer().getCollisionShape(), getCollisionShape())) {
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


        if(dying && System.currentTimeMillis()>= time + curr.getLength()*curr.getSpeed() -20){  //dying animation is over -> remove
           readyToRemove = true;
        }

    }


}
