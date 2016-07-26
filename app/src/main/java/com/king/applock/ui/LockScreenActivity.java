package com.king.applock.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.AppLockActivity;
import com.king.applock.R;
import com.king.applock.bean.AppInfo;
import com.king.applock.utils.AppUtil;
import com.king.applock.utils.SPUtils;
import com.king.applock.utils.StatusBarCompat;


public class LockScreenActivity extends AppLockActivity {

    private String mPackName;
    private BroadcastReceiver mReceiver;
    private int mIntExtra;
    private boolean isfirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SPUtils.getBoolean(this, SPUtils.ISFIRST, true)) {
            isfirst = true;
            setType(AppLock.ENABLE_PINLOCK);
        }
        statusBar();
        super.onCreate(savedInstanceState);

        initData();
    }

    private void statusBar() {
        StatusBarCompat.setStatusBarColor(this,  Color.parseColor("#0288D1"));
        StatusBarCompat.translucentStatusBar(this);
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        mPackName = intent.getStringExtra("toLock");
        mIntExtra = intent.getIntExtra(AppLock.EXTRA_TYPE, AppLock.DISABLE_PINLOCK);

//        System.out.println("包名" + mPackName + "mIntExtra" + mIntExtra);
        // 获取图标
        if (mIntExtra == AppLock.UNLOCK_PIN) {
            //HOME的处理
            mReceiver = new HomeReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            registerReceiver(mReceiver, filter);

            if (mPackName == null) {
                return;
            }

            AppInfo info = AppUtil.getAppInfoByPackage(LockScreenActivity.this, mPackName);
            if (info != null) {
                setDrawable(info.icon);
                mToolbar.setText(info.lable);
            }
        } else {
            mToolbar.setText(R.string.app_name);
            setDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        }
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null)
            unregisterReceiver(mReceiver);

        super.onDestroy();
    }


    private class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                // home键处理
                homeFinish(true);// 关闭自己
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mIntExtra == AppLock.CHANGE_PIN) {
            homeFinish(false);
            return;
        }
        homeFinish(true);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }

    @Override
    public void showForgotDialog() {

        new MaterialDialog.Builder(this)
                .title(R.string.forgot)
                .content(R.string.forgotDetail)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        if (which.name().equals("确定")) {

                        }
                    }
                })
                .show();

    }

    @Override
    public void onPinFailure(int attempts) {
        //错误次数
        if (attempts == 5) {
            showForgotDialog();
        }
    }

    @Override
    public void onPinSuccess(int attempts) {
        if (isfirst) {
            SPUtils.putBoolean(this, SPUtils.ISFIRST, false);
            startActivity(new Intent(this, MainActivity.class));
        } else if (mPackName == null && mIntExtra == 1) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Intent intent = new Intent();
            intent.putExtra("unLock", mPackName);
            intent.setAction("com.king.verify");
            sendBroadcast(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}


