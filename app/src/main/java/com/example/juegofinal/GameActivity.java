package com.example.juegofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        RelativeLayout screenLayout = new RelativeLayout(this);
        gameView = new GameView(this, point.x, point.y);

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout joystickLayout = (RelativeLayout) inflater.inflate(R.layout.joystick, null, false);

        JoystickView joystick = (JoystickView) joystickLayout.findViewById(R.id.joystickView);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                // do whatever you want
                int x = (int) (Math.cos(angle*3.14/180) * Math.floor(strength*5/100));
                int y = (int) (Math.sin(angle*3.14/180) * Math.floor(strength*5/100)) * -1;
                System.out.println("x = "+x+" , y =  " + y);
                gameView.getMap().getPlayer().move(x, y);
            }
        });

        screenLayout.addView(gameView);
        screenLayout.addView(joystickLayout);

        setContentView(screenLayout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
