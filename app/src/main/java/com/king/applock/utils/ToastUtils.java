package com.king.applock.utils;

import android.content.Context;
import android.widget.Toast;

public final class ToastUtils {

    private volatile static ToastUtils singleton;

    public static ToastUtils with(Context context) {
        if (singleton == null) {
            synchronized (ToastUtils.class) {
                if (singleton == null) {
                    singleton = new ToastUtils(context);
                }
            }
        }
        return singleton;
    }

    private final Toast toast;

    private ToastUtils(Context context) {
        toast = Toast.makeText(context.getApplicationContext(), null, Toast.LENGTH_SHORT);
    }

    public void show(CharSequence msg) {
        toast.setText(msg);
        toast.show();
    }

    public void show(int resId) {
        toast.setText(resId);
        toast.show();
    }

}
