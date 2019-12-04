package com.example.lwxg.changweistory;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MyApp extends Application {
    public static Context sContext;//全局的Context对象
    public static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sharedPreferences = sContext.getSharedPreferences("data", Context.MODE_PRIVATE);
    }
}
