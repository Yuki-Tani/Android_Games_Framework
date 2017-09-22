package com.example.yuki.androidgames.framework.impl;

import android.view.KeyEvent;
import android.view.View;

import com.example.yuki.androidgames.framework.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuki on 2017/09/13.
 */

public class KeyboardHandler implements View.OnKeyListener {
    public static final int KEYEVENT_POOL_SIZE = 100;

    boolean[] pressedKeys = new boolean[128];
    Pool<Input.KeyEvent> keyEventPool;
    List<Input.KeyEvent> keyEventsBuffer = new ArrayList<>();
    List<Input.KeyEvent> keyEvents = new ArrayList<>();

    public KeyboardHandler(View view) {
        Pool.PoolObjectFactory<Input.KeyEvent> factory =
                new Pool.PoolObjectFactory<Input.KeyEvent>() {
                    @Override
                    public Input.KeyEvent creatObject() {
                        return new Input.KeyEvent();
                    }
        };
        keyEventPool = new Pool<Input.KeyEvent>(factory,KEYEVENT_POOL_SIZE);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return false;
        synchronized (this) {
            Input.KeyEvent keyEvent = keyEventPool.newObject();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar();
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                keyEvent.type = Input.KeyEvent.KEY_DOWN;
                if (keyCode > 0 && keyCode <= 127) {
                    pressedKeys[keyCode] = true;
                }
            }
            if (event.getAction() == KeyEvent.ACTION_UP) {
                keyEvent.type = Input.KeyEvent.KEY_UP;
                if (keyCode > 0 && keyCode <= 127) {
                    pressedKeys[keyCode] = false;
                }
            }
            keyEventsBuffer.add(keyEvent);
        }
        return false;
    }

    public boolean isKeyPressed(int keyCode) {
        if(keyCode < 0 || keyCode > 127) return false;
        return pressedKeys[keyCode];
    }

    public List<Input.KeyEvent> getKeyEvents() {
        synchronized (this) {
            int len = keyEvents.size();
            // keyEventsの解放
            for (int i=0; i<len; i++){
                keyEventPool.free(keyEvents.get(i));
            }
            keyEvents.clear();
            // keyEventsBufferから読み込み
            keyEvents.addAll(keyEventsBuffer);
            keyEventsBuffer.clear();
            return keyEvents;
        }
    }
}
