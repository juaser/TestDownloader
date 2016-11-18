package com.example.zxl.testdownload;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 下载信息数据库
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DOWN_INFO = "download.db";
    String sql = "create table download_info (id integer PRIMARY KEY AUTOINCREMENT,"
            + "thread_id integer,"
            + "start_pos integer,"
            + "end_pos integer,"
            + "complete_size integer,"
            + "url char)";

    public DBHelper(Context context) {
        super(context, DOWN_INFO, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
