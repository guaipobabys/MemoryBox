package com.paraview.fingersdk.finger.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.paraview.fingersdk.finger.ui.CheckFingerActivity;


public class FingerSDK {

    private Context context;

    private FingerSDK(Context context) {
        this.context = context;
    }

    public static FingerSDK from(Context context) {
        return new FingerSDK(context);
    }

    public void checkFinger(String title, FingerType type, OnCheckedFingerCallBack callBack) {

        Intent intent = new Intent();
        CheckFingerActivity.setCallBack(callBack);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        if (context != null) {
            intent.setClass(context, CheckFingerActivity.class);
            context.startActivity(intent);
        } else {
            throw new IllegalArgumentException("you must call from() first");
        }
    }

    public static interface OnCheckedFingerCallBack {

        void onCancel();

        void onReLogin();

        void onConfirmed(FingerType type);
    }
}
