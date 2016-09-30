package com.haidangkf.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.haidangkf.musicplayer.utils.Common;

public class MyService extends Service {

    public static Thread myThread;

    // constructor
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Common.TAG, "onCreate Service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Common.TAG, "onDestroy Service");

//        Thread.currentThread().interrupt(); // làm gián đoạn Thread hiện tại khi Service bị huỷ
//        myThread = null; // set this to make the condition in while loop (of method ThreadRemind) become false --> just single Thread run at a time
    }

    // onStartCommand() is called EVERY TIME a client starts the service using startService(Intent intent)
    // Dù ứng dụng bị tắt nó vẫn tự chạy lại được
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Common.TAG, "onStartCommand Service");

//        threadDownload();

        return START_STICKY; // when service is killed by system, it tells the OS to recreate the service after it has enough memory
    }

    public void threadDownload() {
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (myThread == Thread.currentThread()) {
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        myThread.start();
    }
}
