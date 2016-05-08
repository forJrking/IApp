package com.king.applock.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.applock.R;
import com.king.applock.base.StatusBarActivity;
import com.king.applock.listener.NavigationFinishClickListener;
import com.king.applock.utils.ShipUtils;
import com.king.applock.utils.ThemeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AboutActivity extends StatusBarActivity {

    @Bind(R.id.about_tv_version)
    TextView mAboutTvVersion;
    @Bind(R.id.about_btn_about_power)
    LinearLayout mAboutBtnAboutPower;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight_FitsStatusBar, R.style.AppThemeDark_FitsStatusBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        mAboutTvVersion.setText("1.5正式版");
    }


    @OnClick(R.id.about_btn_open_source_url)
    protected void onBtnAboutAuthorClick() {
        // ShipUtils.openInBrowser(this, );

    }

    @OnClick(R.id.about_btn_open_in_app_store)
    protected void onBtnOpenInAppStoreClick() {
        ShipUtils.openInAppStore(AboutActivity.this);
    }

    @OnClick(R.id.about_btn_advice_feedback)
    protected void onBtnAdviceFeedbackClick() {
        ShipUtils.sendEmail(
                this,
                "forjrking@sina.com",
                "来自 AppLock-" + " 的客户端反馈",
                "设备信息：Android " + Build.VERSION.RELEASE + " - " + Build.MANUFACTURER + " - " + Build.MODEL + "\n（如果涉及隐私请手动删除这个内容）\n\n");
    }

    @OnClick(R.id.about_btn_about_power)
    public void onClick() {
        ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText("jrking.love@qq.com");
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppDialogLight).setTitle("捐赠开发者")
                .setMessage("支付宝帐号jrking.love@qq.com已复制到剪贴板,感谢您的支持!")
                .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.disagree, null)
                .show();
    }
}
