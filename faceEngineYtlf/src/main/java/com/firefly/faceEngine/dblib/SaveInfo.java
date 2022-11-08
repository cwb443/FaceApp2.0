package com.firefly.faceEngine.dblib;

import android.content.Context;


import com.firefly.faceEngine.dblib.bean.Setting;
import com.firefly.faceEngine.dblib.greendao.DaoSession;
import com.firefly.faceEngine.dblib.greendao.SettingDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class SaveInfo {

    public static boolean SaveInformation(Context context, Setting setting) {
        try {

            DaoSession daoSession = SettingDBManage.getInstance(context).getWriteDaoSession();
            SettingDao settingDao = daoSession.getSettingDao();
            settingDao.insert(setting);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean UpdateInformation(Context context, Setting setting) {
        try {

            DaoSession daoSession = SettingDBManage.getInstance(context).getWriteDaoSession();
            SettingDao settingDao = daoSession.getSettingDao();
//            userDao.createTable(daoSession.getDatabase(),true);
            settingDao.update(setting);
//            FileOutputStream fos = context.openFileOutput("data.txt", Context.MODE_APPEND);
//            fos.write(("用户名:" + user.getName() + " 密码:" + user.getPassword() + "邮箱：" + user.getMail()).getBytes());
//            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Setting> getSaveInformation(Context context) {
        try {
//            FileInputStream fis = context.openFileInput("data.txt");
//            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//            String str = br.readLine();
//            String[] infos = str.split("用户名:"+"密码:"+"邮箱:");
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("username", infos[0]);
//            map.put("password", infos[1]);
//            fis.close();
            DaoSession daoSession = SettingDBManage.getInstance(context).getReadDaoSession();
            SettingDao userDao = daoSession.getSettingDao();
//            Query<User> qb = userDao.queryRawCreate("WHERE name="+"'"+username+"'");
//            List<User> userList = qb.list();

            QueryBuilder<Setting> builder = userDao.queryBuilder();
            List<Setting> settingList = builder.list();
            if (settingList.size()>0){
                return settingList;
            }else{
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
