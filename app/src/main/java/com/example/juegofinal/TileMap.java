package com.example.juegofinal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Iterator;
import java.util.LinkedList;

public class TileMap {

    private Tile tiles[][];
    private Player player;
    private LinkedList<Enemy> enemies;


    /**
      CREATES A MAP WITH WIDTH AND HEIGHT AS NUMBER OF TILES
     */

    TileMap(int width, int height){
        tiles = new Tile[width][height];
        enemies = new LinkedList();
        player =  null;
    }
    /**
     Gets the width of this TileMap (number of tiles across).
     */
    public int getWidth() {
        return tiles.length;
    }


    /**
     Gets the height of this TileMap (number of tiles down).
     */
    public int getHeight() {
        return tiles[0].length;
    }


    /**
     Gets the tile at the specified location. Returns null if
     no tile is at the location or if the location is out of
     bounds.
     */
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= getWidth() ||
                y < 0 || y >= getHeight())
        {
            return null;
        }
        else {
            return tiles[x][y];
        }
    }


    /**
     Sets the tile at the specified location.
     */
    public void setTile(int x, int y, Tile tile) {
        tiles[x][y] = tile;
    }


    /**
     Gets the player Sprite.
     */
    public Player getPlayer() {
        return player;
    }


    /**
     Sets the player Sprite.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }


    /**
     Adds a Sprite object to this map.
     */
    public void addEnemy(Enemy e) {
        enemies.add(e);
    }


    /**
     Removes a Sprite object from this map.
     */
    public void removeEnemy(Enemy e) {
        enemies.remove(e);
    }


    /**
     Gets an Iterator of all the Sprites in this map,
     excluding the player Sprite.
     */
    public Iterator getEnemies() {
        return enemies.iterator();
    }

}
