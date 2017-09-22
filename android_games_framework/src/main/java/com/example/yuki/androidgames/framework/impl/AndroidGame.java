package com.example.yuki.androidgames.framework.impl;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.yuki.androidgames.framework.Audio;
import com.example.yuki.androidgames.framework.FileIO;
import com.example.yuki.androidgames.framework.Game;
import com.example.yuki.androidgames.framework.Graphics;
import com.example.yuki.androidgames.framework.Input;
import com.example.yuki.androidgames.framework.Screen;

/**
 * Created by Yuki on 2017/09/13.
 */

public abstract class AndroidGame extends Activity implements Game {

    public static int LONG_EDGE = 640;
    public static int SHORT_EDGE = 360;
    public static Bitmap.Config COLOR_FORMAT = Bitmap.Config.RGB_565;

    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        boolean isLandscape = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;

        int frameBufferWidth = isLandscape ? LONG_EDGE : SHORT_EDGE;
        int frameBufferHeight = isLandscape ? SHORT_EDGE : LONG_EDGE;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                                                frameBufferHeight,
                                                COLOR_FORMAT);
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);

        // 画面いっぱいにスケール
        float scaleX = (float) frameBufferWidth / displaySize.x;
        float scaleY = (float) frameBufferHeight / displaySize.y;

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(),frameBuffer);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getStartScreen();

        setContentView(renderView);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause(){
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        renderView.pause();
        screen.pause();

        if(isFinishing()) screen.dispose();
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if(screen == null) throw new IllegalArgumentException("Screen must NOT null");
        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

}
