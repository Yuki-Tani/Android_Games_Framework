package com.example.yuki.androidgames.framework;

/**
 * Created by Yuki on 2017/09/12.
 */

public interface Pixmap {
    public int getWidth();
    public int getHeight();
    public Graphics.PixmapFormat getFormat();
    public void dispose();
}
