package com.king.applock.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.king.applock.R;
import com.king.applock.base.AppController;
import com.king.applock.base.StatusBarActivity;
import com.king.applock.listener.NavigationFinishClickListener;
import com.king.applock.utils.SPUtils;
import com.king.applock.utils.ThemeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * @创建者     Administrator
 * @创建时间   2016/4/19 15:32
 * @描述	      ${TODO}
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class SettingActivity extends StatusBarActivity {

    @Bind(R.id.setting_toolbar)
    Toolbar mSettingToolbar;
    @Bind(R.id.setting_switch_lockSmart)
    SwitchCompat mSettingSwitchLockSmart;
    @Bind(R.id.setting_switch_systemApp)
    SwitchCompat mSettingSwitchSystemApp;
    @Bind(R.id.setting_switch_theme_dark)
    SwitchCompat mSettingSwitchThemeDark;
    @Bind(R.id.setting_btn_modify_topic_sign)
    TextView mPwdtop;
    @Bind(R.id.setting_btn_lockSmart)
    RelativeLayout mSettingBtnLockSmart;
    @Bind(R.id.setting_btn_systemApp)
    RelativeLayout mSettingBtnSystemApp;
    @Bind(R.id.setting_btn_theme_dark)
    RelativeLayout mSettingBtnThemeDark;
    @Bind(R.id.setting_btn_rePwd)
    RelativeLayout mSettingBtnRePwd;
    @Bind(R.id.setting_btn_new_pwd)
    RelativeLayout mSettingBtnNewPwd;
    @Bind(R.id.setting_switch_ad)
    SwitchCompat mSettingSwitchAd;
    @Bind(R.id.setting_btn_ad)
    RelativeLayout mSettingBtnAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        mSettingToolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        mSettingSwitchLockSmart.setChecked(SPUtils.getBoolean(this, SPUtils.LOCKSMART, true));
        mSettingSwitchSystemApp.setChecked(SPUtils.getBoolean(this, SPUtils.SYSTEMAPP));
        mSettingSwitchThemeDark.setChecked(SPUtils.getBoolean(this, SPUtils.THEMEDARK));
    }


    @OnClick({R.id.setting_btn_lockSmart, R.id.setting_btn_systemApp, R.id.setting_btn_theme_dark, R.id.setting_btn_rePwd, R.id.setting_btn_new_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_btn_lockSmart:
                mSettingSwitchLockSmart.toggle();
                SPUtils.putBoolean(this, SPUtils.LOCKSMART, mSettingSwitchLockSmart.isChecked());
                break;
            case R.id.setting_btn_systemApp:
                mSettingSwitchSystemApp.toggle();
                SPUtils.putBoolean(this, SPUtils.SYSTEMAPP, mSettingSwitchSystemApp.isChecked());
                ThemeUtils.recreateActivity((MainActivity) AppController.getMainContext());
                break;
            case R.id.setting_btn_theme_dark:
                mSettingSwitchThemeDark.toggle();
                SPUtils.putBoolean(this, SPUtils.SYSTEMAPP, mSettingSwitchThemeDark.isChecked());
                break;
            case R.id.setting_btn_rePwd:
                //重置密码
                Intent intent = new Intent(this, LockScreenActivity.class);

                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
                startActivity(intent);
                break;
            case R.id.setting_btn_new_pwd:
                //密码样式选择
                new MaterialDialog.Builder(this)
                        .title(R.string.lockStyle)
                        .items(R.array.stytleItems)
                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mPwdtop.setText(text);
                                Toast.makeText(SettingActivity.this, "下版本加入此功能", Toast.LENGTH_SHORT).show();
                                return true; // allow selection
                            }
                        })
                        .positiveText(R.string.chose)
                        .show();
                break;
        }
    }

    @OnClick(R.id.setting_btn_ad)
    public void onClick() {

    }
}
