/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.pobaby.common.utils.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * 缓存工具类
 * @author chenqh
 * created at 2019/6/13 17:39
 */
public class PrefUtils {

    private PrefUtils() {
    }

    static Context mContext;

   /**
    * 初始化 必须调用该方法，不然会抛出异常
    * @author chenqh
    * created at 2019/6/13 17:40
    */
    public static void init(Context context) {
        mContext = context;
    }

    public static SharedPreferences getPreferences() {
        if (mContext == null) throw new NullPointerException();
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public static String getString(String key, String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValue) {
        return getPreferences().getStringSet(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        return getPreferences().getLong(key, defaultValue);
    }

    public static float getFloat(String key, float defaultValue) {
        return getPreferences().getFloat(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    public static SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }

    public static void putString(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public static void putStringSet(String key, Set<String> value) {
        getEditor().putStringSet(key, value).apply();
    }

    public static void putInt(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public static void putLong(String key, long value) {
        getEditor().putLong(key, value).apply();
    }

    public static void putFloat(String key, float value) {
        getEditor().putFloat(key, value).apply();
    }

    public static void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public static void remove(String key) {
        getEditor().remove(key).apply();
    }

    public static void clear(Context context) {
        getEditor().clear().apply();
    }
}
