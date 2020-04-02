package com.pobaby.common.libs.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 使ViewPager不能滑动
 */
public class XViewPager extends ViewPager {

    public XViewPager(Context context) {
        super(context);
    }

    public XViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    boolean isTouch = false;

    public void setOnTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTouch) {
            return super.onTouchEvent(event);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isTouch) {
            try {
                return super.onInterceptTouchEvent(event);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
