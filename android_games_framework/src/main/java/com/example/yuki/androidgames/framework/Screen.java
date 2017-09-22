package com.example.yuki.androidgames.framework;

/**
 * Created by Yuki on 2017/09/12.
 */

public abstract class Screen {
    protected final Game game;

    public Screen(Game game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);
    public abstract void present(float deltaTime);

    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();

    protected boolean isBounds(Input.TouchEvent event, int x, int y,int width,int height){
        if(event.x > x && event.x < x + width -1 &&
                event.y > y && event.y < y + height -1){
            return true;
        }else {
            return false;
        }
    }
}
