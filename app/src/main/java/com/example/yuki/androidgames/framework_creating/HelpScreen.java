package com.example.yuki.androidgames.framework_creating;

import android.graphics.Color;

import com.example.yuki.androidgames.framework.Game;
import com.example.yuki.androidgames.framework.Graphics;
import com.example.yuki.androidgames.framework.Input;
import com.example.yuki.androidgames.framework.Screen;

import java.util.List;

/**
 * Created by Yuki on 2017/09/18.
 */

public class HelpScreen extends Screen {
    private static final int BACK_X = 0, BACK_Y = 580,
                             TITLE_X = 20, TITLE_Y = 80, TITLE_SIZE = 40,
                             EXPLAIN_X = 20, EXPLAIN_Y = 160, EXPLAIN_SIZE = 16,
                             EXPLAIN_BETWEEN = 40;

    public HelpScreen(Game game) {
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
        g.drawText("HOW TO PLAY",TITLE_X,TITLE_Y, Color.BLACK,TITLE_SIZE);
        g.drawText("操作: → 右回転",EXPLAIN_X,EXPLAIN_Y+EXPLAIN_BETWEEN*0,Color.BLACK,EXPLAIN_SIZE);
        g.drawText("　　  ← 左回転",EXPLAIN_X,EXPLAIN_Y+EXPLAIN_BETWEEN*1,Color.BLACK,EXPLAIN_SIZE);
        g.drawText("遊び方",EXPLAIN_X,EXPLAIN_Y+EXPLAIN_BETWEEN*3,Color.BLACK,EXPLAIN_SIZE);
        g.drawText("　Snakeを操作してえさを食べさせよう！",
                EXPLAIN_X,EXPLAIN_Y+EXPLAIN_BETWEEN*4,Color.BLACK,EXPLAIN_SIZE);
        g.drawText("　えさを食べるたびにSnakeは成長するぞ！",
                EXPLAIN_X,EXPLAIN_Y+EXPLAIN_BETWEEN*5,Color.BLACK,EXPLAIN_SIZE);
        g.drawText("　自分自身に噛み付いたらゲームオーバー！",
                EXPLAIN_X,EXPLAIN_Y+EXPLAIN_BETWEEN*6,Color.BLACK,EXPLAIN_SIZE);
        g.drawText("　ハイスコアでランクインを目指せ!",
                EXPLAIN_X,EXPLAIN_Y+EXPLAIN_BETWEEN*7,Color.BLACK,EXPLAIN_SIZE);

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
