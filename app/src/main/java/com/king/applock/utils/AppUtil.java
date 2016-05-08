package com.king.applock.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.king.applock.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppUtil {

    public static List<AppInfo> getAllapps(Context context) {

        List<AppInfo> infos = new ArrayList<AppInfo>();

        PackageManager pm = context.getPackageManager();
        // pm.getApplicationLogo(info);
        List<ApplicationInfo> applications = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);

        for (ApplicationInfo info : applications) {
            // 应用名字
            String lable = (String) info.loadLabel(pm);
            // 应用图标
            Drawable icon = info.loadIcon(pm);
            // 应用大小
            String dir = info.sourceDir;
            File file = new File(dir);
            // 文件大小
            long size = file.length();
            String packageName = info.packageName;

            // 判断系统程序与否
            boolean isSystem = false;
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                // !=0 也表示 是系统程序
                isSystem = true;
            }

            boolean isStorage = false;
            // 判断是否在sd中
            if ((info.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                isStorage = true;
            }

            // 进程id
            int uid = info.uid;
            if (!isSystem && !packageName.equals("com.jrking.lock"))
                infos.add(new AppInfo(uid, packageName, lable, icon, size, isSystem, isStorage));

        }

        return infos;
    }

    public static AppInfo getAppInfoByPackage(Context context, String packageName) {

        try {

            PackageManager pm = context.getPackageManager();

            ApplicationInfo info = pm.getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);

            String label = (String) info.loadLabel(pm);
            Drawable icon = info.loadIcon(pm);
            File file = new File(info.sourceDir); // data/ app / xxxx.apk

            boolean isSystem = false;
            //如果是成功就是系统程序
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                isSystem = true;
            }
            boolean isSdcard = false;
            if ((info.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
                isSdcard = true;
            }

            return new AppInfo(packageName, label, icon, file.length(), isSystem, isSdcard);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
