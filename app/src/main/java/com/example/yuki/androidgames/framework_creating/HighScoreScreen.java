package com.example.yuki.androidgames.framework_creating;

import android.graphics.Color;

import com.example.yuki.androidgames.framework.Game;
import com.example.yuki.androidgames.framework.Graphics;
import com.example.yuki.androidgames.framework.Input;
import com.example.yuki.androidgames.framework.Screen;

import java.util.List;

/**
 * Created by Yuki on 2017/09/16.
 */

public class HighScoreScreen extends Screen {
    public static final int TITLE_X = 40, TITLE_Y = 160, TITLE_SIZE = 50,
                            SCORE_X = 80, SCORE_SIZE = 40,
                            BACK_X = 0, BACK_Y = 580;

    public HighScoreScreen(Game game){
        super(game);
    }
    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();
        for (int i=0;i<len;i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if(event.type != Input.TouchEvent.TOUCH_UP) continue;
            if(isBounds(event,BACK_X,BACK_Y,60,60)){
                if(Settings.soundEnabled) Assets.click.play(1);
                game.setScreen(new MainMenuScreen(game));
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.background,0,0);
        g.drawText("HIGE SCORES",TITLE_X,TITLE_Y, Color.BLACK,TITLE_SIZE);
        for(int i=0;i<5;i++) {
            g.drawText(
                    (i+1) + ": " + String.valueOf(Settings.highscores[i]),
                    SCORE_X,
                    TITLE_Y + 60*(i+1) + 40,
                    Color.BLACK,
                    SCORE_SIZE
            );
        }
        g.drawPixmap(Assets.buttons,BACK_X,BACK_Y,0,120,60,60);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
