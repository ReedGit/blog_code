package me.reed.crashdemo;

import android.app.Application;

/**
 * @author reed on 2017/10/8
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
