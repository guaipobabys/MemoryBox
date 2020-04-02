package com.pobaby.common.utils.common;

import android.os.Environment;

import androidx.annotation.NonNull;

import com.pobaby.memorybox.app.AppConfig;
import com.pobaby.memorybox.app.MainApplication;

import java.io.File;

/**
 * 路径工具类
 *
 * @author chenqh
 * created at 2019/6/13 9:48
 */
public class PathUtils {

    /**
     * 获取sdcard路径
     */
    private static String getBasePath() {
        File sdDir = Environment.getExternalStorageDirectory();
        assert sdDir != null;
        return sdDir.toString();
    }

    /**
     * 获取APP运行路径
     */
    public static String getCachePath() {
        String path = getBasePath() + AppConfig.ROOT_CACHE;
        return mkdirs(path);
    }

    /**
     * 获取DB路径
     */
    public static String getDbPath() {
        String path = getCachePath() + AppConfig.ROOT_DB;
        return mkdirs(path);
    }

    @NonNull
    private static String mkdirs(String path) {
        File sdDir = new File(path);
        if (!sdDir.exists()) {
            sdDir.mkdirs();
        }
        return path;
    }
}
