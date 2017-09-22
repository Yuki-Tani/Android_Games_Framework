package com.example.yuki.androidgames.framework.impl;

import android.media.SoundPool;

import com.example.yuki.androidgames.framework.Sound;

/**
 * Created by Yuki on 2017/09/12.
 */

public class AndroidSound implements Sound {
    int soundId;
    SoundPool soundPool;

    public AndroidSound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }


    @Override
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }
}
