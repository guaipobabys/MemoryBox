package com.paraview.fingersdk.finger;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.paraview.fingersdk.finger.lifecycle.LifecycleListener;
import com.paraview.fingersdk.finger.lifecycle.SupportFingerPrinterManagerFragment;


import static com.paraview.fingersdk.finger.CodeException.FINGERPRINTERS_FAILED_ERROR;
import static com.paraview.fingersdk.finger.CodeException.FINGERPRINTERS_RECOGNIZE_FAILED;
import static com.paraview.fingersdk.finger.CodeException.HARDWARE_MISSIING_ERROR;
import static com.paraview.fingersdk.finger.CodeException.KEYGUARDSECURE_MISSIING_ERROR;
import static com.paraview.fingersdk.finger.CodeException.NO_FINGERPRINTERS_ENROOLED_ERROR;
import static com.paraview.fingersdk.finger.CodeException.PERMISSION_DENIED_ERROE;
import static com.paraview.fingersdk.finger.CodeException.SYSTEM_API_ERROR;

public class FingerPrinter implements LifecycleListener {
    static final String TAG = "FingerPrinter";
    private FingerprintManager manager;
    private KeyguardManager mKeyManager;
    private Activity context;
    SupportFingerPrinterManagerFragment supportFingerPrinterManagerFragment;
    @SuppressLint("NewApi")
    CancellationSignal mCancellationSignal;
    @SuppressLint("NewApi")
    FingerprintManager.AuthenticationCallback authenticationCallback;
    private boolean mLogging;
    private boolean mSelfCompleted;
    private CallBack callBack;

    public static interface CallBack {

        void onStart();

        void onError(Throwable e);

        void onComplete();

        void onNext(IdentificationInfo info);
    }

    public FingerPrinter(@NonNull Activity activity) {
        this.context = activity;
        supportFingerPrinterManagerFragment = getRxPermissionsFragment(activity);
    }

    private SupportFingerPrinterManagerFragment getRxPermissionsFragment(Activity activity) {
        SupportFingerPrinterManagerFragment fragment = findRxPermissionsFragment(activity);
        boolean isNewInstance = fragment == null;
        if (isNewInstance) {
            fragment = new SupportFingerPrinterManagerFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
            fragment.getLifecycle().addListener(this);
        }
        return fragment;
    }

    private SupportFingerPrinterManagerFragment findRxPermissionsFragment(Activity activity) {
        return (SupportFingerPrinterManagerFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    public FingerPrinter begin() {
        return this;
    }

    public void callBack(CallBack callBack) {
        if (callBack == null) {
            throw new RuntimeException("CallBack can not be null!");
        }
        this.callBack = callBack;
        callBack.onStart();
        if (Build.VERSION.SDK_INT < 23) {
            callBack.onNext(new IdentificationInfo(SYSTEM_API_ERROR));
        } else {
            initManager();
            if (confirmFinger()) {
                startListening(null);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            callBack.onNext(new IdentificationInfo(PERMISSION_DENIED_ERROE));
        }
        mCancellationSignal = new CancellationSignal();
        if (manager != null && authenticationCallback != null) {
            mSelfCompleted = false;
            manager.authenticate(cryptoObject, mCancellationSignal, 0, authenticationCallback, null);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initManager() {
        manager = context.getSystemService(FingerprintManager.class);
        mKeyManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        authenticationCallback = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                //多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
                if (mCancellationSignal != null) {
                    callBack.onNext(new IdentificationInfo(FINGERPRINTERS_FAILED_ERROR));
                    mCancellationSignal.cancel();
                    mSelfCompleted = true;
                }
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                callBack.onNext(new IdentificationInfo(true));
                mSelfCompleted = true;
            }

            @Override
            public void onAuthenticationFailed() {
                callBack.onNext(new IdentificationInfo(FINGERPRINTERS_RECOGNIZE_FAILED));
            }
        };
    }

    @SuppressLint("NewApi")
    @TargetApi(23)
    public boolean confirmFinger() {

        boolean isDeviceSupport = true;

        //android studio 上，没有这个会报错
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            callBack.onNext(new IdentificationInfo(PERMISSION_DENIED_ERROE));
            isDeviceSupport = false;
        }
        //判断硬件是否支持指纹识别
        if (!manager.isHardwareDetected()) {
            callBack.onNext(new IdentificationInfo(HARDWARE_MISSIING_ERROR));
            isDeviceSupport = false;
        }
        //判断 是否开启锁屏密码

        if (!mKeyManager.isKeyguardSecure()) {
            callBack.onNext(new IdentificationInfo(KEYGUARDSECURE_MISSIING_ERROR));
            isDeviceSupport = false;
        }
        //判断是否有指纹录入
        if (!manager.hasEnrolledFingerprints()) {
            callBack.onNext(new IdentificationInfo(NO_FINGERPRINTERS_ENROOLED_ERROR));
            isDeviceSupport = false;
        }

        return isDeviceSupport;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void stopListening() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    @Override
    public void onStart() {
        log("LifeCycle--------onStart");
    }

    @Override
    public void onStop() {
        log("LifeCycle--------onStop");
    }

    @Override
    public void onResume() {
        if (!mSelfCompleted) {
            startListening(null);
        }
        log("LifeCycle--------onResume");
    }

    @Override
    public void onPause() {
        stopListening();
        log("LifeCycle--------onPause");
    }

    @Override
    public void onDestroy() {
        log("LifeCycle--------onDestroy");
    }

    public void setLogging(boolean logging) {
        mLogging = logging;
    }

    void log(String message) {
        if (mLogging) {
            Log.d(TAG, message);
        }
    }

}
