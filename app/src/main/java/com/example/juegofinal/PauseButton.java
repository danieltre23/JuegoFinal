package com.example.juegofinal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PauseButton {

    int x = 50, y = 50;
    Bitmap pause;

    PauseButton (Resources res) {

        pause = BitmapFactory.decodeResource(res, R.drawable.pause);
        pause = Bitmap.createScaledBitmap(pause, 100, 100, false);

    }

}
