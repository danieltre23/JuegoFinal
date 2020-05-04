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

    private static final int TILE_SIZE = 64;
    // the size in bits of the tile
    // Math.pow(2, TILE_SIZE_BITS) == TILE_SIZE
    private static final int TILE_SIZE_BITS = 6;

    private Bitmap background;
    private Paint paint = new Paint();

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

        int mapWidth = tilesToPixels(map.getWidth());
        int mapHeight = tilesToPixels(map.getWidth());

        // get the scrolling position of the map


         /*int offsetX = screenWidth / 2 - Math.round(player.getX()) - TILE_SIZE;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidth);
          int offsetY = screenHeight - tilesToPixels(map.getHeight());
        */



         //x offset for enemies and tiles
        int offsetX = screenWidth -  tilesToPixels(map.getWidth());

        // get the y offset based on the position

        int offsetY = screenHeight/2 - Math.round(player.getY()) - TILE_SIZE;
        offsetY = Math.min(offsetY, 0);
        offsetY = Math.max(offsetY, screenHeight - mapHeight);


        /*
         // draw black background, if needed

        if (background == null ||
                screenHeight > background.getHeight(null))
        {
            g.
            g.setColor(Color.black);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }

        */


        // draw parallax background image
        if (background != null) {
            int x = offsetX *
                    (screenWidth - background.getWidth()) /
                    (screenWidth - mapWidth);
            x = screenWidth - background.getWidth();
            x/=2;

            int y;
            y = screenHeight-background.getHeight() - offsetY * (screenHeight - background.getHeight())/(screenHeight - mapHeight);
            g.drawBitmap(background, x, y, paint);
        }

        // draw the visible tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX +
                pixelsToTiles(screenWidth) + 1;

        for (int y=0; y<map.getHeight(); y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
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

            if (!e.isAttacking()  && x >= 0 && x < screenWidth)
            {
                e.setAttacking(true);
            }
        }
    }

}