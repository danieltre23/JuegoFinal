package com.example.juegofinal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import static com.example.juegofinal.GameView.tile_size;
import static com.example.juegofinal.GameView.tile_bit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ResourceManager {

    private GameView game;
    private Animation animA;
    private Animation animB;
    private Animation animC;
    private Animation animG;

    private Animation []animE1;
    private Animation []animP1;
    Animation u,d,r,l,n;



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

        return (int)Math.floor((float)pixels / tile_size);
    }


    /**
     Converts a tile position to a pixel position.
     */
    public  int tilesToPixels(int numTiles) {
        // no real reason to use shifting here.
        // it's slightly faster, but doesn't add up to much
        // on modern processors.


        // use this if the tile size isn't a power of 2:
        return numTiles * tile_size;
    }

    void initAnimations(){
        Bitmap x,y,w,z;

        //imagenes a b y c

        x = BitmapFactory.decodeResource(game.getResources(),R.drawable.a);
        x = Bitmap.createScaledBitmap(x, tile_size, tile_size,false);
        Bitmap[] framesA = {x};

        animA = new Animation(framesA,100);

        x = BitmapFactory.decodeResource(game.getResources(),R.drawable.b);
        x = Bitmap.createScaledBitmap(x, tile_size, tile_size,false);
        Bitmap[] framesB = {x};

        animB = new Animation(framesB,100);

        x = BitmapFactory.decodeResource(game.getResources(),R.drawable.c);
        x = Bitmap.createScaledBitmap(x, tile_size, tile_size,false);
        Bitmap[] framesC = {x};

        animC = new Animation(framesC,100);

        String file;

        Bitmap []framesG = {null,null,null,null};




        for(int i= 1; i<=4; i++){
            file = "star" + i;
            x = BitmapFactory.decodeResource(game.getResources(), game.getResources().getIdentifier(file, "drawable", game.pac));
            x = Bitmap.createScaledBitmap(x, tile_size, tile_size,false);
            framesG[i-1] =x;
        }




        animG = new Animation(framesG,210);


        x = BitmapFactory.decodeResource(game.getResources(),R.drawable.enemy1);
        x = Bitmap.createScaledBitmap(x, tile_size, tile_size,false);

        Bitmap[] framesE1 = {x};

        animE1 = new Animation[] {new Animation (framesE1, 100),new Animation (framesE1, 100),new Animation (framesE1, 100),new Animation (framesE1, 100)};


        animP1 = new Animation[9] ;
        Bitmap[] framesP1 = new Bitmap[5];


        Bitmap image, crop;


            for(int j = 1; j<=5; j++){
                image = BitmapFactory.decodeResource(game.getResources(), game.getResources().getIdentifier("p0" + j, "drawable", game.pac));
                image = Bitmap.createScaledBitmap(image, tile_size, (int) (tile_size),false);
                framesP1[j-1] = image;
            }

            animP1[0] = new Animation(framesP1, 150);
            framesP1 = new Bitmap[4];


        for(int i=1; i<=8; i++){
            file = "pa"+i;
            image = BitmapFactory.decodeResource(game.getResources(), game.getResources().getIdentifier(file, "drawable", game.pac));
            for(int j = 0; j<=3; j++){
                crop = Bitmap.createBitmap(image, j*image.getWidth()/4, 0, image.getWidth()/4, image.getHeight());
                crop = Bitmap.createScaledBitmap(crop, tile_size, (int) (tile_size),false);
                framesP1[j] = crop;
            }
            animP1[i] = new Animation(framesP1, 150);
            framesP1 = new Bitmap[4];
        }

       /* for(int i = 1; i<=4; i++) {
            for (int j = 1; j <= 9; j++) {
                file = "tile" + String.valueOf(i) + String.valueOf(j);
                x = BitmapFactory.decodeResource(game.getResources(), game.getResources().getIdentifier(file, "drawable", game.pac));
                x = Bitmap.createScaledBitmap(x, tile_size, tile_size, false);
                framesP1[j - 1] = x;
            }
            animP1[i] = new Animation(framesP1,150);
            framesP1 = new Bitmap[9];
        }
*/
    }
    /*
        1 2 3
        4 0 5
        6 7 8
        */

    public ResourceManager(GameView g){

        game = g;
        //create animations
        initAnimations();

    }

    public TileMap loadMap(int id, Resources res) throws IOException {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // read every line in the text file into the list
        InputStream is = res.openRawResource(id);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);


        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();

        TileMap newMap = new TileMap(width, height);
        int contB = 0;

        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.

                // check if the char represents a sprite
                 if (ch == 'A') {
                    newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),animA));
                     contB++;
                 }
                else if (ch == 'B') {
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),animB));
                }
                else if (ch == 'C') {
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),animC));
                }
                else if (ch == 'G') {
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),animG));
                }
                else if (ch == '1') {
                    newMap.addEnemy(new Enemy(game,tilesToPixels(x), tilesToPixels(y), animE1));
                }
            }
        }

        // add the player to the map (position with map)
        newMap.setPlayer(new Player(game, tilesToPixels(width)/2 - tile_size, tilesToPixels(height)-tile_size, tile_size, (int) (tile_size),  animP1));

        return newMap;
    }

}
