package com.king.applock.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.king.applock.ui.LockScreenActivity;

import java.util.HashMap;
import java.util.Map;

public class AppController extends Application {

    private static Context mContext;
    private static Context mMainContext;
    private static boolean isRun;

    private static AppController INSTANCE;
    private static int mMainThreadId;
    private static Handler mMainThreadHandler;

    private Map<String, String> mProtocolMap = new HashMap<>();


    public Map<String, String> getProtocolMap() {
        return mProtocolMap;
    }

    /**
     * 得到上下文
     */
    public static Context getContext() {
        return mContext;
    }

    public static Context getMainContext() {
        return mMainContext;
    }

    public static AppController getInstance() {
        return INSTANCE;
    }

    public static void setMainContext(Context context) {
        mMainContext = context;
    }

    public static boolean getIsRun() {
        return isRun;
    }

    /**
     * 得到主线程id
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 得到主线程hanlder
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    @Override
    public void onCreate() {//程序的入口方法
        super.onCreate();

        INSTANCE = this;
        //初始化一些常见的属性放到MyApplication里面来

        //上下文
        mContext = getApplicationContext();

        //主线程的Id
        mMainThreadId = android.os.Process.myTid();
        /*
         myTid(); Thread
         myPid(); Process
         myUid();  User
         */

        //程序启动
        isRun = true;
        //主线程的Handler
        mMainThreadHandler = new Handler();

        LockManager<LockScreenActivity> mLockManager = LockManager.getInstance();
        mLockManager.enableAppLock(getApplicationContext(), LockScreenActivity.class);
        mLockManager.getAppLock().setShouldShowForgot(false);

    }

}
