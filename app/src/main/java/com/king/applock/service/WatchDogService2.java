package com.king.applock.service;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import com.king.applock.dao.AppLockDao;
import com.king.applock.ui.LockScreenActivity;

import java.util.ArrayList;
import java.util.List;


public class WatchDogService2 extends AccessibilityService {

    private VerifyReceiver mVerifyReceiver;

    private ScreenStatusReceiver mScreenReceiver;

    // 已经验证过的程序集合
    static List<String> mVerifyList = new ArrayList<>();

    boolean isRun = false;

    private List<String> mAllLockData;

    private AppLockDao mDao;

    @Override
    public void onCreate() {
        super.onCreate();

        mDao = new AppLockDao(this);

        mAllLockData = mDao.query();

        registerVerify();

        registerScreenStatus();

        registerObserver();

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

            String packageName = event.getPackageName() + "";

            if (mAllLockData.contains(packageName)) {

                if (!mVerifyList.contains(packageName)) {
                    startLockActivity(packageName);
                }
            }
            // SystemClock.sleep(1000);
        }
    }

    private void startLockActivity(String packageName) {
        Intent intent = new Intent(this, LockScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // 把包名传给下一个界面，让它去负责显示这个程序的名称以及icon
        intent.putExtra("moshengren", packageName);
        startActivity(intent);
    }

    private void registerObserver() {
        Uri uri = Uri.parse("dm://com.jrking.change");
        getContentResolver().registerContentObserver(uri, true, new DataChangeObserver(new Handler()));
    }

    private void registerScreenStatus() {
        mScreenReceiver = new ScreenStatusReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenReceiver, filter);
    }

    private void registerVerify() {
        mVerifyReceiver = new VerifyReceiver();
        IntentFilter filter = new IntentFilter("com.itheima.mobilesafe.verify");
        registerReceiver(mVerifyReceiver, filter);
    }

    class ScreenStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mVerifyList.clear();
        }
    }

    class VerifyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //做标记的方式可以实现控制器
            String mShuren = intent.getStringExtra("shuren");

            mVerifyList.add(mShuren);
        }
    }

    class DataChangeObserver extends ContentObserver {

        public DataChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            // 重新查一次数据库，因为数据库里面数据已经发生了变化..
            mAllLockData = mDao.query();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mVerifyReceiver);
        unregisterReceiver(mScreenReceiver);

    }

    @Override
    public void onInterrupt() {

    }

}
