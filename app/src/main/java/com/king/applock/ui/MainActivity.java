package com.king.applock.ui;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.applock.R;
import com.king.applock.adapter.AppLockReAdapter;
import com.king.applock.base.AppController;
import com.king.applock.bean.AppInfo;
import com.king.applock.dao.AppLockDao;
import com.king.applock.service.WatchDogService;
import com.king.applock.utils.AppUtil;
import com.king.applock.utils.RefreshLayoutUtils;
import com.king.applock.utils.ServiceUtil;
import com.king.applock.utils.ThreadTask;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, AppLockReAdapter.OnDataChangeListener, TabLayout.OnTabSelectedListener {


    @Bind(R.id.main_nav_tv_running)
    TextView mMainNavTvRunning;

    @Bind(R.id.main_nav_btn_theme_dark)
    ImageView mMainNavBtnThemeDark;
    @Bind(R.id.rl_bg)
    RelativeLayout mRlBg;
    private Toolbar mToolbar;
    private TabLayout mTab;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private FloatingActionButton mFab;
    private DrawerLayout mDrawerLayout;

    private String[] mTitles;

    // 加锁和未加锁
    private List<AppInfo> mUnLockInfos = new ArrayList<>();

    private List<AppInfo> mLockedInfos = new ArrayList<>();

    private TabLayout.Tab mTab1, mTab2;
    private AppLockDao mDao;
    private AppLockReAdapter mAdapter;
    //临时解决
    private boolean mIsRefreshing = false;

    // 是否启用夜间模式
    private boolean enableThemeDark;

    private static final int REQUEST_CODE_ENABLE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enableThemeDark = ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight_FitsStatusBar, R.style.AppThemeDark_FitsStatusBar);
        setContentView(R.layout.activity_main);
        AppController.setMainContext(this);
        ButterKnife.bind(this);
        statusBar();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTab = (TabLayout) findViewById(R.id.tab_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_refresh_layout);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.red_light));
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        mTab1 = mTab.newTab();
        mTab2 = mTab.newTab();
        mTitles = getResources().getStringArray(R.array.tab);
        mTab.addTab(mTab1.setText(mTitles[0]));
        mTab.addTab(mTab2.setText(mTitles[1]));
        mTab.setOnTabSelectedListener(this);

        initFab();

        if (mDao == null)
            mDao = new AppLockDao(this);
        if (mAdapter == null)
            mAdapter = new AppLockReAdapter(this, mUnLockInfos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter.setOnDataChangeListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mIsRefreshing) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );
        //当刷新时设置
        //mIsRefreshing=true;
        //刷新完毕后还原为false
        //mIsRefreshing=false;
        RefreshLayoutUtils.initOnCreate(mRefreshLayout, this);
        RefreshLayoutUtils.refreshOnCreate(mRefreshLayout, this);
    }

    private void statusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);//通知栏所需颜色
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean runing = ServiceUtil.isServiceRuning(MainActivity.this, WatchDogService.class);
        mFab.setImageResource(runing ? R.mipmap.lock_open : R.mipmap.lock_close);
        mMainNavTvRunning.setText(runing ? "加锁服务运行中" : "加锁服务关闭");
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        if (time > 6 && time < 19) {
            mMainNavBtnThemeDark.setImageResource(R.mipmap.ic_wb_sunny_white_24dp);
        } else {
            mMainNavBtnThemeDark.setImageResource(R.mipmap.ic_brightness_3_white_24dp);
            Random random = new Random();
            switch (random.nextInt(3)) {
                case 0:
                    mRlBg.setBackgroundResource(R.mipmap.bg_1);
                    break;
                case 1:
                    mRlBg.setBackgroundResource(R.mipmap.bg_2);
                    break;
                case 2:
                    mRlBg.setBackgroundResource(R.mipmap.bg_3);
                    break;
            }
        }
    }

    private void initFab() {

        mFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
                startActivity(intent);
                return false;
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ServiceUtil.isServiceRuning(MainActivity.this, WatchDogService.class)) {
                    Snackbar.make(view, "长按关闭服务,关闭程序锁", Snackbar.LENGTH_SHORT)
                            .setAction(R.string.agree, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
                                    startActivity(intent);

                                }
                            }).show();

                } else {

                    Snackbar.make(view, "长按启动服务,开启程序锁", Snackbar.LENGTH_LONG)
                            .setAction(R.string.agree, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
                                    startActivity(intent);

                                }
                            }).show();

                }
            }
        });

    }


    @OnClick({
            R.id.main_nav_btn_theme,
            R.id.main_nav_btn_setting,
            R.id.main_nav_btn_about
    })
    public void onNavigationItemOtherClick(View itemView) {
        switch (itemView.getId()) {
            case R.id.main_nav_btn_theme:
                Toast.makeText(MainActivity.this, "功能开发测试中,尽快加入此功能", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_nav_btn_setting:
                AppController.getMainThreadHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                    }
                }, 400);
                break;
            case R.id.main_nav_btn_about:
                AppController.getMainThreadHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    }
                }, 400);
                break;
        }
        mDrawerLayout.closeDrawers();
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                AppController.getMainThreadHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                    }
                }, 400);
                break;
            case R.id.action_about:
                AppController.getMainThreadHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                    }
                }, 400);
                break;
            case R.id.action_exit:
                //v7包
                AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppDialogLight)
                        .setMessage(R.string.agree_exit)
                        .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton(R.string.disagree, null)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 首次按下返回键时间戳
    private long firstBackPressedTime = 0;

    /**
     * 返回键关闭导航
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            long secondBackPressedTime = System.currentTimeMillis();
            if (secondBackPressedTime - firstBackPressedTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                firstBackPressedTime = secondBackPressedTime;
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onRefresh() {
        //获取数据
        mIsRefreshing = true;
        ThreadTask.getInstance().executorDBThread(new Runnable() {
            @Override
            public void run() {
                mLockedInfos.clear();
                mUnLockInfos.clear();

                List<String> packages = mDao.query();
                // 获取所有app的集合
                List<AppInfo> allApps = AppUtil.getAllApp(MainActivity.this);

                for (String packageName : packages) {
                    // 循环添加锁的app加对象
                    AppInfo info = AppUtil.getAppInfoByPackage(MainActivity.this, packageName);

                    mLockedInfos.add(info);
                }


                for (AppInfo appInfo : allApps) {

                    if (!mDao.isAppLock(appInfo.packageName)) {
                        // 添加到未加锁集合
                        mUnLockInfos.add(appInfo);
                    }
                }
                AppController.getMainThreadHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        //标签数据
                        String unLockNum = "(" + mUnLockInfos.size() + ")";
                        mTab1.setText("未加锁应用" + (mUnLockInfos.size() == 0 ? "" : unLockNum));

                        String lockNum = "(" + mLockedInfos.size() + ")";
                        mTab2.setText("已加锁应用" + (mLockedInfos.size() == 0 ? "" : lockNum));
                        mRefreshLayout.setRefreshing(false);
                        //BUG
                        mIsRefreshing = false;
                    }
                });
            }
        }, ThreadTask.ThreadPeriod.PERIOD_HIGHT);
    }

    @Override
    public void onDataChange(boolean isLock, AppInfo info) {
        if (isLock) {
            mUnLockInfos.add(info);
        } else {
            mLockedInfos.add(info);
        }
        //标签数据
        String unLockNum = "(" + mUnLockInfos.size() + ")";
        mTab1.setText("未加锁应用" + (mUnLockInfos.size() == 0 ? "" : unLockNum));

        String lockNum = "(" + mLockedInfos.size() + ")";
        mTab2.setText("已加锁应用" + (mLockedInfos.size() == 0 ? "" : lockNum));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.equals(mTab1)) {
            //未加锁
            mAdapter.showData(mUnLockInfos, false);
            mAdapter.notifyDataSetChanged();
        } else {
            //加锁
            mAdapter.showData(mLockedInfos, true);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadTask.getInstance().shutDownAll();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ENABLE:
                //成功进入界面

                break;
        }
    }
}
