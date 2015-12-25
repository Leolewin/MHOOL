package com.summer.mho.application;

import android.app.Application;
import android.content.Context;

/**
 * 包名      com.summer.mho.application
 * 类名      MyApplication
 * 创建时间   2015/12/22
 * 创建人     Summer
 * 类描述
 * 所属类     {@link }
 */
public class MyApplication extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getMyApplicationContext() {
        return context;
    }
}
