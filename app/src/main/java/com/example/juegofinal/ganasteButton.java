package com.example.juegofinal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ganasteButton {

    int x = 800, y = 200;
    Bitmap ganaste;

    ganasteButton(Resources res) {

        ganaste = BitmapFactory.decodeResource(res, R.drawable.smily);
        ganaste = Bitmap.createScaledBitmap(ganaste, 100, 100, false);

    }

}
