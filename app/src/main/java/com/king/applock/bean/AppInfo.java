package com.king.applock.bean;

import android.graphics.drawable.Drawable;

public class AppInfo implements Comparable<AppInfo> {

    public int pid;

    public String packageName;

    public String lable;

    public Drawable icon;

    public long size;

    public boolean isSystem;

    public boolean isStorage;


    public AppInfo(String packageName, String lable, Drawable icon, long size, boolean isSystem, boolean isStorage) {
        super();
        this.packageName = packageName;
        this.lable = lable;
        this.icon = icon;
        this.size = size;
        this.isSystem = isSystem;
        this.isStorage = isStorage;
    }


    /**
     * Creates a new instance of AppInfo.
     * Description
     *
     * @param pid
     * @param packageName
     * @param lable
     * @param icon
     * @param size
     * @param isSystem
     * @param isStorage
     */

    public AppInfo(int pid, String packageName, String lable, Drawable icon, long size, boolean isSystem,
                   boolean isStorage) {
        super();
        this.pid = pid;
        this.packageName = packageName;
        this.lable = lable;
        this.icon = icon;
        this.size = size;
        this.isSystem = isSystem;
        this.isStorage = isStorage;
    }


    @Override
    public int compareTo(AppInfo another) {
        if (another == null) {
            return 1;
        } else {
            return this.lable.compareTo(another.lable);
        }
    }
}
