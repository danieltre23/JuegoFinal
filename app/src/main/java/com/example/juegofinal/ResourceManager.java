package com.example.juegofinal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.FileReader;
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

    private Animation animE1[];
    private Animation animP1[];

    private static final int TILE_SIZE = 64;
    // the size in bits of the tile
    // Math.pow(2, TILE_SIZE_BITS) == TILE_SIZE
    private static final int TILE_SIZE_BITS = 6;

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


    public ResourceManager(GameView g){
        game = g;
        //create animations
        Bitmap x;

        //imagenes a b y c


        x = BitmapFactory.decodeResource(g.getResources(),R.drawable.a);
        x = Bitmap.createScaledBitmap(x, TILE_SIZE, TILE_SIZE,false);
        Bitmap[] framesA = {x};

        animA = new Animation(framesA,100);

        x = BitmapFactory.decodeResource(g.getResources(),R.drawable.b);
        x = Bitmap.createScaledBitmap(x, TILE_SIZE, TILE_SIZE,false);
        Bitmap[] framesB = {x};

        animB = new Animation(framesB,100);

        x = BitmapFactory.decodeResource(g.getResources(),R.drawable.c);
        x = Bitmap.createScaledBitmap(x, TILE_SIZE, TILE_SIZE,false);
        Bitmap[] framesC = {x};

        animC = new Animation(framesC,100);

        x = BitmapFactory.decodeResource(g.getResources(),R.drawable.g);
        x = Bitmap.createScaledBitmap(x, TILE_SIZE, TILE_SIZE,false);
        Bitmap[] framesG = {x};

        animG = new Animation(framesG,100);

        x = BitmapFactory.decodeResource(g.getResources(),R.drawable.enemy1);
        x = Bitmap.createScaledBitmap(x, TILE_SIZE, TILE_SIZE,false);

        Bitmap[] framesE1 = {x};

        x = BitmapFactory.decodeResource(g.getResources(),R.drawable.player1);
        x = Bitmap.createScaledBitmap(x, TILE_SIZE, TILE_SIZE,false);

        Bitmap[] framesP1 = {x};

        for(int i=0; i<5; i++){
            animE1[i] = new Animation(framesE1,100);
        }

        for(int i=0; i<5; i++){
            animP1[i] = new Animation(framesP1,100);
        }


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

        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.

                // check if the char represents a sprite
                 if (ch == 'A') {
                    newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),animA));
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

        // add the player to the map (generic position)
        newMap.setPlayer(new Player(game, animP1));

        return newMap;
    }

}
