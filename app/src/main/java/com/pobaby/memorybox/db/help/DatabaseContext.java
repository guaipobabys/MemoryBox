package com.pobaby.memorybox.db.help;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.pobaby.common.utils.common.PathUtils;

import java.io.File;

public class DatabaseContext extends ContextWrapper {
    public DatabaseContext(Context base) {
        super(base);
    }

    /**
     * 获得数据库路径，如果不存在，则自动创建
     */
    @Override
    public File getDatabasePath(String name) {
        return new File(PathUtils.getDbPath() + name);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }
}
