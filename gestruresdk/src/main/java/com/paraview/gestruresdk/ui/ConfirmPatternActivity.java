package com.paraview.gestruresdk.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.paraview.gestruresdk.utils.GestrureSDK;
import com.paraview.gestruresdk.utils.GestrureType;
import com.paraview.gestruresdk.utils.ResourceUtils;
import com.paraview.gestruresdk.utils.ViewAccessibilityCompat;
import com.paraview.gestruresdk.view.PatternView;

import java.util.List;

/**
 * 图案解锁确认
 *
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2018/12/8 16:55
 */
public class ConfirmPatternActivity extends BasePatternActivity implements PatternView.OnPatternListener {

    private static final String KEY_NUM_FAILED_ATTEMPTS = "num_failed_attempts";

    public static final int RESULT_FORGOT_PASSWORD = Activity.RESULT_FIRST_USER;
    public static final int PATTERN_ERROR_COUNT = 5; // 手势解锁错误次数上限

    protected int mNumFailedAttempts;
    GestrureType type;
    private static GestrureSDK.OnCheckedGestrureCallBack callBack;
    private static String pattern;

    public static void setCallBack(String pattern, GestrureSDK.OnCheckedGestrureCallBack callBack) {
        ConfirmPatternActivity.callBack = callBack;
        ConfirmPatternActivity.pattern = pattern;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI(savedInstanceState);
    }

    private void initUI(Bundle savedInstanceState) {

        Toolbar mTitleToolBar = findViewById(ResourceUtils.getIdByName(this, "id", "toolbar"));
        TextView tvTitle = findViewById(ResourceUtils.getIdByName(this, "id", "toolbar_title"));
        tvTitle.setText("验证手势密码");
        type = (GestrureType) getIntent().getSerializableExtra("type");
        if (type == GestrureType.GESTRURE_CONFIRM) {

            mRelogin.setVisibility(View.GONE);

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

        } else if (type == GestrureType.GESTRURE_CHECK
                || type == GestrureType.GESTRURE_NO_PWD_AUTHORIZE) {
            mRelogin.setVisibility(View.VISIBLE);
        }

        mRelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if (callBack != null) {
                    callBack.onReLogin();
                }
            }
        });

        mMessageText.setText(ResourceUtils.getIdByName(this, "string", "pl_draw_pattern_to_unlock"));
        mPatternView.setInStealthMode(isStealthModeEnabled());
        mPatternView.setOnPatternListener(this);
        mLeftButton.setText(ResourceUtils.getIdByName(this, "string", "pl_cancel"));
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
        mRightButton.setText(ResourceUtils.getIdByName(this, "string", "pl_forgot_pattern"));
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForgotPassword();
            }
        });
        ViewAccessibilityCompat.announceForAccessibility(mMessageText, mMessageText.getText());

        if (savedInstanceState == null) {
            mNumFailedAttempts = 0;
        } else {
            mNumFailedAttempts = savedInstanceState.getInt(KEY_NUM_FAILED_ATTEMPTS);
        }
    }

    @Override
    public void onPatternStart() {
        removeClearPatternRunnable();
        // Set display mode to correct to ensure that pattern can be in stealth mode.
        mPatternView.setDisplayMode(PatternView.DisplayMode.Correct);
    }

    @Override
    public void onPatternCleared() {
        removeClearPatternRunnable();
    }

    @Override
    public void onPatternCellAdded(List<PatternView.Cell> pattern) {

    }

    @Override
    public void onPatternDetected(List<PatternView.Cell> pattern) {
        if (isPatternCorrect(pattern)) {
            onConfirmed();
        } else {
            mMessageText.setText(ResourceUtils.getIdByName(this, "string", "pl_wrong_pattern"));
            mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
            postClearPatternRunnable();
            ViewAccessibilityCompat.announceForAccessibility(mMessageText, mMessageText.getText());
            onWrongPattern();
        }
    }

    protected boolean isStealthModeEnabled() {
        return false;
    }

    protected boolean isPatternCorrect(List<PatternView.Cell> patternCheck) {
        if (pattern.equals(patternCheck.toString())) {
            return true;
        }
        return false;
    }

    protected void onConfirmed() {

        setResult(Activity.RESULT_OK);
        finish();
        if (callBack != null) {
            callBack.onConfirmed(type);
        }
    }

    protected void onWrongPattern() {
        ++mNumFailedAttempts;
        mMessageText.setText(String.format("图案错误，还可以再输入 %s 次", PATTERN_ERROR_COUNT - mNumFailedAttempts));
        mMessageText.setTextColor(Color.RED);
        if (mNumFailedAttempts >= PATTERN_ERROR_COUNT) {
            finish();
            if (callBack != null) {
                callBack.onReLogin();
            }
        }
    }

    protected void onCancel() {
        if (callBack != null) {
            callBack.onCancel();
        }
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    protected void onForgotPassword() {
        setResult(RESULT_FORGOT_PASSWORD);
        finish();
    }
}
