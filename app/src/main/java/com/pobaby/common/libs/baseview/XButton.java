package com.pobaby.common.libs.baseview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.pobaby.common.utils.view.FontUtils;


/**
 * 主要设置字体
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2017/12/13 11:04
 */
public class XButton extends AppCompatButton {

    public XButton(Context context) {
        super(context);
    }

    public XButton(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.buttonStyle);
    }

    public XButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /***
     * 设置字体
     *
     * @return
     */
    public void init(Context context) {
        setTypeface(FontUtils.setFont(context));
    }
}
