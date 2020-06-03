package com.example.juegofinal;

import android.animation.ArgbEvaluator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import static com.example.juegofinal.GameView.tile_size;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ResourceManager {

    private GameView game;
    private Bitmap bA, bB, bC, bD, bE , bR, bL, bU, background, bitmapPowerUp;
    private Animation animE1, animED, animG;
    private Animation []animP1;

    private int starSize,playerSize,enemySize, backgroundheightSize;


    /**
     Converts a tile position to a pixel position.
     */
    public  int tilesToPixels(int numTiles) {
        // use this if the tile size isn't a power of 2:
        return numTiles * tile_size;
    }

    public Bitmap getBackground(){
        return background;
    }

    /**
     * Load Images from drawables to create bitmaps and animations
     */
    void initAnimations(){
        Bitmap x;

        // powerup image
        bitmapPowerUp = BitmapFactory.decodeResource(game.getResources(), R.drawable.oie_transparent);
        bitmapPowerUp = Bitmap.createScaledBitmap(bitmapPowerUp,tile_size,tile_size,false);

        //image a b c d e l u r
        bA = BitmapFactory.decodeResource(game.getResources(),R.drawable.platform_tile_021);
        bA = Bitmap.createScaledBitmap(bA, tile_size, tile_size,false);

        bB = BitmapFactory.decodeResource(game.getResources(),R.drawable.platform_tile_026);
        bB = Bitmap.createScaledBitmap(bB, tile_size, tile_size,false);

        bC = BitmapFactory.decodeResource(game.getResources(),R.drawable.platform_tile_035);
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

        // goal (star) animation
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

        //enemy animation
        for(int j = 0; j<=4; j++){
            crop1 = Bitmap.createBitmap(image1, (int) (dist1[j]), 0, (int) (dist1[j+1]-dist1[j]) , image1.getHeight()/2);
            crop1 = Bitmap.createScaledBitmap(crop1, enemySize, enemySize,false);
            framesE1[j] = crop1;
        }

        animE1 = new Animation (framesE1, 120);
        framesE1 = new Bitmap[6];

        image1 = BitmapFactory.decodeResource(game.getResources(), game.getResources().getIdentifier("virus", "drawable", game.pac));
        double dist2[] = {0,(60.0/322)*image1.getWidth(),(107.0/322)*image1.getWidth(),(142.0/322)*image1.getWidth(),(165.0/322)*image1.getWidth(),(200.0/322)*image1.getWidth(),(234.0/322)*image1.getWidth()};

        //enemy dying animation
        for(int j = 0; j<=5; j++){
            crop1 = Bitmap.createBitmap(image1, (int)(dist2[j]), image1.getHeight()/2, (int) (dist2[j+1]-dist2[j]) ,image1.getHeight()/2);
            crop1 = Bitmap.createScaledBitmap(crop1, enemySize, enemySize,false);
            framesE1[j] = crop1;
        }

        animED = new Animation (framesE1, 110);


        // player animation (8 movements and standing)
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

    /**
     constructor
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

    /**
    darkens an image used to visual purposes
     */
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

    /**
     creates the background made of "tiles" as one image to the renderer to be faster.
     The background depends on the width and height of the actual map but has fixed number
     row of tiles above and below it. (backgroundheightsize)
     */
    public void setB(int width, int height){

        //outside
        Bitmap temp1 = bD;
        Bitmap accumH = temp1;

        for(int i=0; i<width-1; i++){
            accumH = combineBitmaps(accumH,temp1);
        }

        Bitmap accumV = accumH;

        for(int i = 0; i<backgroundheightSize-1;i++){
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

        // join outside / inside /outside
        background = combineBitmapsV(combineBitmapsV(accumV,accumV1),accumV);
    }

    /**
     use to create an image of one color only. with specified dimensions
     */
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

    /**
     * <p>This method combines two images into one by rendering them vertically.</p>
     *
     * @param up The image that goes up on the combined image
     * @param down The image that goes down  of the combined image.
     * @return The combined image.
     */
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
     * Reads a txt file with players, tiles, enemies, powerups and different dimensions to save it into
     * a new map and return it
     * @param id the raw resource id to acces it
     * @param res resources
     * @return the new map with all assets loaded
     */
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

        // the height is considering the fixed number of rows below and above the map
        int fullHeight = height+backgroundheightSize*2;

        // create new map with width and the fullheight. It is important so that the renderer could follow up the player to the edges of the map.
        TileMap newMap = new TileMap(width,fullHeight);

        String line;

        for (int y=0; y<fullHeight; y++) {
            //only read the actual map
            if(y>=backgroundheightSize && y<height+backgroundheightSize) {
                 line = (String) lines.get(y-backgroundheightSize);
            }
            else{
                line="";
            }
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C OR ENEMIES OR POWERUPS
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
                else if (ch=='P'){
                     newMap.addPowerUp(new PowerUp(tilesToPixels(x), tilesToPixels(y), tile_size, tile_size, bitmapPowerUp, game));
                 }
            }
        }
        // create background (static tiles outside the original map) and inside background too
        setB(width,height);

        // add the player to the map (position with map, below)
        newMap.setPlayer(new Player(game, tilesToPixels(width)/2 - playerSize/2, tilesToPixels(fullHeight-16)-playerSize, playerSize, playerSize,  animP1));

        Log.i("a", "mapa leido");
        return newMap;
    }

}
