package com.firefly.faceEngine.dblib;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.firefly.faceEngine.dblib.bean.Person;
import com.firefly.faceEngine.dblib.greendao.DaoMaster;
import com.firefly.faceEngine.dblib.greendao.DaoSession;
import com.firefly.faceEngine.dblib.greendao.PersonDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DBManager {

    public DBManager(Context context) {
        setDatabase(context);
    }

    private PersonDao personDao;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private void setDatabase(Context context) {
        if(mDaoSession==null){
            mHelper = new DaoMaster.DevOpenHelper(context, "person_db", null);
            db = mHelper.getWritableDatabase();
            mDaoMaster = new DaoMaster(db);
            mDaoSession = mDaoMaster.newSession();
            personDao = mDaoSession.getPersonDao();
        }
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public long insertPerson(String name, byte[] feature){
        Person person = new Person();
        person.setId(null);
        person.setName(name);
        person.setFeature(feature);
        return personDao.insert(person);
    }

    public long insertPerson(String name, byte[] feature, String imgUrl){
        Person person = new Person(null, name, feature, imgUrl);
        return personDao.insert(person);
    }

    //删除用户根据ID
    public void deletePerson(long id){
        personDao.deleteByKey(id);
    }

    //根据对象删除用户
    public void deletePerson(Person person){
        personDao.delete(person);
    }

    //删除所有
    public void deletePersonAll(){
        personDao.deleteAll();
    }

    //
    public long countPerson(){
        return personDao.count();
    }

    //更新用户返回为0无此用户为1修改成功
    public int updatePersonById(long id,String name){

        Person person = personDao.load(id);

        if (person==null)
            return 0;
        else{
            person.setName(name);
            personDao.update(person);
            return 1;
        }

    }

    public int updatePersonById(long id,String name,byte[] feature){
        Person person = personDao.load(id);

        if (person==null)
            return 0;
        else {
            person.setName(name);
            person.setFeature(feature);
            personDao.update(person);
            return 1;
        }

    }

    //获取用户列表
    public List<Person> getPersonList(){
        QueryBuilder<Person> qb = personDao.queryBuilder();
        return qb.list();
    }
}
