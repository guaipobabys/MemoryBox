package com.paraview.gestruresdk.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.paraview.gestruresdk.utils.ResourceUtils;
import com.paraview.gestruresdk.view.PatternView;

/**
 * 图案解锁窗体基类
 *
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2018/12/8 19:54
 */
public class BasePatternActivity extends AppCompatActivity {

    private static final int CLEAR_PATTERN_DELAY_MILLI = 2000;

    private final Runnable clearPatternRunnable = new Runnable() {
        public void run() {
            // clearPattern() resets display mode to DisplayMode.Correct.
            mPatternView.clearPattern();
        }
    };
    TextView mMessageText;
    PatternView mPatternView;
    Button mLeftButton;
    Button mRightButton;
    Button mRelogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtils.getIdByName(this, "layout", "activity_base_pattern"));

        mMessageText = findViewById(ResourceUtils.getIdByName(this, "id", "pl_message_text"));
        mPatternView = findViewById(ResourceUtils.getIdByName(this, "id", "pl_pattern"));
        mLeftButton = findViewById(ResourceUtils.getIdByName(this, "id", "pl_left_button"));
        mRightButton = findViewById(ResourceUtils.getIdByName(this, "id", "pl_right_button"));
        mRelogin = findViewById(ResourceUtils.getIdByName(this, "id", "pl_relogin"));
    }

    protected void removeClearPatternRunnable() {
        mPatternView.removeCallbacks(clearPatternRunnable);
    }

    protected void postClearPatternRunnable() {
        removeClearPatternRunnable();
        mPatternView.postDelayed(clearPatternRunnable, CLEAR_PATTERN_DELAY_MILLI);
    }

}
