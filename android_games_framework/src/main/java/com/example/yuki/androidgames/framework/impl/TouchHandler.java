package com.example.yuki.androidgames.framework.impl;

import android.view.MotionEvent;
import android.view.View;

import com.example.yuki.androidgames.framework.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuki on 2017/09/13.
 */

public class TouchHandler implements View.OnTouchListener {
    public static final int POINTER_NUM = 20;
    public static final int TOUCHEVENT_POOL_SIZE = 100;

    boolean[] isTouched = new boolean[POINTER_NUM];
    int[] touchX = new int[POINTER_NUM];
    int[] touchY = new int[POINTER_NUM];
    Pool<Input.TouchEvent> touchEventPool;
    List<Input.TouchEvent> touchEvents = new ArrayList<>();
    List<Input.TouchEvent> touchEventsBuffer = new ArrayList<>();
    float scaleX;
    float scaleY;

    public TouchHandler(View view, float scaleX, float scaleY) {
        Pool.PoolObjectFactory<Input.TouchEvent> factory =
                new Pool.PoolObjectFactory<Input.TouchEvent>() {
                    @Override
                    public Input.TouchEvent creatObject() {
                        return new Input.TouchEvent();
                    }
                };
        touchEventPool = new Pool<Input.TouchEvent>(factory,TOUCHEVENT_POOL_SIZE);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            int action = event.getActionMasked();
            int pointerIndex = event.getActionIndex();
            int pointerId = event.getPointerId(pointerIndex);
            Input.TouchEvent touchEvent;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = Input.TouchEvent.TOUCH_DOWN;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] =
                            (int) (event.getX(pointerIndex) * scaleX);
                    touchEvent.y = touchY[pointerId] =
                            (int) (event.getY(pointerIndex) * scaleY);
                    isTouched[pointerId] = true;
                    touchEventsBuffer.add(touchEvent);
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = Input.TouchEvent.TOUCH_UP;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] =
                            (int) (event.getX(pointerIndex) * scaleX);
                    touchEvent.y = touchY[pointerId] =
                            (int) (event.getY(pointerIndex) * scaleY);
                    isTouched[pointerId] = false;
                    touchEventsBuffer.add(touchEvent);
                    break;

                case MotionEvent.ACTION_MOVE:
                    int pointerCount = event.getPointerCount();
                    for (int i=0;i<pointerCount;i++) {
                        pointerIndex = i;
                        pointerId = event.getPointerId(pointerIndex);
                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = Input.TouchEvent.TOUCH_DRAGGED;
                        touchEvent.pointer = pointerId;
                        touchEvent.x = touchX[pointerId] =
                                (int) (event.getX(pointerIndex) * scaleX);
                        touchEvent.y = touchY[pointerId] =
                                (int) (event.getY(pointerIndex) * scaleY);
                        touchEventsBuffer.add(touchEvent);
                    }
                    break;
            }
            return true;
        }
    }

    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= POINTER_NUM) {
                return false;
            } else {
                return isTouched[pointer];
            }
        }
    }

    public int getToucheX(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= POINTER_NUM) {
                return 0;
            } else {
                return touchX[pointer];
            }
        }
    }


    public int getToucheY(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= POINTER_NUM) {
                return 0;
            } else {
                return touchY[pointer];
            }
        }
    }

    public List<Input.TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            for (int i=0; i<len; i++) {
                touchEventPool.free(touchEvents.get(i));
            }
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }
}
