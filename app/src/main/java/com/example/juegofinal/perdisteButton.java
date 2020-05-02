package com.example.juegofinal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class perdisteButton {

    int x = 200, y = 200;
    Bitmap perdiste;

    perdisteButton (Resources res) {

        perdiste = BitmapFactory.decodeResource(res, R.drawable.sick);
        perdiste = Bitmap.createScaledBitmap(perdiste, 100, 100, false);

    }

}
