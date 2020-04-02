package com.paraview.gestruresdk.utils;

import android.content.Context;
import android.content.Intent;

import com.paraview.gestruresdk.ui.ConfirmPatternActivity;
import com.paraview.gestruresdk.ui.SetPatternActivity;


public class GestrureSDK {

    private Context context;

    private GestrureSDK(Context context) {
        this.context = context;
    }

    public static GestrureSDK from(Context context) {
        return new GestrureSDK(context);
    }


    public void setGestrure(OnSetGestrureCallBack callBack) {

        Intent intent = new Intent();
        SetPatternActivity.setCallBack(callBack);
        if (context != null) {
            intent.setClass(context, SetPatternActivity.class);
            context.startActivity(intent);
        } else {
            throw new IllegalArgumentException("you must call from() first");
        }
    }

    public void checkGestrure(GestrureType type, String pattern, OnCheckedGestrureCallBack callBack) {

        Intent intent = new Intent();
        ConfirmPatternActivity.setCallBack(pattern, callBack);
        intent.putExtra("type", type);
        if (context != null) {
            intent.setClass(context, ConfirmPatternActivity.class);
            context.startActivity(intent);
        } else {
            throw new IllegalArgumentException("you must call from() first");
        }
    }

    public static interface OnSetGestrureCallBack {

        void onCancel();

        void onConfirmed(String pattern);
    }

    public static interface OnCheckedGestrureCallBack {

        void onCancel();

        void onReLogin();

        void onConfirmed(GestrureType type);
    }
}
