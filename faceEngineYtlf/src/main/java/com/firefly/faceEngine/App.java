package com.firefly.faceEngine;

import android.app.Application;
import android.content.Context;

import com.firefly.faceEngine.dblib.SettingManage;
import com.firefly.faceEngine.utils.Tools;
import com.firefly.faceEngine.dblib.DBManager;

public class App extends Application{
    private DBManager dbManager;
    private SettingManage settingManage;
    private static Context context;
    private static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        instance = this;
        Tools.init(this);
        initDBManager();
        initSettingManage();
    }

    private void initDBManager(){
        if(dbManager == null){
            dbManager = new DBManager(context);
        }
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    private void initSettingManage(){
        if(settingManage == null){
            settingManage = new SettingManage(context);
        }
    }

    public SettingManage getSettingManage(){
        return settingManage;
    }

    public static Context getContext(){
        return context;
    }

    public static App getInstance() {
        return instance;
    }
}
