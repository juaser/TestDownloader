package com.example.zxl.testdownload;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载信息数据处理
 */
public class Dao {
    private DBHelper dbHelper;

    public Dao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean isNewTask(String url) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select count(*) from download_info where url=?";
        Cursor cursor = db.rawQuery(sql, new String[]{url});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    public void saveInfo(List<DownloadInfo> infos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "insert into download_info(thread_id,start_pos,end_pos,complete_size,url) values(?,?,?,?,?)";
        Object[] bindArgs = null;
        for (DownloadInfo info : infos) {
            bindArgs = new Object[]{info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompleteSize(), info.getUrl()};
            db.execSQL(sql, bindArgs);
        }

    }

    public List<DownloadInfo> getInfo(String url) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<DownloadInfo> infos = new ArrayList<DownloadInfo>();
        String sql = "select thread_id,start_pos,end_pos,complete_size,url from download_info where url=?";
        Cursor cursor = db.rawQuery(sql, new String[]{url});
        while (cursor.moveToNext()) {
            DownloadInfo info = new DownloadInfo(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
            infos.add(info);
        }
        cursor.close();
        return infos;

    }

    public void deleteInfo(String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("download_info", "url=?", new String[]{url});
        db.close();
    }

    public void updateInfo(int completeSize, int threadId, String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "update download_info set complete_size=? where thread_id=? and url=?";
        db.execSQL(sql, new Object[]{completeSize, threadId, url});
    }

    public void closeDB() {
        dbHelper.close();
    }

}