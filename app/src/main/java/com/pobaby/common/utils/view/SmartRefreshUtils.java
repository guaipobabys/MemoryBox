package com.pobaby.common.utils.view;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.pobaby.memorybox.R;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

/**
 * 下拉刷新统一工具类
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/23 15:45
 */
public class SmartRefreshUtils {

    /**
     * 设置 Header 为 贝塞尔雷达 样式
     *
     * @param context
     * @return
     */
    public static BezierRadarHeader getBezierRadarHeader(Context context) {
        return new BezierRadarHeader(context)
                .setEnableHorizontalDrag(true)
                .setAccentColorId(R.color.colorLabel);
    }

    /**
     * 设置 Footer 为 球脉冲 样式
     *
     * @param context
     * @return
     */
    public static BallPulseFooter getBallPulseFooter(Context context) {
        return new BallPulseFooter(context)
                .setSpinnerStyle(SpinnerStyle.FixedBehind)
                .setAnimatingColor(ContextCompat.getColor(context, R.color.colorLabel));
    }
}
