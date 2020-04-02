package com.paraview.fingersdk.finger.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.paraview.fingersdk.BuildConfig;
import com.paraview.fingersdk.finger.FPerException;
import com.paraview.fingersdk.finger.FingerPrinter;
import com.paraview.fingersdk.finger.IdentificationInfo;
import com.paraview.fingersdk.finger.utils.FingerSDK;
import com.paraview.fingersdk.finger.utils.FingerType;
import com.paraview.fingersdk.finger.utils.ResourceUtils;
import com.paraview.fingersdk.finger.view.FingerPrinterView;

import java.util.Timer;
import java.util.TimerTask;

import static com.paraview.fingersdk.finger.CodeException.FINGERPRINTERS_FAILED_ERROR;
import static com.paraview.fingersdk.finger.CodeException.HARDWARE_MISSIING_ERROR;
import static com.paraview.fingersdk.finger.CodeException.KEYGUARDSECURE_MISSIING_ERROR;
import static com.paraview.fingersdk.finger.CodeException.NO_FINGERPRINTERS_ENROOLED_ERROR;
import static com.paraview.fingersdk.finger.CodeException.PERMISSION_DENIED_ERROE;
import static com.paraview.fingersdk.finger.CodeException.SYSTEM_API_ERROR;


/**
 * 验证指纹
 *
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2018/12/10 19:19
 */
public class CheckFingerActivity extends AppCompatActivity {

    Button loginBtn;
    private Dialog dialog;
    private FingerPrinterView fingerPrinterView;
    private FingerPrinter rxfingerPrinter;
    private FingerPrinter.CallBack callBack;
    FingerType type;
    private boolean mIsException = false;

    public static void setCallBack(FingerSDK.OnCheckedFingerCallBack callBackChecked) {
        CheckFingerActivity.callBackChecked = callBackChecked;
    }

    private static FingerSDK.OnCheckedFingerCallBack callBackChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtils.getIdByName(this, "layout", "activity_check_finger"));
        initUI();
    }

    private void initUI() {

        String title = getIntent().getStringExtra("title");
        type = (FingerType) getIntent().getSerializableExtra("type");

        loginBtn = findViewById(ResourceUtils.getIdByName(this, "id", "login_btn"));

        Toolbar mTitleToolBar = findViewById(ResourceUtils.getIdByName(this, "id", "toolbar"));
        TextView tvTitle = findViewById(ResourceUtils.getIdByName(this, "id", "toolbar_title"));
        tvTitle.setText(title);

        findViewById(ResourceUtils.getIdByName(this, "id", "iv_pl_finger")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFingerDialog();
            }
        });
        findViewById(ResourceUtils.getIdByName(this, "id", "login_btn")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackChecked != null) {
                    callBackChecked.onReLogin();
                }
            }
        });

        if (type == FingerType.FINGER_SET) {

            setSupportActionBar(mTitleToolBar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                //去除默认Title显示
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(ResourceUtils.getIdByName(this, "drawable", "arrow"));
            }
            mTitleToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            loginBtn.setVisibility(View.GONE);
        } else {
            loginBtn.setVisibility(View.VISIBLE);
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(SHOW_FINGER_DIALOG);
            }
        }, 200);
    }

    private static final int SHOW_FINGER_DIALOG = 1001;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_FINGER_DIALOG:
                    showFingerDialog();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        /*if (type == FingerType.FINGER_SET || type == FingerType.FINGER_CONFIRM) {
            callBackChecked.onCancel();
        }*/
        callBackChecked.onCancel();
        super.onBackPressed();
    }

    private void showFingerDialog() {
        if (dialog == null) {
            View view = LayoutInflater.from(this).inflate(ResourceUtils.getIdByName(this, "layout", "dialog_check_finger"), null);
            view.findViewById(ResourceUtils.getIdByName(this, "id", "tv_cancel")).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog = new Dialog(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(view,
                    new LinearLayout.LayoutParams(800, LinearLayout.LayoutParams.WRAP_CONTENT));

            if (fingerPrinterView == null) {
                initFingerView(view);
            }
        }
        createCallBack();
        rxfingerPrinter.begin().callBack(callBack);
        if (!mIsException) {
            dialog.show();
        }
    }

    private void initFingerView(View view) {
        fingerPrinterView = view.findViewById(ResourceUtils.getIdByName(this, "id", "fpv"));
        fingerPrinterView.setOnStateChangedListener(new FingerPrinterView.OnStateChangedListener() {
            @Override
            public void onChange(int state) {
                if (state == FingerPrinterView.STATE_WRONG_PWD) {
                    fingerPrinterView.setState(FingerPrinterView.STATE_NO_SCANING);
                }
            }
        });
        rxfingerPrinter = new FingerPrinter(this);
        rxfingerPrinter.setLogging(BuildConfig.DEBUG);
    }

    private void createCallBack() {
        callBack = new FingerPrinter.CallBack() {
            @Override
            public void onStart() {
                if (fingerPrinterView.getState() == FingerPrinterView.STATE_SCANING) {
                    return;
                } else if (fingerPrinterView.getState() == FingerPrinterView.STATE_CORRECT_PWD
                        || fingerPrinterView.getState() == FingerPrinterView.STATE_WRONG_PWD) {
                    fingerPrinterView.setState(FingerPrinterView.STATE_NO_SCANING);
                } else {
                    fingerPrinterView.setState(FingerPrinterView.STATE_SCANING);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onNext(IdentificationInfo info) {
                if (info.isSuccessful()) {
                    fingerPrinterView.setState(FingerPrinterView.STATE_CORRECT_PWD);
                    showMessage("指纹识别成功");
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    setResult(Activity.RESULT_OK);
                    finish();
                    if (callBackChecked != null) {
                        callBackChecked.onConfirmed(type);
                    }

                } else {
                    FPerException exception = info.getException();
                    if (exception != null) {

                        switch (exception.getCode()) {
                            case SYSTEM_API_ERROR: // 系统API小于23
                            case PERMISSION_DENIED_ERROE:// 没有指纹识别权限
                            case HARDWARE_MISSIING_ERROR:// 没有指纹识别模块
                            case KEYGUARDSECURE_MISSIING_ERROR:// 没有开启锁屏密码
                            case NO_FINGERPRINTERS_ENROOLED_ERROR:// 没有指纹录入

                                mIsException = true;
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                showMessage(exception.getDisplayMessage());
                                finish();
                                break;

                            case FINGERPRINTERS_FAILED_ERROR: // 多次指纹密码验证错误

                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                showMessage("多次指纹密码验证错误，请稍候再试！");
                                break;


                            default:
                                showMessage(exception.getDisplayMessage());
                                break;
                        }
                    }
                    fingerPrinterView.setState(FingerPrinterView.STATE_WRONG_PWD);
                }
            }
        };
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
