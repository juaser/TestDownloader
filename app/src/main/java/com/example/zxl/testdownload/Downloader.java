package com.example.zxl.testdownload;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件下载 统一工具类
 */
public class Downloader {
    public static final String TAG = "TAG";
    private String url;//网络下载的路径
    private String localPath;//存储手机路径
    private int threadCount;//线程数量
    private Handler mHanler;
    private Dao dao;
    private int fileSize;//文件大小
    private List<DownloadInfo> infos;
    private final static int INIT = 1;//开始下载
    private final static int DOWNLOADING = 2;//正在下载
    private final static int PAUSE = 3;//暂停
    private int state = INIT;

    public Downloader(String address, String lPath, int thCount, Context context, Handler handler) {
        url = address;
        localPath = lPath;
        threadCount = thCount;
        mHanler = handler;
        dao = new Dao(context);
    }

    public boolean isDownloading() {
        return state == DOWNLOADING;
    }

    public LoadInfo getDownLoadInfos() {
        if (isFirstDownload(url)) {
            init();
            int range = fileSize / threadCount;
            infos = new ArrayList<>();
            for (int i = 0; i <= threadCount - 1; i++) {
                DownloadInfo info = new DownloadInfo(i, i * range, (i + 1) * range - 1, 0, url);
                infos.add(info);
            }
            dao.saveInfo(infos);
            return new LoadInfo(fileSize, 0, url);
        } else {
            infos = dao.getInfo(url);
            int size = 0;
            int completeSize = 0;
            for (DownloadInfo info : infos) {
                completeSize += info.getCompleteSize();
                size += info.getEndPos() - info.getStartPos() + 1;
            }
            return new LoadInfo(size, completeSize, url);
        }
    }

    public boolean isFirstDownload(String url) {
        return dao.isNewTask(url);
    }

    public void init() {
        try {
            URL mUrl = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            fileSize = connection.getContentLength();

            File file = new File(localPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
            accessFile.setLength(fileSize);
            accessFile.close();
            connection.disconnect();

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException");
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        }
    }

    public void download() {
        if (infos != null) {
            if (state == DOWNLOADING) {
                return;
            }
            state = DOWNLOADING;
            for (DownloadInfo info : infos) {
                new DownloadThread(info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompleteSize(), info.getUrl()).start();

            }
        }
    }

    class DownloadThread extends Thread {
        private int threadId;
        private int startPos;
        private int endPos;
        private int completeSize;
        private String url;

        public DownloadThread(int tId, int sp, int ep, int cSize, String address) {
            threadId = tId;
            startPos = sp;
            endPos = ep;
            completeSize = cSize;
            url = address;
        }

        @Override
        public void run() {
            HttpURLConnection connection = null;
            RandomAccessFile randomAccessFile = null;
            InputStream is = null;
            try {
                URL mUrl = new URL(url);
                connection = (HttpURLConnection) mUrl.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Range", "bytes=" + (startPos + completeSize) + "-" + endPos);
                randomAccessFile = new RandomAccessFile(localPath, "rwd");
                randomAccessFile.seek(startPos + completeSize);
                is = connection.getInputStream();
                byte[] buffer = new byte[4096];
                int length = -1;
                while ((length = is.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, length);
                    completeSize += length;
                    dao.updateInfo(threadId, completeSize, url);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = url;
                    msg.arg1 = length;
                    mHanler.sendMessage(msg);
                    if (state == PAUSE) {
                        return;
                    }
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException");
            } catch (IOException e) {
                Log.e(TAG, "IOException");
            } finally {
                try {
                    is.close();
                    randomAccessFile.close();
                    connection.disconnect();
                    dao.closeDB();
                } catch (IOException e) {
                    Log.e(TAG, "IOException");
                }

            }

        }

    }

    public void delete(String url) {
        dao.deleteInfo(url);
    }

    public void reset() {
        state = INIT;
    }

    public void pause() {
        state = PAUSE;
    }

}