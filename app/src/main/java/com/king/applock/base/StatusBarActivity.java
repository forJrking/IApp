package com.king.applock.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.king.applock.R;
import com.king.applock.utils.ThemeUtils;


public abstract class StatusBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            FrameLayout rootView = new FrameLayout(this);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            View contentView = LayoutInflater.from(this).inflate(layoutResID, rootView, false);
            contentView.setFitsSystemWindows(true);
            rootView.addView(contentView);

            View statusBarView = new View(this);
            statusBarView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ThemeUtils.getStatusBarHeight(this)));
            statusBarView.setBackgroundColor(ThemeUtils.getThemeAttrColor(this, R.attr.colorPrimaryDark));
            rootView.addView(statusBarView);

            super.setContentView(rootView);
        } else {
            super.setContentView(layoutResID);
        }
    }

}
