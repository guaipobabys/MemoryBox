package com.pobaby.common.libs.baseview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.pobaby.common.utils.view.FontUtils;


/**
 * 主要设置字体
 *
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2017/12/12 10:55
 */
public class XTextView extends AppCompatTextView {


    public XTextView(Context context) {
        this(context, null);
    }

    public XTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public XTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
