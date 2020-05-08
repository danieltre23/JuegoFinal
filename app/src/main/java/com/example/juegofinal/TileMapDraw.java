package com.example.juegofinal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.Iterator;
import static com.example.juegofinal.GameView.tile_bit;
import static com.example.juegofinal.GameView.tile_size;

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



    private Bitmap background;
    private Bitmap black;
    private Paint paint;

    /**
     Converts a pixel position to a tile position.
     */
    public  int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }


    /**
     Converts a pixel position to a tile position.
     */
    public  int pixelsToTiles(int pixels) {
        // use shifting to get correct values for negative pixels
       // return pixels >> tile_bit;

        // or, for tile sizes that aren't a power of two,
        // use the floor function:
        return (int)Math.floor((float)pixels / tile_size);
    }


    /**
     Converts a tile position to a pixel position.
     */
    public  int tilesToPixels(int numTiles) {
        // no real reason to use shifting here.
        // it's slightly faster, but doesn't add up to much
        // on modern processors.
        //return numTiles << tile_bit;

        // use this if the tile size isn't a power of 2:
         return numTiles * tile_size;
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

        int offsetY = screenHeight/2 - Math.round(player.getY());

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


            int y  = offsetY * (screenHeight - background.getHeight())/(screenHeight - mapHeightP); //cm

            g.drawBitmap(background, x, y, paint);
        }

        // draw the visible tiles




        int firstTileY = pixelsToTiles(-offsetY);
        int lastTileY = firstTileY + pixelsToTiles(screenHeight)+1;


        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX =  firstTileX + pixelsToTiles(screenWidth)+1;





        for (int y=firstTileY; y<=lastTileY; y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                Tile tile = map.getTile(x, y);
                if (tile != null) {
                    tile.update();  //caco
                    g.drawBitmap(tile.getAnim().getCurrentFrame(),
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