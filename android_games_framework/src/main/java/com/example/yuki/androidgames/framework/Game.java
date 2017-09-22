package com.example.yuki.androidgames.framework;

/**
 * Created by Yuki on 2017/09/12.
 */

public interface Game {
    public Input getInput();
    public FileIO getFileIO();
    public Graphics getGraphics();
    public Audio getAudio();
    public void setScreen(Screen screen);
    public Screen getCurrentScreen();
    public Screen getStartScreen();
}
