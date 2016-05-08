package com.king.applock.utils;

import android.content.Context;

public final class SettingShared {

    private SettingShared() {}

    private static final String TAG = "SettingShared";

    private static final String KEY_ENABLE_NOTIFICATION = "notification";
    private static final String KEY_ENABLE_THEME_DARK = "theme_dark";
    private static final String KEY_NEW_TOPIC_DRAFT = "new_topic_draft";
    private static final String KEY_ENABLE_TOPIC_SIGN = "topic_sign";
    private static final String KEY_TOPIC_SIGN_CONTENT = "topic_sign_content";

    public static final String DEFAULT_TOPIC_SIGN_CONTENT = "来自酷炫的 [CNodeMD](https://github.com/TakWolf/CNode-Material-Design)";

    public static boolean isEnableThemeDark(Context context) {
        return false;
    }
}
