package com.luleo.baselibrary.listener;

import android.util.Log;

import com.squareup.otto.Bus;

/**
 * Created by leo on 2015/8/23.
 */

public abstract class BaseOttoBus extends Bus {

    private final String TAG = this.getClass().getPackage().getName();
    public void register(Object object) {
        try {
            super.register(object);
        } catch (IllegalArgumentException exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    public void unregister(Object object) {
        try {
            super.unregister(object);
        } catch (IllegalArgumentException exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

}