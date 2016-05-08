package com.king.applock.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;

import java.util.List;

/**
 * @Description 系统服务工具类
 * @author Jrking
 * @date 2016-1-5 下午4:03:46
 */

public class ServiceUtil {

    public static boolean isServiceRuning(Context context, Class<? extends Service> clazz) {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取系统当前运行的所有服务
        List<RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);

        for (RunningServiceInfo runningServiceInfo : runningServices) {
            String className = runningServiceInfo.service.getClassName();
            if (className.equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }
}
