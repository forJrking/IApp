package com.king.applock.utils;

import android.content.Context;
import android.content.res.Resources;

import com.king.applock.base.AppController;


/**
 * 创建时间   2016/2/24 11:19
 * 描述	      封装和ui相关的工具方法
 */
public class UIUtils {
    /**
     * 得到上下文
     */
    public static Context getContext() {
        return AppController.getContext();
    }

    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到string.xml中的字符
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 得到string.xml中的字符数组
     */
    public static String[] getStrings(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到color.xml中的颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 得到应用程序的包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * dp-->px
     */
    public static int dp2px(int dp) {
        float density = getResources().getDisplayMetrics().density;

        return (int) (dp * density + .5f);
    }

    /**
     * dx-->dp
     */
    public static int px2dp(int px) {
        float density = getResources().getDisplayMetrics().density;

        return (int) (px / density + .5f);
    }
}
