package com.example.homeworkcorrectteacher;

import android.app.Application;

import io.rong.imkit.RongIM;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String appKey = "cpj2xarlcm6on";
        RongIM.init(this, appKey);
    }
}
