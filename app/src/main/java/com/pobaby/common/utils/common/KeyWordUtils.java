package com.pobaby.common.utils.common;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 关键字工具类
 *
 * @author chenqh
 * created at 2019/6/26 14:38
 */
public class KeyWordUtils {

    /**
     * 设置搜索关键字高亮
     *
     * @param content 原文本内容
     * @param keyword 关键字
     */
    public static SpannableString setKeyWordColor(String content, String keyword) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(keyword)) {
            return new SpannableString(content);
        }
        SpannableString s = new SpannableString(content);
        Pattern p = Pattern.compile(keyword.trim());
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#f69d6c")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

}