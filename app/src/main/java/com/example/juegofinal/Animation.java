package com.example.juegofinal;


import android.graphics.Bitmap;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mauricio
 */
public class Animation {

    private int speed;
    private int index;
    private long lastTime;
    private long timer;
    private Bitmap[] frames;
    public String id;

    /**
     * initializes the values for the animation
     * @param frames the frames
     * @param speed the speed
     */
    public Animation(Bitmap[] frames, int speed) {
        this.frames= frames;
        this.speed = speed;
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis();
        id = "ekis";

    }


    /**
     * get the speed of the animation
     * @return speed
     */
    public int getSpeed(){
        return speed;
    }

    /**
     * get the length of the animation
     * @return length
     */
    public int getLength(){
        return frames.length;
    }

    /**
     * get the index of the animation
     * @return index
     */
    public int getIndex(){
        return index;
    }

    /**
     * set the index for the animation
     * @param c
     */
    public void setIndex(int c){
        index = c;
    }

    /**
     * get the current frame of the animation
     * @return
     */
    public Bitmap getCurrentFrame(){
        return frames[index];
    }

    /**
     * tick the animation
     */
    public void update(){
        timer+= System.currentTimeMillis()-lastTime;
        lastTime = System.currentTimeMillis();

        if(timer>speed){
            index++;
            timer = 0;

            if(index>=frames.length){
                index=0;
            }
        }
    }

    public void start(){
        index=0;
        timer=0;
    }
}

