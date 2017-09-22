package com.example.yuki.androidgames.framework_creating;

import android.graphics.Color;

import com.example.yuki.androidgames.framework.Game;
import com.example.yuki.androidgames.framework.Graphics;
import com.example.yuki.androidgames.framework.Input;
import com.example.yuki.androidgames.framework.Screen;

import java.util.List;

/**
 * Created by Yuki on 2017/09/14.
 */

public class MainMenuScreen extends Screen {
    private static final int LOGO_X = 30, LOGO_Y = 100,
                             MENU_X = 80, MENU_Y = 330,
                             SOUND_X = 0, SOUND_Y = 580;
    private static final float HOLDING_TIME = 3.0f;

    private float specialT = 0;
    private boolean holding = false;
    private boolean special = false;

    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        // special check
        if(holding) specialT += deltaTime;
        if(specialT >= HOLDING_TIME){
            special = !special;
            holding = false;
            specialT = 0;
        }

        int len = touchEvents.size();
        for (int i=0;i<len;i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if(event.type == Input.TouchEvent.TOUCH_UP) {
                holding = false;
                specialT = 0;
                if (isBounds(event, SOUND_X, SOUND_Y, 60, 60)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                } else if (isBounds(event, MENU_X, MENU_Y, 200, 80)) {
                    game.setScreen(new GameScreen(game,special));
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                } else if (isBounds(event, MENU_X, MENU_Y + 80, 200, 40)) {
                    game.setScreen(new HighScoreScreen(game));
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                } else if (isBounds(event, MENU_X, MENU_Y + 120, 200, 40)) {
                    game.setScreen(new HelpScreen(game));
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                }
            }else if(event.type == Input.TouchEvent.TOUCH_DOWN) {
                if (isBounds(event, LOGO_X, LOGO_Y, 300,200)) {
                    holding = true;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.background,0,0);

        if(special) {
            for (int i=0;i<640;i+=40){
                for (int j=0;j<360;j+=40) {
                    if((i+j)%80 == 0) {
                        g.drawRect(j,i,40,40,Color.GRAY);
                    }
                }
            }
        }

        g.drawPixmap(Assets.logo,LOGO_X,LOGO_Y);
        g.drawPixmap(Assets.mainMenu,MENU_X,MENU_Y);
        if(Settings.soundEnabled) {
            g.drawPixmap(Assets.buttons,SOUND_X,SOUND_Y,0,0,60,60);
        }else{
            g.drawPixmap(Assets.buttons,SOUND_X,SOUND_Y,60,0,60,60);
        }
    }

    @Override
    public void pause() {
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}
