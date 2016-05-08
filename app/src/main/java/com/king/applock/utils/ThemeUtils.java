package com.king.applock.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

public final class ThemeUtils {

    private ThemeUtils() {}

    @ColorInt
    public static int getThemeAttrColor(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }

    public static Drawable getThemeAttrDrawable(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            return a.getDrawable(0);
        } finally {
            a.recycle();
        }
    }

    public static boolean configThemeBeforeOnCreate(Activity activity, @StyleRes int light, @StyleRes int dark) {
        boolean enable = SettingShared.isEnableThemeDark(activity);
        activity.setTheme(enable ? dark : light);
        return enable;
    }

    public static void recreateActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            activity.recreate();
        } else {
            Intent intent = activity.getIntent();
            intent.setClass(activity, activity.getClass());
            activity.startActivity(intent);
            activity.finish();
            activity.overridePendingTransition(0, 0);
        }
    }


    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? context.getResources().getDimensionPixelSize(resourceId) : 0;
    }

}
