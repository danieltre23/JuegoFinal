package com.example.juegofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import static com.example.juegofinal.MainActivity.soundOn;


public class Pause extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        //set layout
        setContentView(R.layout.activity_pause);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ImageView iMusica =  findViewById(R.id.sonido);

        //play music if soundOn
        if(soundOn){
            iMusica.setImageResource(R.drawable.musicwhite);
        }
        else{
            iMusica.setImageResource(R.drawable.music);
        }

        //return to game
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Pause.this, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //go to home
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pause.this, MainActivity.class));
            }
        });

        //toggle sound
        findViewById(R.id.sonido).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                soundOn = soundOn? false: true;
                if(soundOn){
                    iMusica.setImageResource(R.drawable.musicwhite);
                }
                else{
                    iMusica.setImageResource(R.drawable.music);
                }
            }
        });
    }
}
