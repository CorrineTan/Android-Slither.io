package com.packt.snake;

import android.graphics.Bitmap;
import android.view.View;
public class Screenshot {
    public static Bitmap takescreenshot(View v){
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }

    public static Bitmap takescreenshotRootView(View v) {
        return takescreenshot(v.getRootView());
    }
}
