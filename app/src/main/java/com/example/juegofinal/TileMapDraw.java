package com.example.juegofinal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseIntArray;

import java.util.Iterator;
import java.util.LinkedList;

/**
 The TileMapRenderer class draws a TileMap on the screen.
 It draws all tiles, sprites, and an optional background image
 centered around the position of the player.

 <p>If the width of background image is smaller the width of
 the tile map, the background image will appear to move
 slowly, creating a parallax background effect.

 <p>Also, three static methods are provided to convert pixels
 to tile positions, and vice-versa.

 <p>This TileMapRender uses a tile size of 64.
 */
public class TileMapDraw {

    private static final int TILE_SIZE = 128;
    // the size in bits of the tile
    // Math.pow(2, TILE_SIZE_BITS) == TILE_SIZE
    private static final int TILE_SIZE_BITS = 7;

    private Bitmap background;
    private Bitmap black;
    private Paint paint;

    /**
     Converts a pixel position to a tile position.
     */
    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }


    /**
     Converts a pixel position to a tile position.
     */
    public static int pixelsToTiles(int pixels) {
        // use shifting to get correct values for negative pixels
        return pixels >> TILE_SIZE_BITS;

        // or, for tile sizes that aren't a power of two,
        // use the floor function:
        //return (int)Math.floor((float)pixels / TILE_SIZE);
    }


    /**
     Converts a tile position to a pixel position.
     */
    public static int tilesToPixels(int numTiles) {
        // no real reason to use shifting here.
        // it's slightly faster, but doesn't add up to much
        // on modern processors.
        return numTiles << TILE_SIZE_BITS;

        // use this if the tile size isn't a power of 2:
        //return numTiles * TILE_SIZE;
    }

    TileMapDraw(Bitmap bg, Bitmap b){
        background = bg;
        paint = new Paint();
        black = b;
    }


    /**
     Sets the background to draw.
     */
    public void setBackground(Bitmap background) {
        this.background = background;

    }


    /**
     Draws the specified TileMap.
     */
    public void draw(Canvas g, TileMap map,
                     int screenWidth, int screenHeight)
    {
        Player player = map.getPlayer();

        int mapWidthP = tilesToPixels(map.getWidth());
        int mapHeightP = tilesToPixels(map.getHeight());

        // get the scrolling position of the map


         /*int offsetX = screenWidth / 2 - Math.round(player.getX()) - TILE_SIZE;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidth);
          int offsetY = screenHeight - tilesToPixels(map.getHeight());
        */



         //x offset for enemies and tiles
        int offsetX = screenWidth/2 - Math.round(player.getX()) - 128;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidthP);

        // get the y offset based on the player position

        int offsetY = screenHeight/2 - (mapHeightP-Math.round(player.getY()));

        offsetY = Math.min(offsetY, 0);
        offsetY = Math.max(offsetY, screenHeight - mapHeightP);



         // draw black background, if needed

        if (screenWidth > background.getWidth())
        {
          g.drawBitmap(black,0,0,null);
        }




        // draw parallax background image
        if (background != null) {

           int  x = offsetX *(screenWidth - background.getWidth())/(screenWidth - mapWidthP);


            int y  = screenHeight-background.getHeight() - offsetY * (screenHeight - background.getHeight())/(screenHeight - mapHeightP); //fallando

            g.drawBitmap(background, x, y, paint);
        }

        // draw the visible tiles




        int lastTileY = (map.getHeight()-1) - pixelsToTiles(-offsetY);
        int firstTileY = lastTileY - pixelsToTiles(screenHeight);

        firstTileY = Math.max(firstTileY,0);
        lastTileY = Math.min((map.getHeight()-1),lastTileY);

        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX =  firstTileX + pixelsToTiles(screenWidth);

        firstTileX = Math.max(firstTileX,0);
        lastTileX = Math.min((map.getWidth()-1),lastTileX);



        for (int y=firstTileY; y<=lastTileY; y++) {
            for (int x=firstTileX; x < lastTileX+2; x++) {
                Tile image = map.getTile(x, y);
                if (image != null) {
                    g.drawBitmap(image.getAnim().getCurrentFrame(),
                            tilesToPixels(x) + offsetX,
                            tilesToPixels(y) + offsetY,
                            paint);
                }
            }
        }

        // draw player
        g.drawBitmap(player.getAnim().getCurrentFrame(),
                Math.round(player.getX()) + offsetX,
                Math.round(player.getY()) + offsetY,
                paint);

        // draw enemies
        Iterator i = map.getEnemies();

        while (i.hasNext()) {
            Enemy e = (Enemy)i.next();

            int x = Math.round(e.getX()) + offsetX;
            int y = Math.round(e.getY()) + offsetY;

            g.drawBitmap(e.getAnim().getCurrentFrame(), x, y, paint);

            // wake up the creature when it's on screen (starts attacking)

            if (!e.isAttacking()  && y<screenHeight && y>0)
            {
                e.setAttacking(true);
            }
        }
    }

}