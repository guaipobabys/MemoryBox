package com.pobaby.common.ui.base;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pobaby.common.libs.baseview.XTextView;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.ui.main.SplashActivity;
import com.zhl.cbdialog.CBDialogBuilder;

import java.util.Random;

/**
 * Activity基类
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/16 15:37
 */
public class BaseActivity extends AppCompatActivity {

    private static int[] mResIds = new int[]{
            R.mipmap.ic_view_background_1,
            R.mipmap.ic_view_background_2,
            R.mipmap.ic_view_background_3,
            R.mipmap.ic_view_background_4,
            R.mipmap.ic_view_background_5,
            R.mipmap.ic_view_background_6,
            R.mipmap.ic_view_background_7
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        randomBackground();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        randomBackground();
    }

    private void randomBackground() {
        // 随机背景
        LinearLayout rootView = findViewById(R.id.mLlRootView);
        if (rootView != null) {
            rootView.setBackgroundResource(mResIds[new Random().nextInt(mResIds.length)]);
        }
        if (this instanceof SplashActivity) {
            getWindow().getDecorView().setBackgroundResource(mResIds[new Random().nextInt(mResIds.length)]);
        }
    }

    @TargetApi(23)
    protected boolean checkPermission(String permission, int requestCode) {
        int hasWriteContactsPermission = checkSelfPermission(permission);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    @TargetApi(23)
    protected boolean checkPermission(String[] permissions, int requestCode) {
        boolean b = false;
        for (int i = 0; i < permissions.length; i++) {
            int hasWriteContactsPermission = checkSelfPermission(permissions[i]);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                b = true;
                break;
            }
        }
        if (b) {
            requestPermissions(permissions, requestCode);
            return false;
        }
        return true;
    }

    public Toolbar initToolBar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            TextView toolbaTitle = toolbar.findViewById(R.id.toolbar_title);
            toolbaTitle.setText(title);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        return toolbar;
    }

    public Toolbar initToolBarAsHome(String title) {

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            XTextView toolTitle = findViewById(R.id.toolbar_title);
            toolTitle.setText(title);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        return toolbar;
    }

    public void setToolBarPadding() {
        findViewById(R.id.toolbar).setPadding(0, (int) this.getStatusBarHeight(), 0, 0);
    }

    @Override
    public void setTitle(CharSequence title) {
        XTextView toolTitle = findViewById(R.id.toolbar_title);
        toolTitle.setText(title);
    }

    public void setBackPressed(int resId) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(resId);
        }
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }


    /**
     * 显示只有确定的提示对话框
     *
     * @param msg
     * @param listener
     */
    public void showAskDialog(String msg, CBDialogBuilder.onDialogbtnClickListener listener) {
        showAskDialog(msg, false, listener);
    }

    /**
     * 显示有确定、取消的提示对话框
     *
     * @param msg
     * @param showCancel
     * @param listener
     */
    public void showAskDialog(String msg, boolean showCancel, CBDialogBuilder.onDialogbtnClickListener listener) {
        showAskDialog("确认", "取消", msg, showCancel, listener);
    }

    /**
     * 显示自定义按钮文字的提示对话框
     *
     * @param confirmButtonText
     * @param cancelButtonText
     * @param msg
     * @param showCancel
     * @param listener
     */
    public void showAskDialog(String confirmButtonText,
                              String cancelButtonText,
                              String msg,
                              boolean showCancel,
                              CBDialogBuilder.onDialogbtnClickListener listener) {
        new CBDialogBuilder(this)
                .setTouchOutSideCancelable(false)
                .showCancelButton(showCancel)
                .showIcon(false)
                .setTitle("温馨提示")
                .setMessage(msg)
                .setCancelable(false)
                .setConfirmButtonText(confirmButtonText)
                .setCancelButtonText(cancelButtonText)
                .setButtonClickListener(true, listener)
                .setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_NORMAL)
                .create()
                .show();
    }

    /**
     * 返回值就是状态栏的高度,得到的值单位px
     *
     * @author chenqh
     * @created 2018/7/17 9:41
     */
    public float getStatusBarHeight() {
        float result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimension(resourceId);
        }
        return result;
    }
}
