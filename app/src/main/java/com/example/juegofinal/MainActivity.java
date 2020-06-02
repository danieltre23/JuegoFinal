package com.example.juegofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.media.SoundPool;

public class MainActivity extends AppCompatActivity {

    public static boolean soundOn = true;
    private int sound1,sound2;
    SoundPool soundPool;
    MediaPlayer music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC, 0);
        sound1 = soundPool.load(this, R.raw.backsound, 1);  //walking
        sound2 = soundPool.load(this, R.raw.coin, 1);  //walking

        music = MediaPlayer.create(this, R.raw.backsound);

        final ImageView iMusica =  findViewById(R.id.sonido);

        if(soundOn){
            iMusica.setImageResource(R.drawable.musicwhite);
            if(music.isPlaying()==false){
                music.start();
                music.setLooping(true);
            }
        }
        else{
            music.pause();
            iMusica.setImageResource(R.drawable.music);
        }

        findViewById(R.id.sonido).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                soundOn = soundOn? false: true;
                if(soundOn){
                    iMusica.setImageResource(R.drawable.musicwhite);
                    music.start();
                    music.setLooping(true);
                }
                else{
                    iMusica.setImageResource(R.drawable.music);
                    music.pause();
                }
            }
        });

        findViewById(R.id.jugar).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                music.pause();
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
    }
    @Override
    protected void onDestroy() {
        music.stop();
        super.onDestroy();
    }
}
