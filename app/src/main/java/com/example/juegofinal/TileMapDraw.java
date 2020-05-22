package com.example.juegofinal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

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

    public void setB(TileMap map){
        /*Bitmap back = Bitmap.createScaledBitmap(background,tilesToPixels(map.getWidth()), tilesToPixels(map.getHeight()),false);


        for(int i=0; i<back.getHeight();i++){
            for(int j=0; j<back.getWidth(); j++){
                back.setPixel(i,j,Color.GREEN);
            }
        }
*/

        Bitmap c1 = createImage(tilesToPixels(1), tilesToPixels(1), Color.rgb(195,206,217));
        Bitmap c2 = createImage(tilesToPixels(1), tilesToPixels(1), Color.rgb(217,229,240));
        Bitmap c22 = c2;
        Bitmap c11 = c1;

        c1 = combineBitmaps(c1,c2);
        c1 = combineBitmaps(c1,c1);
        c1 = combineBitmaps(c1,c1);
        c1 = combineBitmaps(c1,c11);


        c2 = combineBitmaps(c2,c11);
        c2 = combineBitmaps(c2,c2);
        c2 = combineBitmaps(c2,c2);
        c2 = combineBitmaps(c2,c22);

        background = combineBitmapsV(c1,c2);
        background = combineBitmapsV(background, background);
        background = combineBitmapsV(background, background);
        background = combineBitmapsV(background, background);
        background = combineBitmapsV(background, background);
        background = combineBitmapsV(background, background);



    }

    public static Bitmap createImage(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }

    /**
     * <p>This method combines two images into one by rendering them side by side.</p>
     *
     * @param left The image that goes on the left side of the combined image.
     * @param right The image that goes on the right side of the combined image.
     * @return The combined image.
     */
    private Bitmap combineBitmaps(final Bitmap left, final Bitmap right){
        // Get the size of the images combined side by side.
        int width = left.getWidth() + right.getWidth();
        int height = left.getHeight() > right.getHeight() ? left.getHeight() : right.getHeight();

        // Create a Bitmap large enough to hold both input images and a canvas to draw to this
        // combined bitmap.
        Bitmap combined = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combined);

        // Render both input images into the combined bitmap and return it.
        canvas.drawBitmap(left, 0f, 0f, null);
        canvas.drawBitmap(right, left.getWidth(), 0f, null);

        return combined;
    }

    private Bitmap combineBitmapsV(final Bitmap up, final Bitmap down){
        // Get the size of the images combined side by side.
        int height = down.getHeight() + up.getHeight();
        int width = up.getWidth() > down.getWidth() ? up.getWidth() : down.getWidth();

        // Create a Bitmap large enough to hold both input images and a canvas to draw to this
        // combined bitmap.
        Bitmap combined = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combined);

        // Render both input images into the combined bitmap and return it.
        canvas.drawBitmap(up, 0f, 0f, null);
        canvas.drawBitmap(down, 0f, up.getHeight(), null);

        return combined;
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
        offsetX=-tile_size/2;

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

                        tile.update();  //caco
                        tile.draw(g, tilesToPixels(x) + offsetX, tilesToPixels(y) + offsetY);
                    }
                }
            }

        Tile tile = map.getGoal();

        if(map.getEnemyN()==0){
            tile.update();  //caco
            tile.draw(g, tile.getX() + offsetX, tile.getY()+ offsetY);
        }

        // draw player
        player.draw(g,offsetX,offsetY);

        // draw enemies
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
    }

}