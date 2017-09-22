package com.example.yuki.androidgames.framework.impl;

import android.graphics.Bitmap;

import com.example.yuki.androidgames.framework.Graphics;
import com.example.yuki.androidgames.framework.Pixmap;

/**
 * Created by Yuki on 2017/09/13.
 */

public class AndroidPixmap implements Pixmap {
    Bitmap bitmap;
    Graphics.PixmapFormat format;

    public AndroidPixmap(Bitmap bitmap, Graphics.PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }


    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Graphics.PixmapFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }
}
