package com.example.juegofinal;

import java.util.Iterator;
import java.util.LinkedList;

public class TileMap {

    private Tile tiles[][];
    private Player player;
    private Tile goal;
    private LinkedList<Enemy> enemies;
    private LinkedList<PowerUp>powerups;
    public int goalX;
    public int goalY;
    private Grid grid;

    /**
     * Grid class used for the pathfinding. Based on the actual map TileMap (needs to be loaded)
     */
    public void setGrid(){

        int width = getWidth();
        int height = getHeight();
        boolean[][] tiles = new boolean[width][height];

    // Fill it with values, false represents blocking tile
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                tiles[x][y] = getTile(x,y)!=null? false : true;

         grid = new Grid(width, height, tiles);

    }

    public Grid getGrid(){
    return grid;
    }

    public Tile getGoal(){
        return goal;
    }

    public int getEnemyN(){
        return enemies.size();

    }
    /**
     CREATES A MAP WITH WIDTH AND HEIGHT AS NUMBER OF TILES
     */
    TileMap(int width, int height){
        tiles = new Tile[width][height];
        enemies = new LinkedList();
        powerups = new LinkedList();
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
    public void setTile(int x, int y, Tile tile, boolean t) {
        goalX=x;
        goalY=y;
        goal = tile;
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

    /**
     * add powerup to this map
     */
    public void addPowerUp(PowerUp s){
        powerups.add(s);
    }
    /**
     * remove powerup to this map
     */
    public void removePowerUp(PowerUp s){
        powerups.remove(s);
    }
    /**
     * returns an iterator to iterate the powerups list
     */
    public Iterator getPowerUps(){
        return powerups.iterator();
    }

    public boolean noEnemies() { return enemies.size() == 0; }

}
