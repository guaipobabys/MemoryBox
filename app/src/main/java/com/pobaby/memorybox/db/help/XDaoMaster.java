package com.pobaby.memorybox.db.help;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pobaby.memorybox.db.AccountModelDao;
import com.pobaby.memorybox.db.DaoMaster;

import org.greenrobot.greendao.database.Database;

public class XDaoMaster extends DaoMaster.OpenHelper {
    private static final String TAG = "XDaoMaster";

    public XDaoMaster(Context context, String name) {
        super(context, name);
    }

    public XDaoMaster(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, AccountModelDao.class);
        Log.e(TAG, "onUpgrade: " + oldVersion + " newVersion = " + newVersion);
    }
}

