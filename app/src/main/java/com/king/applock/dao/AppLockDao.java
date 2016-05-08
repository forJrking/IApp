package com.king.applock.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AppLockDao {

    AppLockDB helper = null;

    Context context;

    public AppLockDao(Context context) {
        helper = new AppLockDB(context);
        this.context = context;
    }

    public boolean insert(String packageName) {
        Uri uri = Uri.parse("dm://com.jrking.change");
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppLockDB.PACKAGENAME, packageName);
        long insert = -1;
        try {
            insert = db.insert(AppLockDB.APPLOCK_TB, null, values);
            context.getContentResolver().notifyChange(uri, null);
            db.close();
        }catch (Exception e){
            Toast.makeText(context,"已加入加锁应用",Toast.LENGTH_SHORT).show();
        }

        return insert > -1 ? true : false;
    }

    public boolean delete(String packageName) {
        Uri uri = Uri.parse("dm://com.jrking.change");
        SQLiteDatabase db = helper.getWritableDatabase();

        int result = db.delete(AppLockDB.APPLOCK_TB, AppLockDB.PACKAGENAME + "=?", new String[]{packageName});
        context.getContentResolver().notifyChange(uri, null);
        db.close();
        return result != 0 ? true : false;
    }

    public List<String> query() {
        List<String> list = new ArrayList<String>();

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(AppLockDB.APPLOCK_TB, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(AppLockDB.PACKAGENAME));

            list.add(data);
        }
        if (cursor != null)
            cursor.close();
        db.close();

        return list;
    }

    public boolean isAppLock(String packageName) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(AppLockDB.APPLOCK_TB, null, AppLockDB.PACKAGENAME + "=?", new String[]{packageName},
                    null, null, null);

            return cursor.getCount() > 0 ? true : false;
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
    }
}
