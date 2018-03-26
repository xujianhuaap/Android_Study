package com.example.xujianhua.myapplication;

/**
 * Created by xujianhua on 3/9/18.
 */

public class MediaPlayer {
    static {
        System.loadLibrary("MediaPlayerJNi");
    }
    public native void print(String name);
    public native static String getName();
}
