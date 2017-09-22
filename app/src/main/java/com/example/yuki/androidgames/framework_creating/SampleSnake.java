package com.example.yuki.androidgames.framework_creating;

import com.example.yuki.androidgames.framework.Screen;
import com.example.yuki.androidgames.framework.impl.AndroidGame;

/**
 * Created by Yuki on 2017/09/14.
 */

public class SampleSnake extends AndroidGame {
    @Override
    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }
}
