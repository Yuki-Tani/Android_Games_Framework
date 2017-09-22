package com.example.yuki.androidgames.framework_creating;

import android.graphics.Color;

import com.example.yuki.androidgames.framework.Game;
import com.example.yuki.androidgames.framework.Graphics;
import com.example.yuki.androidgames.framework.Input;
import com.example.yuki.androidgames.framework.Pixmap;
import com.example.yuki.androidgames.framework.Screen;
import com.example.yuki.androidgames.framework.impl.AndroidGame;

import java.util.List;

/**
 * Created by Yuki on 2017/09/16.
 */

public class GameScreen extends Screen {
    enum GameState {
        Ready,
        Running,
        Paused,
        GameOver
    }
    private static final int PAUSE_X = 0,   PAUSE_Y = 0,
                             LEFT_X = 10,    LEFT_Y = 570,
                             RIGHT_X = 290, RIGHT_Y = 570,
                             UNDERLINE_Y = 560,
                             GUIDE_X = 40, GUIDE_Y = 200,  GUIDE_SIZE = 50,
                             SCORE_X = 150, SCORE_Y = 620,  SCORE_SIZE = 50,
                             FIN_X = 45, FIN_Y = 240;

    GameState state = GameState.Ready;
    GameSystem system;
    int oldScore = 0;
    String score = "0";

    public GameScreen(Game game) {this(game,false);}
    public GameScreen(Game game,boolean special) {
        super(game);
        system = new GameSystem(special);
    }

    //////////////////// update
    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        switch(state) {
            case Ready: updateReady(touchEvents,deltaTime); break;
            case Running: updateRunning(touchEvents,deltaTime); break;
            case Paused: updatePaused(touchEvents,deltaTime); break;
            case GameOver: updateGameOver(touchEvents,deltaTime); break;
        }
    }

    private void updateReady(List<Input.TouchEvent> events,float delta){
        if(events.size()>0) state = GameState.Running;
    }

    private void updateRunning(List<Input.TouchEvent> events,float delta){
        int len = events.size();
        for (int i=0;i<len;i++) {
            Input.TouchEvent event = events.get(i);
            if(event.type == Input.TouchEvent.TOUCH_UP) {
                if(isBounds(event,PAUSE_X,PAUSE_Y,60,60)){
                    if(Settings.soundEnabled) Assets.click.play(1);
                    state = GameState.Paused;
                    return;
                }
            } else if(event.type == Input.TouchEvent.TOUCH_DOWN) {
                if(isBounds(event,LEFT_X-10,LEFT_Y-10,80,80)){
                    if(system.snake.change != Snake.LEFT) {
                        system.snake.turnLeft();
                        system.snake.change -= 1;
                    }
                } else if(isBounds(event,RIGHT_X-10,RIGHT_Y-10,80,80)){
                    if(system.snake.change != Snake.RIGHT) {
                        system.snake.turnRight();
                        system.snake.change += 1;
                    }
                }
            }
        }

        system.update(delta);
        if(system.gameOver) {
            if(Settings.soundEnabled) Assets.miss.play(1);
            state = GameState.GameOver;
        }
        if(oldScore != system.score) {
            oldScore = system.score;
            score = String.valueOf(oldScore);
            if(Settings.soundEnabled) Assets.eat.play(1);
        }
    }

    private void updatePaused(List<Input.TouchEvent> events,float delta){
        int len = events.size();
        for (int i=0;i<len;i++) {
            Input.TouchEvent event = events.get(i);
            if(event.type == Input.TouchEvent.TOUCH_UP) {
                if(isBounds(event,GUIDE_X,GUIDE_Y-GUIDE_SIZE,300,GUIDE_SIZE)){
                    if(Settings.soundEnabled) Assets.click.play(1);
                    state = GameState.Running;
                    return;
                }else if(isBounds(event,GUIDE_X,GUIDE_Y+120-GUIDE_SIZE,300,GUIDE_SIZE)){
                    if(Settings.soundEnabled) Assets.click.play(1);
                    Settings.addScore(system.score);
                    Settings.save(game.getFileIO());
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            } else if(event.type == Input.TouchEvent.TOUCH_DOWN) {
            }
        }
    }

    private void updateGameOver(List<Input.TouchEvent> events,float delta){
        int len = events.size();
        for (int i=0;i<len;i++) {
            Input.TouchEvent event = events.get(i);
            if(event.type == Input.TouchEvent.TOUCH_UP) {
                if(isBounds(event,FIN_X,FIN_Y,60,60)){
                    game.setScreen(new MainMenuScreen(game));
                }
            } else if(event.type == Input.TouchEvent.TOUCH_DOWN) {
            }
        }
    }

    ///////////////////present
    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.background,0,0);
        drawWorld(g);
        switch(state) {
            case Ready: drawReadyUI(g); break;
            case Running: drawRunningUI(g); break;
            case Paused: drawPausedUI(g); break;
            case GameOver: drawGameOverUI(g); break;
        }
    }

    private void drawWorld(Graphics g) {
        Snake snake = system.snake;
        SnakePart head = snake.parts.get(0);
        Food food = system.food;
        //ボーダーライン
        g.drawLine(0,UNDERLINE_Y, AndroidGame.SHORT_EDGE,UNDERLINE_Y, Color.BLACK);
        // えさ
        g.drawPixmap(Assets.food,food.x*40,food.y*40);
        // 胴体
        int len = snake.parts.size();
        for(int i=1;i<len;i++) {
            SnakePart part = snake.parts.get(i);
            g.drawPixmap(Assets.tail,part.x*40,part.y*40);
        }
        // 頭
        Pixmap headPixmap = null;
        switch(snake.direction) {
            case Snake.UP : headPixmap = Assets.headup; break;
            case Snake.LEFT : headPixmap = Assets.headleft; break;
            case Snake.DOWN : headPixmap = Assets.headdown; break;
            case Snake.RIGHT : headPixmap = Assets.headright; break;
        }
        g.drawPixmap(headPixmap,head.x*40,head.y*40);
    }

    private void drawReadyUI(Graphics g) {
        g.drawText("READY...",GUIDE_X,GUIDE_Y,Color.BLACK,GUIDE_SIZE);
    }

    private void drawRunningUI(Graphics g) {
        g.drawPixmap(Assets.buttons,PAUSE_X,PAUSE_Y,60,120,60,60);
        g.drawPixmap(Assets.buttons,LEFT_X,LEFT_Y,60,60,60,60);
        g.drawPixmap(Assets.buttons,RIGHT_X,RIGHT_Y,0,60,60,60);
        g.drawText(score,SCORE_X,SCORE_Y,Color.BLACK,SCORE_SIZE);
    }

    private void drawPausedUI(Graphics g) {
        g.drawText("RESTART",GUIDE_X,GUIDE_Y,Color.BLUE,GUIDE_SIZE);
        g.drawText(" QUIT ",GUIDE_X,GUIDE_Y+120,Color.DKGRAY,GUIDE_SIZE);
        g.drawText(score,SCORE_X,SCORE_Y,Color.BLACK,SCORE_SIZE);
    }

    private void drawGameOverUI(Graphics g) {
        g.drawText("GAME OVER",GUIDE_X,GUIDE_Y,Color.RED,GUIDE_SIZE);
        g.drawRect(FIN_X,FIN_Y,60,60,Color.WHITE);
        g.drawPixmap(Assets.buttons,FIN_X,FIN_Y,0,120,60,60);
        g.drawText(score,SCORE_X,SCORE_Y,Color.BLACK,SCORE_SIZE);
    }

    @Override
    public void pause() {
        if(state == GameState.Running) {
            state = GameState.Paused;
        }else if(state == GameState.GameOver) {
            Settings.addScore(system.score);
            Settings.save(game.getFileIO());
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
