package com.example.yuki.androidgames.framework_creating;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuki on 2017/09/16.
 */

public class Snake {
    public static final int UP = 0,
                            LEFT = 1,
                            DOWN = 2,
                            RIGHT = 3,
                            STRAIGHT = 2;

    public List<SnakePart> parts = new ArrayList<>();
    public int direction;
    public int change;

    public Snake() {this(false);};
    public Snake(boolean special) {
        direction = UP;
        parts.add(new SnakePart(4,6));
        parts.add(new SnakePart(4,7));
        parts.add(new SnakePart(4,8));

        if(special) { // +30
            for(int i=9;i<=13;i++) {
                parts.add(new SnakePart(4,i));//5
            }
            parts.add(new SnakePart(3,13));//1
            for(int i=13;i>=0;i--) {
                parts.add(new SnakePart(2,i));//14
            }
            parts.add(new SnakePart(1,0));//1
            for (int i=0;i<=8;i++) {
                parts.add(new SnakePart(0,i));//9
            }
        }
    }

    public void turnLeft() {
        direction = (direction + 1)%4;
    }

    public void turnRight() {
        direction = (direction + 3)%4;
    }

    public void eat() {
        SnakePart end = parts.get(parts.size()-1);
        parts.add(new SnakePart(end.x,end.y));
    }

    public void advance() {
        SnakePart head = parts.get(0);
        int len = parts.size() - 1;
        for (int i=len;i>0;i--) {
            SnakePart before = parts.get(i-1);
            SnakePart part = parts.get(i);
            part.x = before.x;
            part.y = before.y;
        }

        switch (direction) {
            case UP: head.y -= 1; break;
            case LEFT: head.x -= 1; break;
            case DOWN: head.y += 1; break;
            case RIGHT: head.x += 1; break;
        }
        if(head.x < 0) head.x = GameSystem.WORLD_WIDTH-1;
        if(head.x > GameSystem.WORLD_WIDTH-1) head.x = 0;
        if(head.y < 0) head.y = GameSystem.WORLD_HEIGHT-1;
        if(head.y > GameSystem.WORLD_HEIGHT-1) head.y = 0;
    }

    public boolean checkDeath() {
        int len = parts.size();
        SnakePart head = parts.get(0);
        for (int i=1;i<len;i++) {
            SnakePart part = parts.get(i);
            if(part.x == head.x && part.y == head.y) {
                return true;
            }
        }
        return false;
    }
}
