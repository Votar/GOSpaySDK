package com.gospay.rabbit.ui;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by bertalt on 09.09.16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initialize(Stetho.newInitializerBuilder(getApplicationContext())

                .enableDumpapp(Stetho.defaultDumperPluginsProvider(getApplicationContext()))

                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(getApplicationContext()))

                .build());
    }
}
