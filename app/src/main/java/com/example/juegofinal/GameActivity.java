package com.example.juegofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private  RelativeLayout screenLayout;
    private int tile, screenW, screenH;
    private  RelativeLayout joystickLayout, pauseBtnLayout;
    private  RelativeLayout.LayoutParams params;
    private boolean firstTime;
    private int lastX = -400, lastY= -400;

    private Rect getJoystickRect(){
        return new Rect(lastX, lastY, tile*4, tile*4);
    }

    /**
     * listen for touch
     */
    private View.OnTouchListener gameViewListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            if(e.getAction() != MotionEvent.ACTION_MOVE && !getJoystickRect().contains((int)e.getX(), (int)e.getY())) {
                change((int) e.getX(), (int) e.getY());
            }
            return true;
        }
    };

    /**
     * change the joystick view or add it in the first place
     */
    private void change(int x, int y){

        Log.i("x,y", "" +x+" "+ y);

        if(!firstTime) {
            screenLayout.removeView(joystickLayout);
        }

        if(y>=screenH*0.15) {

            params.leftMargin = x - tile;
            params.topMargin = y - tile;

            params.leftMargin = Math.max(params.leftMargin, 0);
            params.leftMargin = Math.min(params.leftMargin, screenW - tile*4);
            params.topMargin = Math.min(params.topMargin, screenH -tile*4);

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

        //Get the parameters sent
        Intent myIntent = getIntent();
        int nivel = myIntent.getExtras().getInt("nivel");
        int health = myIntent.getExtras().getInt("health");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        screenW = point.x;
        screenH = point.y;
        firstTime = true;

        //add the gameview to the layout
        screenLayout = new RelativeLayout(this);
        tile = point.x/10;
        gameView = new GameView(this, screenW, screenH,tile, nivel, health);
        gameView.setOnTouchListener(gameViewListener);

        params = new RelativeLayout.LayoutParams(tile*4, tile*4);

        LayoutInflater inflater = LayoutInflater.from(this);
        joystickLayout = (RelativeLayout) inflater.inflate(R.layout.joystick, null, false);

        joystick = (JoystickView) joystickLayout.findViewById(R.id.joystickView);

        //listen for joystick movement
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

        pauseBtnLayout = (RelativeLayout) inflater.inflate(R.layout.pausebtn, null, false);
        screenLayout.addView(pauseBtnLayout);

        //pause listener
        pauseBtnLayout.findViewById(R.id.pause).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gameView.getMap().getPlayer().getSoundPool().autoPause();  //pause sounds
                startActivity(new Intent(GameActivity.this, Pause.class));
            }
        });

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(gameView);
        System.gc();
    }

    /**
     * unbind when changing activity
     * @param view
     */
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
