package com.example.juegofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private  RelativeLayout screenLayout;
    private int tile, screenW, screenH;
    private  RelativeLayout joystickLayout;
    private  RelativeLayout.LayoutParams params;
    private boolean firstTime;
    private int lastX = -400, lastY= -400;

    private Rect getJoystickRect(){
        return new Rect(lastX, lastY, tile*2, tile*2);
    }



    private View.OnTouchListener gameViewListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            if(e.getAction()!= MotionEvent.ACTION_MOVE && !getJoystickRect().contains((int)e.getX(), (int)e.getY())) {
                change((int) e.getX(), (int) e.getY());
            }
            return true;
        }
    };

    private void change(int x, int y){

        Log.i("x,y", "" +x+" "+ y);

        if(!firstTime) {
            screenLayout.removeView(joystickLayout);
        }

        if(y>=screenH*0.15) {

            params.leftMargin = x - tile;
            params.topMargin = y - tile;

            params.leftMargin = Math.max(params.leftMargin, 0);
            params.leftMargin = Math.min(params.leftMargin, screenW - tile*2);
            params.topMargin = Math.min(params.topMargin, screenH -tile*2);

            screenLayout.addView(joystickLayout, params);

            lastX = params.leftMargin;
            lastY = params.topMargin;
            firstTime = false;
        }

        //addContentView(joystickLayout,params);


    }

    private JoystickView joystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        screenW = point.x;
        screenH = point.y;
        firstTime = true;

         screenLayout = new RelativeLayout(this);
        gameView = new GameView(this, screenW, screenH);
        gameView.setOnTouchListener(gameViewListener);


        tile = point.x/5;

        params = new RelativeLayout.LayoutParams(tile*2, tile*2);

        LayoutInflater inflater = LayoutInflater.from(this);
         joystickLayout = (RelativeLayout) inflater.inflate(R.layout.joystick, null, false);

         joystick = (JoystickView) joystickLayout.findViewById(R.id.joystickView);

        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                // do whatever you want
                int x = (int) (Math.cos(angle*3.14/180) * Math.floor(strength*5/100));
                int y = (int) (Math.sin(angle*3.14/180) * Math.floor(strength*5/100)) * -1;
                gameView.getMap().getPlayer().setDx(x);
                gameView.getMap().getPlayer().setDy(y);
                gameView.getMap().getPlayer().updateDir(angle);
            }
        });

        screenLayout.addView(gameView);





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
