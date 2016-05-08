package com.king.applock.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.applock.R;
import com.king.applock.base.StatusBarActivity;
import com.king.applock.bean.AppInfo;
import com.king.applock.utils.AppUtil;


public class LockScreenActivity extends StatusBarActivity implements OnClickListener {
    private ImageView mIcon;

    private TextView mTvName;

    private EditText mEtPwd;

    private TextView mTvEnt;

    private String mPackName;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        initView();

        initData();
    }

    private void initView() {

        mIcon = (ImageView) findViewById(R.id.lockscreen_iv_icon);
        mTvName = (TextView) findViewById(R.id.lockscreen_tv_name);
        mEtPwd = (EditText) findViewById(R.id.lockscreen_et_pwd);
        mTvEnt = (TextView) findViewById(R.id.lockscreen_tv_ent);
        mTvEnt.setOnClickListener(this);
    }


    private void initData() {
        Intent intent = getIntent();
        mPackName = intent.getStringExtra("moshengren");

        // 根据包名获取应用信息

        // 获取图标
        AppInfo info = AppUtil.getAppInfoByPackage(this, mPackName);
        // Drawable icon = getPackageManager().getApplicationIcon(mPackName);

        mTvName.setText(info.lable);
        mIcon.setImageDrawable(info.icon);// 设置拦截app的图标

        mReceiver = new HomeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onClick(View v) {
        // 密码验证
        String pwd = mEtPwd.getText().toString().trim();
        // String guardPwd = SPUtils.getString(this, Constants.KEY_GUARD_PWD);
        if (TextUtils.isEmpty(pwd)) {
            return;
        }

        if ("123".equals(pwd)) {
            // 密码校验成功告诉服务不要再验证此应用
            Intent intent = new Intent();
            intent.putExtra("shuren", mPackName);

            intent.setAction("com.itheima.mobilesafe.verify");
            sendBroadcast(intent);
            finish();
        }
    }

    private class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                // home键处理
                finish();// 关闭自己
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
        finish();

    }
}
