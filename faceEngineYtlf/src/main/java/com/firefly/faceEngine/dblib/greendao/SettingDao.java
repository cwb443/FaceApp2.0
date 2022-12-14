package com.firefly.faceEngine.dblib.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.firefly.faceEngine.dblib.bean.Setting;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SETTING".
*/
public class SettingDao extends AbstractDao<Setting, Long> {

    public static final String TABLENAME = "SETTING";

    /**
     * Properties of entity Setting.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Recognition = new Property(1, Integer.class, "recognition", false, "RECOGNITION");
        public final static Property White = new Property(2, Integer.class, "white", false, "WHITE");
        public final static Property Brightness = new Property(3, Integer.class, "brightness", false, "BRIGHTNESS");
        public final static Property Infrared = new Property(4, Integer.class, "infrared", false, "INFRARED");
        public final static Property Predicted = new Property(5, String.class, "predicted", false, "PREDICTED");
        public final static Property Recommended = new Property(6, String.class, "recommended", false, "RECOMMENDED");
        public final static Property CustomerList = new Property(7, Integer.class, "customerList", false, "CUSTOMER_LIST");
        public final static Property JumpInterval = new Property(8, String.class, "jumpInterval", false, "JUMP_INTERVAL");
    }


    public SettingDao(DaoConfig config) {
        super(config);
    }
    
    public SettingDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SETTING\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"RECOGNITION\" INTEGER," + // 1: recognition
                "\"WHITE\" INTEGER," + // 2: white
                "\"BRIGHTNESS\" INTEGER," + // 3: brightness
                "\"INFRARED\" INTEGER," + // 4: infrared
                "\"PREDICTED\" TEXT," + // 5: predicted
                "\"RECOMMENDED\" TEXT," + // 6: recommended
                "\"CUSTOMER_LIST\" INTEGER," + // 7: customerList
                "\"JUMP_INTERVAL\" TEXT);"); // 8: jumpInterval
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SETTING\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Setting entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer recognition = entity.getRecognition();
        if (recognition != null) {
            stmt.bindLong(2, recognition);
        }
 
        Integer white = entity.getWhite();
        if (white != null) {
            stmt.bindLong(3, white);
        }
 
        Integer brightness = entity.getBrightness();
        if (brightness != null) {
            stmt.bindLong(4, brightness);
        }
 
        Integer infrared = entity.getInfrared();
        if (infrared != null) {
            stmt.bindLong(5, infrared);
        }
 
        String predicted = entity.getPredicted();
        if (predicted != null) {
            stmt.bindString(6, predicted);
        }
 
        String recommended = entity.getRecommended();
        if (recommended != null) {
            stmt.bindString(7, recommended);
        }
 
        Integer customerList = entity.getCustomerList();
        if (customerList != null) {
            stmt.bindLong(8, customerList);
        }
 
        String jumpInterval = entity.getJumpInterval();
        if (jumpInterval != null) {
            stmt.bindString(9, jumpInterval);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Setting entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer recognition = entity.getRecognition();
        if (recognition != null) {
            stmt.bindLong(2, recognition);
        }
 
        Integer white = entity.getWhite();
        if (white != null) {
            stmt.bindLong(3, white);
        }
 
        Integer brightness = entity.getBrightness();
        if (brightness != null) {
            stmt.bindLong(4, brightness);
        }
 
        Integer infrared = entity.getInfrared();
        if (infrared != null) {
            stmt.bindLong(5, infrared);
        }
 
        String predicted = entity.getPredicted();
        if (predicted != null) {
            stmt.bindString(6, predicted);
        }
 
        String recommended = entity.getRecommended();
        if (recommended != null) {
            stmt.bindString(7, recommended);
        }
 
        Integer customerList = entity.getCustomerList();
        if (customerList != null) {
            stmt.bindLong(8, customerList);
        }
 
        String jumpInterval = entity.getJumpInterval();
        if (jumpInterval != null) {
            stmt.bindString(9, jumpInterval);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Setting readEntity(Cursor cursor, int offset) {
        Setting entity = new Setting( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // recognition
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // white
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // brightness
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // infrared
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // predicted
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // recommended
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // customerList
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // jumpInterval
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Setting entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRecognition(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setWhite(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setBrightness(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setInfrared(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setPredicted(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRecommended(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCustomerList(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setJumpInterval(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Setting entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Setting entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Setting entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
