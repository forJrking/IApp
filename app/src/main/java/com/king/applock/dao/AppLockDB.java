package com.king.applock.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLockDB extends SQLiteOpenHelper {
    
    public static final String APPLOCK_TB = "applock";

    public static final String PACKAGENAME = "packagename";
    
    
    /** @Fields BLACKLIST_DB: 数据库名*/
      	
    private static final String APPLOCK_DB = "applock.db";
    /** @Fields versionCode:数据库版本 */
      	
    private static int versionCode = 1;

    //建表语句
    public static final String CREATE_TABLE_SQL = "CREATE TABLE "+APPLOCK_TB+" ( _ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
            PACKAGENAME+" VARCHAR UNIQUE)";
    
    /**
     * 构造函数
     * 
     * @param context
     */
    public AppLockDB(Context context) {
        super(context, APPLOCK_DB, null, versionCode );

    }

    /**
     * 创建数据库
     * 
     * @param db
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
       
        // 执行一次
        db.execSQL(CREATE_TABLE_SQL);
       
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("drop table applock");
       onCreate(db);
    }

}
