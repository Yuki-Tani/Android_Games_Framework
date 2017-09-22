package com.example.yuki.androidgames.framework_creating;

import java.util.Random;

/**
 * Created by Yuki on 2017/09/16.
 */

public class GameSystem {
    public static final int WORLD_WIDTH = 9,
                     WORLD_HEIGHT = 14,
                     SCORE_INCREMENT = 10,
                     CLEAR_BOUNUS = 270,
                     LEVEL_UP_SCORE = 100;

    public static final float TICK_INITIAL = 0.6f,
                       TICK_DECREMENT = 0.05f;

    public Snake snake;
    public Food food;
    public boolean gameOver = false;
    public int score = 0;

    boolean[][] field = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
    Random random = new Random();
    float tickTime = 0;
    float tick = TICK_INITIAL;

    public GameSystem() {this(false);}
    public GameSystem(boolean special) {
        snake = new Snake(special);
        placeFood();

        if(special) {
            tick -= TICK_DECREMENT*3;
            score = 300;
        }
    }

    private void placeFood() {
        for (int i=0;i<WORLD_WIDTH;i++){
            for (int j=0;j<WORLD_HEIGHT;j++){
                field[i][j] = false;
            }
        }

        int len = snake.parts.size();
        for (int i=0;i<len;i++) {
            SnakePart part = snake.parts.get(i);
            field[part.x][part.y] = true;
        }

        int place = random.nextInt(WORLD_WIDTH * WORLD_HEIGHT);
        int inc = (random.nextInt(2)==0)?-1:+1;

        while(true) {
            if(!field[place%WORLD_WIDTH][place/WORLD_WIDTH]) break;
            place += inc;
        }
        food = new Food(place%WORLD_WIDTH,place/WORLD_WIDTH);
    }

    public void update(float delta) {
        if(gameOver) return;
        tickTime += delta;

        // tickTimeがtickを越える度に実行
        while(tickTime > tick) {
            tickTime -= tick;
            // 移動
            snake.change = Snake.STRAIGHT;
            snake.advance();
            // 死亡判定
            if(snake.checkDeath()) {
                gameOver = true;
                return;
            }

            // 食事判定
            SnakePart head = snake.parts.get(0);
            if(head.x == food.x && head.y == food.y) {
                score += SCORE_INCREMENT;
                snake.eat();
                // クリア判定
                if(snake.parts.size() == WORLD_WIDTH * WORLD_HEIGHT) {
                    score += CLEAR_BOUNUS;
                    gameOver = true;
                    return;
                } else { // 新えさの配置
                    placeFood();
                }
                // レベルアップ
                if(score % LEVEL_UP_SCORE == 0 && tick > TICK_DECREMENT) {
                    tick -= TICK_DECREMENT;
                }
            }
        }
    }
}
