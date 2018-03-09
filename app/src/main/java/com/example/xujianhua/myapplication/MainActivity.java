package com.example.xujianhua.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.example.app.BookOuterClass;
import com.example.xujianhua.myapplication.service.PlayerService;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private MusicPlayer musicPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this,PlayerService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                musicPlayer = MusicPlayer.Stub.asInterface(service);
                try {
                    musicPlayer.getMusicPlayerName();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        },BIND_AUTO_CREATE);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(MainActivity.class.getName(),"onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(MainActivity.class.getName(),"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(MainActivity.class.getName(),"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(MainActivity.class.getName(),"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(MainActivity.class.getName(),"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(MainActivity.class.getName(),"onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(MainActivity.class.getName(),"onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(MainActivity.class.getName(),"onRestoreInstanceState");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(MainActivity.class.getName(),"onConfigurationChanged");

    }
}
