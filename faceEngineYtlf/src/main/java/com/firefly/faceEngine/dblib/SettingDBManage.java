package com.firefly.faceEngine.dblib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.firefly.faceEngine.dblib.greendao.DaoMaster;
import com.firefly.faceEngine.dblib.greendao.DaoSession;


public class SettingDBManage {

    private Context context;
    private final static String dbName = "setting.db";
    private static SettingDBManage mInstance;//单例
    private DaoMaster.DevOpenHelper openHelper;
    private DaoSession mDaoSession;
    private SQLiteDatabase db;
    public SettingDBManage(Context context){
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context,dbName,null);

    }

    public static SettingDBManage getInstance(Context context){
        if (mInstance == null){
            synchronized (SettingDBManage.class){
                if (mInstance == null){
                    mInstance = new SettingDBManage(context);
                }
            }
        }
        return mInstance;
    }

    public SQLiteDatabase getReadableDatabase(){
        if (openHelper == null){
            openHelper = new DaoMaster.DevOpenHelper(context,dbName,null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    public SQLiteDatabase getWritableDatabase(){
        if (openHelper == null){
            openHelper = new DaoMaster.DevOpenHelper(context,dbName,null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }
    public DaoSession getWriteDaoSession(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        mDaoSession = daoMaster.newSession();
        return mDaoSession;
    }
    /**
     * 获取可读的会话层
     * @return
     */
    public DaoSession getReadDaoSession(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        mDaoSession = daoMaster.newSession();
        return mDaoSession;
    }

}
