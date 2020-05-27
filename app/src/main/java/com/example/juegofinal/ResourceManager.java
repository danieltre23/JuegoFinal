package com.example.juegofinal;

import android.animation.ArgbEvaluator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
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
    private Bitmap bA, bB, bC, bD, bE , bR, bL, bU, background;
    private Animation animE1, animED, animG;
    private Animation []animP1;

    private int starSize,playerSize,enemySize, backgroundheightSize;


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

    public Bitmap getBackground(){
        return background;
    }

    void initAnimations(){
        Bitmap x,y,w,z;

        //imagenes a b y c

        bA = BitmapFactory.decodeResource(game.getResources(),R.drawable.platform_tile_021);
        bA = Bitmap.createScaledBitmap(bA, tile_size, tile_size,false);

        bB = BitmapFactory.decodeResource(game.getResources(),R.drawable.platform_tile_018);
        bB = Bitmap.createScaledBitmap(bB, tile_size, tile_size,false);
        //bB = TileMapDraw.createImage(tile_size,tile_size, Color.rgb(9,46,44));

        bC = BitmapFactory.decodeResource(game.getResources(),R.drawable.c);
        bC = Bitmap.createScaledBitmap(bC, tile_size, tile_size,false);

        bD = BitmapFactory.decodeResource(game.getResources(),R.drawable.platform_tile_011);
        bD = Bitmap.createScaledBitmap(bD, tile_size, tile_size,false);

        bE = BitmapFactory.decodeResource(game.getResources(),R.drawable.platform_tile_015);
        bE = Bitmap.createScaledBitmap(bE, tile_size, tile_size,false);

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        bL = Bitmap.createBitmap(bE, 0, 0, tile_size,tile_size, matrix, true);
        bU = Bitmap.createBitmap(bL, 0, 0, tile_size, tile_size, matrix, true);
        bR = Bitmap.createBitmap(bU, 0, 0, tile_size, tile_size, matrix, true);


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
        Bitmap[] framesP1 = new Bitmap[2];


        Bitmap image, crop;


                image = BitmapFactory.decodeResource(game.getResources(), R.drawable.npa7);
                crop = Bitmap.createBitmap(image, 0, 0, image.getWidth()/4, image.getHeight());
                crop = Bitmap.createScaledBitmap(crop, playerSize,  playerSize,false);
                framesP1[0] = crop;
                crop = Bitmap.createBitmap(image, image.getWidth()/4, 0, image.getWidth()/4, image.getHeight());
                crop = Bitmap.createScaledBitmap(crop, playerSize,  playerSize,false);
                framesP1[1] = crop;

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
        enemySize = (int)(tile_size);
        playerSize = (int)(tile_size*2.0);
        backgroundheightSize = 14;

        //create animations
        initAnimations();

    }


    private Bitmap darkenBitMap(Bitmap bitmap) {
        int lighten = 0;
        Bitmap bmpGrayscale = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);

        Paint grayscalePaint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        grayscalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
        c.drawBitmap(bitmap, new Matrix(), grayscalePaint);

        ArgbEvaluator evaluator = new ArgbEvaluator();

        float fraction = lighten / 100.0F;

        int mul = (int) evaluator.evaluate(fraction, 0xFF7F7F7F, 0xFFFFFFFF);
        int add = (int) evaluator.evaluate(fraction, 0x00000000, 0x00222222);

        Paint lighteningPaint = new Paint();
        lighteningPaint.setColorFilter(new LightingColorFilter(mul, add));
        c.drawBitmap(bmpGrayscale, new Matrix(), lighteningPaint);

        return (bmpGrayscale);

    }

    public void setB(int width, int height){

        //outside
        Bitmap temp1 = bD;
        Bitmap accumH = temp1;

        for(int i=0; i<width-1; i++){
            accumH = combineBitmaps(accumH,temp1);
        }

        Bitmap accumV = accumH;

        for(int i = 0; i<13;i++){
            accumV = combineBitmapsV(accumV,accumH);
        }


        //inside

        //horizontal
        Bitmap temp = bD;
        Bitmap bD1 = darkenBitMap(temp);
        Bitmap accumH1 = bD1;

        for(int i=0; i<width-1; i++){
            accumH1 = combineBitmaps(accumH1,bD1);
        }

        //vertical
        Bitmap accumV1 = accumH1;

        for(int i = 0; i<height-1;i++){
            accumV1 = combineBitmapsV(accumV1,accumH1);
        }

        background = combineBitmapsV(combineBitmapsV(accumV,accumV1),accumV);
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

        int fullHeight = height+backgroundheightSize*2;

        //???
        bD = BitmapFactory.decodeResource(game.getResources(),R.drawable.platform_tile_011);
        bD = Bitmap.createScaledBitmap(bD, tile_size, tile_size,false);

        TileMap newMap = new TileMap(width,fullHeight);
        String line;

        for (int y=0; y<fullHeight; y++) {
            if(y>=backgroundheightSize && y<height+backgroundheightSize) {
                 line = (String) lines.get(y-backgroundheightSize);
            }
            else{
                line="";
            }
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.

                // check if the char represents a sprite
                 if (ch == 'A') {
                    newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),bA));
                 }
                else if (ch == 'B') {
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),bB));
                }
                else if (ch == 'C') {
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),bC));
                }
                else if (ch =='D'){
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),bD));
                 }
                 else if (ch =='E'){
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),bE));
                 }
                 else if (ch == 'R') {
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),bR));
                 }
                 else if (ch =='L'){
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),bL));
                 }
                 else if (ch =='U'){
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x), tilesToPixels(y),bU));
                 }
                else if (ch == 'G') {
                     newMap.setTile(x,y, new Tile(game, tilesToPixels(x)- starSize/4, tilesToPixels(y), starSize, starSize,animG),true);
                }
                else if (ch == '1') {
                    newMap.addEnemy(new Enemy1(game,tilesToPixels(x), tilesToPixels(y), enemySize, enemySize,animE1, animED));
                }
                else if (ch == '2') {
                    newMap.addEnemy(new Enemy2(game,tilesToPixels(x), tilesToPixels(y), enemySize, enemySize,animE1, animED));
                }
            }
        }
        // create background (static tiles outside the original map)
        setB(width,height);

        // add the player to the map (position with map)
        newMap.setPlayer(new Player(game, tilesToPixels(width)/2 - playerSize/2, tilesToPixels(fullHeight-16)-playerSize, playerSize, playerSize,  animP1));

        return newMap;
    }

}
