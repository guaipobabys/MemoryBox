package com.pobaby.common.utils.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenqh on 2016-06-02.
 * 特殊字体定义
 */
public class FontUtils {

    static String fontgUrl = "fonts/PingFang Medium.ttf";
    static Typeface tf;


    /***
     * 设置字体
     *
     * @return
     */
    public static Typeface setFont(Context context) {
       /* if (AppConfig.IS_DEBUG) {
            return Typeface.DEFAULT;
        }*/
        if (tf == null) {
            //将字体文件保存在assets/fonts/目录下，创建Typeface对象
            AssetManager assets = context.getAssets();
            tf = Typeface.createFromAsset(assets, fontgUrl);
        }
        return tf;

    }

    /**
     * 设置搜索关键字高亮
     *
     * @param content 原文本内容
     * @param keyword 关键字
     */
    public static SpannableString setKeyWordColor(String content, String keyword) {
        if (TextUtils.isEmpty(content)) {
            return new SpannableString("");
        }
        SpannableString s = new SpannableString(content);
        Pattern p = Pattern.compile(keyword.trim());
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

}