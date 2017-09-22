package com.example.yuki.androidgames.framework_creating;

import com.example.yuki.androidgames.framework.Audio;
import com.example.yuki.androidgames.framework.Game;
import com.example.yuki.androidgames.framework.Graphics;
import com.example.yuki.androidgames.framework.Screen;

/**
 * Created by Yuki on 2017/09/14.
 */

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        Assets.logo = g.newPixmap("logo.png", Graphics.PixmapFormat.ARGB4444);
        Assets.mainMenu = g.newPixmap("mainmenu.png", Graphics.PixmapFormat.ARGB4444);
        Assets.buttons = g.newPixmap("buttons.png", Graphics.PixmapFormat.ARGB4444);
        Assets.headup = g.newPixmap("headup.png", Graphics.PixmapFormat.ARGB4444);
        Assets.headright = g.newPixmap("headright.png", Graphics.PixmapFormat.ARGB4444);
        Assets.headdown = g.newPixmap("headdown.png", Graphics.PixmapFormat.ARGB4444);
        Assets.headleft = g.newPixmap("headleft.png", Graphics.PixmapFormat.ARGB4444);
        Assets.tail = g.newPixmap("tail.png", Graphics.PixmapFormat.ARGB4444);
        Assets.food = g.newPixmap("food.png", Graphics.PixmapFormat.ARGB4444);
        Settings.load(game.getFileIO());
        game.setScreen(new MainMenuScreen(game));

        Audio audio = game.getAudio();
        Assets.click = audio.newSound("click.ogg");
        Assets.eat = audio.newSound("eat.ogg");
        Assets.miss = audio.newSound("miss.ogg");
    }

    @Override
    public void present(float deltaTime) {

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
