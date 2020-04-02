package com.pobaby.common.utils.view;

import android.content.Context;
import android.widget.Toast;

/**
 * 气泡工具栏
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/17 11:01
 */
public class ToastUtils {
    private static Toast mToast;

    private ToastUtils(Context context) {

    }

    //在applicaton类的oncreate方法中初始化
    public static void init(Context context) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
    }

    public static void show(final String text) {
        mToast.setText(text);
        mToast.show();
    }

}
