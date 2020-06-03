package com.example.juegofinal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.Iterator;
import static com.example.juegofinal.GameView.tile_size;

/**
 The TileMapRenderer class draws a TileMap on the screen.
 It draws all tiles, sprites, and an optional background image
 centered around the position of the player. Vertically.

 <p>Horizontally we used fixed number of tiles that always fit the
 user's screen

 <p>Also, three static methods are provided to convert pixels
 to tile positions, and vice-versa.

 <p>This TileMapRender uses a tile size depending on the user screen
 */
public class TileMapDraw {

    private Bitmap background;
    private Bitmap black;
    private Paint paint;
    private GameView game;

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
        // use this if the tile size isn't a power of 2:
         return numTiles * tile_size;
    }



    TileMapDraw(Bitmap bg){
        background = bg;
        paint = new Paint();
    }



    /**
     Sets the background to draw.
     */
    public void setBackground(Bitmap background) {
        this.background = background;

    }

    /**
     Draws the specified TileMap. All assets
     */
    public void draw(Canvas g, TileMap map,
                     int screenWidth, int screenHeight)
    {
        Player player = map.getPlayer();

        int mapWidthP = tilesToPixels(map.getWidth());
        int mapHeightP = tilesToPixels(map.getHeight());

         //x offset for enemies and tiles
        int offsetX = -tile_size/2;

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
            int y  = offsetY ; //cm

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
                     tile.draw(g, tilesToPixels(x) + offsetX, tilesToPixels(y) + offsetY);
                    }
                }
            }

        Tile tile = map.getGoal();

        // if no enemies draw the goal tile
        if(map.getEnemyN()==0){
            tile.update();  //caco
            tile.draw(g, tile.getX() + offsetX, tile.getY()+ offsetY);
        }

        // draw player
        player.draw(g,offsetX,offsetY);

        // draw all enemies
        Iterator i = map.getEnemies();

        while (i.hasNext()) {
            Enemy e = (Enemy)i.next();

            int x = Math.round(e.getX()) + offsetX;
            int y = Math.round(e.getY()) + offsetY;

            e.draw(g,offsetX,offsetY);

            // wake up the creature when it's on screen (starts attacking)

            if (!e.isAttacking()  && y<screenHeight && y>0 && x>0 && x<screenWidth)
            {
                e.setAttacking(true);
            }
        }

        //draw powerups

        Iterator powerUp = map.getPowerUps();

        while(powerUp.hasNext()){
            PowerUp P = (PowerUp)powerUp.next();
            P.draw(g,offsetX,offsetY);
        }



    }

}