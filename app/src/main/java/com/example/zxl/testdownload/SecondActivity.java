package com.example.zxl.testdownload;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2016/11/18 15:18
 */

public class SecondActivity extends FragmentActivity {

    private ProgressBar progressBar;
    private TextView tvStart, tvPause, tvUrl;
    private Downloader downloader;
    private String downUrl = "http://img4.duitang.com/uploads/item/201206/11/20120611174542_5KRMj.jpeg";
    private String localPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    public void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        tvStart = (TextView) findViewById(R.id.tv_start);
        tvPause = (TextView) findViewById(R.id.tv_pause);
        tvUrl = (TextView) findViewById(R.id.tv_url);
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
        tvPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });

    }

    /**
     * 初始化下载数据
     */
    public void initData() {
        localPath = Environment.getExternalStorageDirectory().getPath() + "/test1.jpg";
        downloader = new Downloader(downUrl, localPath, 3, getApplicationContext(), handler);
    }

    void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoadInfo loadInfo = downloader.getDownLoadInfos();
                Message msg = handler.obtainMessage();
                msg.what = 2;
                msg.arg1 = loadInfo.getFileSize();
                handler.sendMessage(msg);
                downloader.download();
            }
        }).start();
    }

    void pause() {
        downloader.pause();
    }

    //处理下载进度UI的
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                tvUrl.setText((String) msg.obj);
                tvUrl.setVisibility(View.VISIBLE);
                int lenght = msg.arg1;
                progressBar.incrementProgressBy(lenght);
            }
            if (msg.what == 2) {
                int max = msg.arg1;
                progressBar.setMax(max);
                Toast.makeText(getApplicationContext(), max + "", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
