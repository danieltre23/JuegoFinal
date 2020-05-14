package com.example.juegofinal;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import static com.example.juegofinal.GameView.tile_size;


public abstract class Sprite {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected GameView game;
    protected Point point;
    protected int realW;
    protected int realH;

    public int getRealW() {
        return realW;
    }

    public int getRealH() {
        return realH;
    }

    /**
     * Set the initial values to create the item
     * @param x <b>x</b> position of the object
     * @param y <b>y</b> position of the object
     */
    public Sprite(int x, int y, int width, int height, int realW, int realH, GameView game){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.game = game;
        this.realH = realH;
        this.realW = realW;
        point = new Point();
    }

    /**
     * Get x value
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Get y value
     * @return y
     */
    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Set x value
     * @param x to modify
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set y value
     * @param y to modify
     */
    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * To update positions of the item for every tick
     */
    public abstract void update();

    /**
     * To paint the item
     */
    public abstract void draw(Canvas canvas, int offsetX, int offsetY);

    Rect getCollisionShape () {
        return new Rect(x + (width-realW)/2, y +(height-realH)/2, x + (width-realW)/2 + realW, y +(height-realH)/2 + realH);
    }

    public Point getTileCollision(float newX, float newY)
    {
        int sizeOffSetX =  (getWidth()-realW)/2;
        int sizeOffSetY = (getHeight()-realH)/2;

        float fromX = Math.min(getX()+sizeOffSetX, newX+sizeOffSetX);
        float fromY = Math.min(getY()+sizeOffSetY, newY+sizeOffSetY);
        float toX = Math.max(getX()+sizeOffSetX, newX+sizeOffSetX);
        float toY = Math.max(getY()+sizeOffSetY, newY+sizeOffSetY);

        // get the tile locations

        int fromTileX = game.getRenderer().pixelsToTiles(fromX);
        int fromTileY = game.getRenderer().pixelsToTiles(fromY);
        int toTileX = game.getRenderer().pixelsToTiles(
                toX + realW - 1);
        int toTileY = game.getRenderer().pixelsToTiles(
                toY + realH - 1);

        // check each tile for a collision
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= game.getMap().getWidth() || y<0 || y>=game.getMap().getHeight() ||
                        game.getMap().getTile(x, y) != null)
                {
                    // collision found, return the tile
                    point.set(x,y);
                    return point;
                }
            }
        }

        // no collision found
        return null;
    }


}