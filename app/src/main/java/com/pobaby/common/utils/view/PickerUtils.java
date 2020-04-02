package com.pobaby.common.utils.view;

import android.app.Activity;
import android.content.Context;

import androidx.core.content.ContextCompat;

import com.pobaby.memorybox.R;

import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 选择对话框生成工具
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/28 16:25
 */
public class PickerUtils {

    /**
     * @param activity 上下文
     * @param values   选项列表
     * @param title    标题
     * @param index    默认选中项索引
     * @param listener 回调
     * @return
     */
    public static OptionPicker createOptionPicker(Activity activity, List<String> values, String title, int index, OptionPicker.OnOptionPickListener listener) {

        OptionPicker mOptPicker = new OptionPicker(activity, values);
        mOptPicker.setTitleText(title);
        mOptPicker.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        mOptPicker.setTopBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        mOptPicker.setTitleTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryWhite));
        mOptPicker.setTextColor(ContextCompat.getColor(activity, R.color.colorLabel));
        mOptPicker.setCancelTextColor(ContextCompat.getColor(activity, R.color.colorSubtitle));
        mOptPicker.setSubmitTextColor(ContextCompat.getColor(activity, R.color.colorLabel));
        mOptPicker.setOnOptionPickListener(listener);
        mOptPicker.setSelectedIndex(index);
        return mOptPicker;
    }
}
