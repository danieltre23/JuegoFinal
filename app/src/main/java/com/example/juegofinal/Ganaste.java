package com.example.juegofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class Ganaste extends AppCompatActivity {

    int nivelMax = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganaste);

        //get parameters sent
        Intent myIntent = getIntent();
        final int nivel = myIntent.getExtras().getInt("nivel");
        final int health = myIntent.getExtras().getInt("health");

        //get views
        final TextView textViewNivel = (TextView)findViewById(R.id.nivel);
        final TextView textViewFrase = (TextView)findViewById(R.id.frase);
        final ImageView imageView = (ImageView)findViewById(R.id.play);

        textViewNivel.setText(String.valueOf(nivel));

        if(nivel == nivelMax){
            textViewFrase.setText("Ganaste");

            imageView.setImageResource(R.drawable.home);
            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Ganaste.this, MainActivity.class));
                }
            });
        }else {
            //Get random phrase to show
            String[] frases = {
                    "Ventila espacios cerrados para evitar los viruses",
                    "Lavate las manos frecuentemente",
                    "Cubrete la boca al toser o estornudar",
                    "Evita tocarte la cara con las manos sucias",
                    "Si estas enfermo, quedate en casa",
                    "No olvides tu cubrebocas",

            };
            Random random = new Random();
            int index = random.nextInt(frases.length);
            textViewFrase.setText(frases[index]);

            //Go to gameActivity and send parameters
            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent gameIntent = new Intent(Ganaste.this, GameActivity.class);
                    gameIntent.putExtra("nivel", nivel+1);
                    gameIntent.putExtra("health", health);
                    startActivity(gameIntent);
                }
            });
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
