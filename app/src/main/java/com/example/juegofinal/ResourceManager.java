package com.example.juegofinal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

    private Animation animE1;
    private Animation []animP1;
    private Animation animED;
    private int starSize;
    private int playerSize;
    private int enemySize;
    private Bitmap background;



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

        x = BitmapFactory.decodeResource(game.getResources(),R.drawable.black);
        x = Bitmap.createScaledBitmap(x, tile_size, tile_size,false);
        x = TileMapDraw.createImage(tile_size,tile_size, Color.rgb(9,46,44));
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
            x = Bitmap.createScaledBitmap(x, starSize, starSize,false);
            framesG[i-1] =x;
        }

        animG = new Animation(framesG,210);




        Bitmap[] framesE1 = new Bitmap[5];

        Bitmap image1, crop1;


        image1 = BitmapFactory.decodeResource(game.getResources(), game.getResources().getIdentifier("virus", "drawable", game.pac));
        double dist1[] = {0,(68.0/322)*image1.getWidth(),(132.0/322)*image1.getWidth(),(199.0/322)*image1.getWidth(),(262.0/322)*image1.getWidth(), (322.0/322)*image1.getWidth()};
        Log.i("virus", " "+ image1.getWidth());

        for(int j = 0; j<=4; j++){
            crop1 = Bitmap.createBitmap(image1, (int) (dist1[j]), 0, (int) (dist1[j+1]-dist1[j]) , image1.getHeight()/2);
            crop1 = Bitmap.createScaledBitmap(crop1, enemySize, enemySize,false);
            framesE1[j] = crop1;
        }

        animE1 = new Animation (framesE1, 120);
        framesE1 = new Bitmap[6];

        image1 = BitmapFactory.decodeResource(game.getResources(), game.getResources().getIdentifier("virus", "drawable", game.pac));
        double dist2[] = {0,(60.0/322)*image1.getWidth(),(107.0/322)*image1.getWidth(),(142.0/322)*image1.getWidth(),(165.0/322)*image1.getWidth(),(200.0/322)*image1.getWidth(),(234.0/322)*image1.getWidth()};

        for(int j = 0; j<=5; j++){
            crop1 = Bitmap.createBitmap(image1, (int)(dist2[j]), image1.getHeight()/2, (int) (dist2[j+1]-dist2[j]) ,image1.getHeight()/2);
            crop1 = Bitmap.createScaledBitmap(crop1, enemySize, enemySize,false);
            framesE1[j] = crop1;
        }

        animED = new Animation (framesE1, 110);


        animP1 = new Animation[9] ;
        Bitmap[] framesP1 = new Bitmap[1];


        Bitmap image, crop;


                image = BitmapFactory.decodeResource(game.getResources(), R.drawable.npa7);
                image = Bitmap.createBitmap(image, 0, 0, image.getWidth()/4, image.getHeight());

                 image = Bitmap.createScaledBitmap(image, playerSize,  playerSize,false);
                framesP1[0] = image;


            animP1[0] = new Animation(framesP1, 150);
            framesP1 = new Bitmap[4];


        for(int i=1; i<=8; i++){
            file = "npa"+i;
            image = BitmapFactory.decodeResource(game.getResources(), game.getResources().getIdentifier(file, "drawable", game.pac));
            for(int j = 0; j<=3; j++){
                crop = Bitmap.createBitmap(image, j*image.getWidth()/4, 0, image.getWidth()/4, image.getHeight());
                crop = Bitmap.createScaledBitmap(crop, playerSize, playerSize,false);
                framesP1[j] = crop;
            }
            animP1[i] = new Animation(framesP1, 150);
            framesP1 = new Bitmap[4];
        }

    }
    /*
        1 2 3
        4 0 5
        6 7 8
        */

    public ResourceManager(GameView g){

        game = g;
        starSize = (int)(tile_size*2.0);
        enemySize = (int)(tile_size*1.5);
        playerSize = (int)(tile_size*2.0);

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
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x)- starSize/4, tilesToPixels(y), starSize, starSize,animG),true);
                }
                else if (ch == '1') {
                    newMap.addEnemy(new Enemy1(game,tilesToPixels(x)-enemySize/4, tilesToPixels(y)-enemySize/2, enemySize, enemySize,animE1, animED));
                }
            }
        }

        // add the player to the map (position with map)
        newMap.setPlayer(new Player(game, tilesToPixels(width)/2 - playerSize/2, tilesToPixels(height-16)-playerSize, playerSize, playerSize,  animP1));

        return newMap;
    }

}
