package com.example.yuki.androidgames.framework.impl;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.example.yuki.androidgames.framework.Music;

import java.io.IOException;

/**
 * Created by Yuki on 2017/09/12.
 */

public class AndroidMusic implements Music, MediaPlayer.OnCompletionListener {
    MediaPlayer mediaPlayer;
    boolean isPrepared = false;

    public AndroidMusic(AssetFileDescriptor assetDescriptor) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(
                    assetDescriptor.getFileDescriptor(),
                    assetDescriptor.getStartOffset(),
                    assetDescriptor.getLength()
            );
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e){
            throw new RuntimeException("Cound NOT load music");
        }
    }

    @Override
    public void setLooping(boolean looping) {
        mediaPlayer.setLooping(isLooping());
    }

    @Override
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume,volume);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public boolean isStopped() {
        return !isPrepared;
    }

    @Override
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    @Override
    public void dispose() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    @Override
    public void play() {
        if(mediaPlayer.isPlaying()) return;
        try {
            synchronized (this) {
                if(!isPrepared){
                    mediaPlayer.prepare();
                    isPrepared = true;
                }
                mediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
        synchronized (this) {
            isPrepared = false;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        synchronized (this) {
            isPrepared = false; // 必要？
        }
    }
}
