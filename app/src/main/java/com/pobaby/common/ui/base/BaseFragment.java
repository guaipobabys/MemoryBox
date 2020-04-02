package com.pobaby.common.ui.base;

import androidx.fragment.app.Fragment;

import com.zhl.cbdialog.CBDialogBuilder;

/**
 * Fragment基类
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/16 15:37
 */
public class BaseFragment extends Fragment {


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
        new CBDialogBuilder(getContext())
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
