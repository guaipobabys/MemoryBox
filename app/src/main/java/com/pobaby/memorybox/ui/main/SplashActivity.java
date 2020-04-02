package com.pobaby.memorybox.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pobaby.common.ui.base.BaseActivity;

/**
 * 闪屏页
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/24 10:21
 */
public class SplashActivity extends BaseActivity {

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 101;
    private static final Long DELAY_MILLIS = 1000L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA
            }, READ_EXTERNAL_STORAGE_REQUEST_CODE)) {
                delayedLoad();
            }
        } else {
            delayedLoad();
        }
    }

    private void delayedLoad() {
        new Handler().postDelayed(() -> gotoNext(), DELAY_MILLIS);
    }

    private void gotoNext() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            //权限通过
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    delayedLoad();
                } else {
                    // TODO 提示有未授权的权限，程序不能使用
//                    ToastUtil.show("用户拒绝了请求的储存读写权限！");
                    finish();
                    System.exit(0);
                }
                return;
            }
        }
    }
}
