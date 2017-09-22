package com.example.yuki.androidgames.framework.impl;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.yuki.androidgames.framework.Audio;
import com.example.yuki.androidgames.framework.Music;
import com.example.yuki.androidgames.framework.Sound;

import java.io.IOException;

/**
 * Created by Yuki on 2017/09/12.
 */

public class AndroidAudio implements Audio {
    public static final int POOL_MAX = 20;

    AssetManager assets;
    SoundPool soundPool;

    public AndroidAudio(Activity activity){
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();

        //SoundPoolのセットアップ
        //Android 5.0 (API21) 以前
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }
        else {// Android 5.0 (API21) 以降
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(POOL_MAX)
                    .build();
        }
    }

    @Override
    public Music newMusic(String fileName) {
        try {
            AssetFileDescriptor descriptor = assets.openFd(fileName);
            return new AndroidMusic(descriptor);
        }catch (IOException e) {
            throw new RuntimeException("Could NOT load music: "+fileName);
        }
    }

    @Override
    public Sound newSound(String fileName) {
        try {
            AssetFileDescriptor descriptor = assets.openFd(fileName);
            int soundId = soundPool.load(descriptor,0);
            return new AndroidSound(soundPool,soundId);
        }catch (IOException e){
            throw new RuntimeException("Could NOT load sound: "+fileName);
        }
    }
}
