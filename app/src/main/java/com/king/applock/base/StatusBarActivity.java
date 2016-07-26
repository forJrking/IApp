package com.king.applock.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.king.applock.utils.StatusBarCompat;


public abstract class StatusBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#0288D1"));
        StatusBarCompat.translucentStatusBar(this);
    }

}
