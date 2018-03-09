package com.example.xujianhua.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by xujianhua on 3/4/18.
 */

public class PlayerService extends Service {
    IBinder binder = new com.example.xujianhua.myapplication.MusicPlayer.Stub(){
        @Override
        public String getMusicPlayerName() throws RemoteException {
            return "123456";
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
