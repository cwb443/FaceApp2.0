package com.firefly.faceEngine.dblib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.firefly.faceEngine.dblib.bean.Person;
import com.firefly.faceEngine.dblib.bean.Setting;
import com.firefly.faceEngine.dblib.greendao.DaoMaster;
import com.firefly.faceEngine.dblib.greendao.DaoSession;
import com.firefly.faceEngine.dblib.greendao.SettingDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class SettingManage {

    public SettingManage(Context context) {
        setDatabase(context);
    }

    private SettingDao settingDao;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private void setDatabase(Context context) {
        if(mDaoSession==null){
            mHelper = new DaoMaster.DevOpenHelper(context, "setting.db", null);
            db = mHelper.getWritableDatabase();
            mDaoMaster = new DaoMaster(db);
            mDaoSession = mDaoMaster.newSession();
            settingDao = mDaoSession.getSettingDao();
        }
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public List<Setting> getSaveInformation(){
        QueryBuilder<Setting> qb = settingDao.queryBuilder();
        return qb.list();
    }

    public void UpdateInformation(Setting setting){
        settingDao.update(setting);
    }


    public void SaveInformation(Setting setting){
        settingDao.save(setting);
    }

}
