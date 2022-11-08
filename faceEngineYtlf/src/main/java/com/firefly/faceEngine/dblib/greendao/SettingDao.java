package com.firefly.faceEngine.dblib.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.firefly.faceEngine.dblib.bean.Setting;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

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
        public final static Property Red = new Property(2, Integer.class, "red", false, "RED");
        public final static Property Brightness = new Property(3, Integer.class, "brightness", false, "BRIGHTNESS");
        public final static Property Infrared = new Property(4, Integer.class, "infrared", false, "INFRARED");
        public final static Property Forecast = new Property(5, String.class, "forecast", false, "FORECAST");
        public final static Property Products = new Property(6, String.class, "products", false, "PRODUCTS");
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
                "\"RED\" INTEGER," + // 2: red
                "\"BRIGHTNESS\" INTEGER," + // 3: brightness
                "\"INFRARED\" INTEGER," + // 4: infrared
                "\"FORECAST\" TEXT," + // 5: forecast
                "\"PRODUCTS\" TEXT);"); // 6: products
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
 
        Integer red = entity.getRed();
        if (red != null) {
            stmt.bindLong(3, red);
        }
 
        Integer brightness = entity.getBrightness();
        if (brightness != null) {
            stmt.bindLong(4, brightness);
        }
 
        Integer infrared = entity.getInfrared();
        if (infrared != null) {
            stmt.bindLong(5, infrared);
        }
 
        String forecast = entity.getForecast();
        if (forecast != null) {
            stmt.bindString(6, forecast);
        }
 
        String products = entity.getProducts();
        if (products != null) {
            stmt.bindString(7, products);
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
 
        Integer red = entity.getRed();
        if (red != null) {
            stmt.bindLong(3, red);
        }
 
        Integer brightness = entity.getBrightness();
        if (brightness != null) {
            stmt.bindLong(4, brightness);
        }
 
        Integer infrared = entity.getInfrared();
        if (infrared != null) {
            stmt.bindLong(5, infrared);
        }
 
        String forecast = entity.getForecast();
        if (forecast != null) {
            stmt.bindString(6, forecast);
        }
 
        String products = entity.getProducts();
        if (products != null) {
            stmt.bindString(7, products);
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
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // red
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // brightness
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // infrared
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // forecast
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // products
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Setting entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRecognition(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setRed(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setBrightness(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setInfrared(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setForecast(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setProducts(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
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
