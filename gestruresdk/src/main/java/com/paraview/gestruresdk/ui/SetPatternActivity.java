package com.paraview.gestruresdk.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.paraview.gestruresdk.utils.GestrureSDK;
import com.paraview.gestruresdk.utils.PatternUtils;
import com.paraview.gestruresdk.utils.ResourceUtils;
import com.paraview.gestruresdk.utils.ViewAccessibilityCompat;
import com.paraview.gestruresdk.view.PatternView;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置图案解锁
 *
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2018/12/8 21:55
 */
public class SetPatternActivity extends BasePatternActivity implements PatternView.OnPatternListener {

    private enum LeftButtonState {

        Cancel(ResourceUtils.getIdByName(context, "string", "pl_cancel"), true),
        CancelDisabled(ResourceUtils.getIdByName(context, "string", "pl_cancel"), false),
        Redraw(ResourceUtils.getIdByName(context, "string", "pl_redraw"), true),
        RedrawDisabled(ResourceUtils.getIdByName(context, "string", "pl_redraw"), false);

        public final int textId;
        public final boolean enabled;

        LeftButtonState(int textId, boolean enabled) {
            this.textId = textId;
            this.enabled = enabled;
        }
    }

    private enum RightButtonState {

        Continue(ResourceUtils.getIdByName(context, "string", "pl_continue"), true),
        ContinueDisabled(ResourceUtils.getIdByName(context, "string", "pl_continue"), false),
        Confirm(ResourceUtils.getIdByName(context, "string", "pl_confirm"), true),
        ConfirmDisabled(ResourceUtils.getIdByName(context, "string", "pl_confirm"), false);

        public final int textId;
        public final boolean enabled;

        RightButtonState(int textId, boolean enabled) {
            this.textId = textId;
            this.enabled = enabled;
        }
    }

    private enum Stage {

        Draw(ResourceUtils.getIdByName(context, "string", "pl_draw_pattern"), LeftButtonState.Cancel, RightButtonState.ContinueDisabled, true),
        DrawTooShort(ResourceUtils.getIdByName(context, "string", "pl_pattern_too_short"), LeftButtonState.Redraw, RightButtonState.ContinueDisabled, true),
        DrawValid(ResourceUtils.getIdByName(context, "string", "pl_pattern_recorded"), LeftButtonState.Redraw, RightButtonState.Continue, false),
        Confirm(ResourceUtils.getIdByName(context, "string", "pl_confirm_pattern"), LeftButtonState.Cancel, RightButtonState.ConfirmDisabled, true),
        ConfirmWrong(ResourceUtils.getIdByName(context, "string", "pl_wrong_pattern"), LeftButtonState.Cancel, RightButtonState.ConfirmDisabled, true),
        ConfirmCorrect(ResourceUtils.getIdByName(context, "string", "pl_pattern_confirmed"), LeftButtonState.Cancel, RightButtonState.Confirm, false);

        public final int messageId;
        public final LeftButtonState leftButtonState;
        public final RightButtonState rightButtonState;
        public final boolean patternEnabled;

        Stage(int messageId, LeftButtonState leftButtonState, RightButtonState rightButtonState,
              boolean patternEnabled) {
            this.messageId = messageId;
            this.leftButtonState = leftButtonState;
            this.rightButtonState = rightButtonState;
            this.patternEnabled = patternEnabled;
        }
    }

    private static final String KEY_STAGE = "stage";
    private static final String KEY_PATTERN = "pattern";

    private int mMinPatternSize;
    private List<PatternView.Cell> mPattern;
    private Stage mStage;
    private static Context context;

    public static void setCallBack(GestrureSDK.OnSetGestrureCallBack callBack) {
        SetPatternActivity.callBack = callBack;
    }

    private static GestrureSDK.OnSetGestrureCallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        initUI(savedInstanceState);
    }

    private void initUI(Bundle savedInstanceState) {

        Toolbar mTitleToolBar = findViewById(ResourceUtils.getIdByName(this, "id", "toolbar"));
        TextView tvTitle = findViewById(ResourceUtils.getIdByName(this, "id", "toolbar_title"));
        tvTitle.setText("开启验证手势");

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

        mRelogin.setVisibility(View.GONE);
        mMinPatternSize = getMinPatternSize();

        mPatternView.setOnPatternListener(this);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftButtonClicked();
            }
        });
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightButtonClicked();
            }
        });

        if (savedInstanceState == null) {
            updateStage(Stage.Draw);
        } else {
            String patternString = savedInstanceState.getString(KEY_PATTERN);
            if (patternString != null) {
                mPattern = PatternUtils.stringToPattern(patternString);
            }
            updateStage(Stage.values()[savedInstanceState.getInt(KEY_STAGE)]);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_STAGE, mStage.ordinal());
        if (mPattern != null) {
            outState.putString(KEY_PATTERN, PatternUtils.patternToString(mPattern));
        }
    }

    @Override
    public void onPatternStart() {

        removeClearPatternRunnable();

        mMessageText.setText(ResourceUtils.getIdByName(context, "string", "pl_recording_pattern"));
        mPatternView.setDisplayMode(PatternView.DisplayMode.Correct);
        mLeftButton.setEnabled(false);
        mRightButton.setEnabled(false);
    }

    @Override
    public void onPatternCellAdded(List<PatternView.Cell> pattern) {
    }

    @Override
    public void onPatternDetected(List<PatternView.Cell> newPattern) {
        switch (mStage) {
            case Draw:
            case DrawTooShort:
                if (newPattern.size() < mMinPatternSize) {
                    updateStage(Stage.DrawTooShort);
                } else {
                    mPattern = new ArrayList<>(newPattern);
//                    updateStage(Stage.DrawValid); // TODO 修改为直接再次绘制确认手势
                    updateStage(Stage.Confirm);
                }
                break;
            case Confirm:
            case ConfirmWrong:
                if (newPattern.equals(mPattern)) {
//                    updateStage(Stage.ConfirmCorrect); // TODO 修改为直接完成新的手势
                    onSetPattern(mPattern);
                    onConfirmed();
                } else {
                    updateStage(Stage.ConfirmWrong);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected stage " + mStage + " when " + "entering the pattern.");
        }
    }

    @Override
    public void onPatternCleared() {
        removeClearPatternRunnable();
    }

    private void onLeftButtonClicked() {
        if (mStage.leftButtonState == LeftButtonState.Redraw) {
            mPattern = null;
            updateStage(Stage.Draw);
        } else if (mStage.leftButtonState == LeftButtonState.Cancel) {
            onCanceled();
        } else {
            throw new IllegalStateException("left footer button pressed, but stage of " + mStage + " doesn't make sense");
        }
    }

    protected void onCanceled() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void onRightButtonClicked() {
        if (mStage.rightButtonState == RightButtonState.Continue) {
            if (mStage != Stage.DrawValid) {
                throw new IllegalStateException("expected ui stage " + Stage.DrawValid
                        + " when button is " + RightButtonState.Continue);
            }
            updateStage(Stage.Confirm);
        } else if (mStage.rightButtonState == RightButtonState.Confirm) {
            if (mStage != Stage.ConfirmCorrect) {
                throw new IllegalStateException("expected ui stage " + Stage.ConfirmCorrect
                        + " when button is " + RightButtonState.Confirm);
            }
            onSetPattern(mPattern);
            onConfirmed();
        }
    }

    protected void onConfirmed() {
        setResult(Activity.RESULT_OK);
        finish();
        if (callBack != null) {
            callBack.onConfirmed(mPattern.toString());
        }
    }

    private void updateStage(Stage newStage) {

        Stage previousStage = mStage;
        mStage = newStage;

        if (mStage == Stage.DrawTooShort) {
            mMessageText.setText(getString(mStage.messageId, mMinPatternSize));
        } else {
            mMessageText.setText(mStage.messageId);
        }

        mLeftButton.setText(mStage.leftButtonState.textId);
        mLeftButton.setEnabled(mStage.leftButtonState.enabled);

        mRightButton.setText(mStage.rightButtonState.textId);
        mRightButton.setEnabled(mStage.rightButtonState.enabled);

        mPatternView.setInputEnabled(mStage.patternEnabled);

        switch (mStage) {
            case Draw:
                // clearPattern() resets display mode to DisplayMode.Correct.
                mPatternView.clearPattern();
                break;
            case DrawTooShort:
                mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case DrawValid:
                break;
            case Confirm:
                mPatternView.clearPattern();
                break;
            case ConfirmWrong:
                mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case ConfirmCorrect:
                break;
        }

        // If the stage changed, announce the header for accessibility. This
        // is a no-op when accessibility is disabled.
        if (previousStage != mStage) {
            ViewAccessibilityCompat.announceForAccessibility(mMessageText, mMessageText.getText());
        }
    }

    protected int getMinPatternSize() {
        return 4;
    }

    protected void onSetPattern(List<PatternView.Cell> pattern) {
    }

    @Override
    public void onBackPressed() {
        callBack.onCancel();
        super.onBackPressed();
    }
}
