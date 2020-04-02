package com.pobaby.memorybox.ui.mvp.adapter.account;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.pobaby.common.utils.common.KeyWordUtils;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.app.MainApplication;
import com.pobaby.memorybox.ui.mvp.model.account.AccountModel;

import java.util.List;

/**
 * 帐号列表适配器
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/17 15:04
 */
public class AccountAdapter extends BaseQuickAdapter<AccountModel, BaseViewHolder> {

    public AccountAdapter(@Nullable List<AccountModel> data) {
        super(R.layout.adapter_item_account, data);
    }

    private CallBack callBack;
    private boolean isSwipeEnable = true;
    private String mKeyWord;

    public interface CallBack {

        void onItemClick(AccountModel model);

        void onCopeClick(AccountModel model);

        void onDeleteClick(AccountModel model);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void setmKeyWord(String keyWord) {
        this.mKeyWord = keyWord;
    }

    /**
     * 设置侧滑功能开关
     * 默认打开
     *
     * @param swipeEnable
     */
    public void setSwipeEnable(boolean swipeEnable) {
        this.isSwipeEnable = swipeEnable;
    }

    @Override
    protected void convert(BaseViewHolder helper, AccountModel item) {
        ((SwipeMenuLayout) helper.itemView).setSwipeEnable(isSwipeEnable);

        // 这里需要把一对一关系的Dao设置，不然报错“Entity is detached from DAO context”
        item.__setDaoSession(MainApplication.getDaoSession());
        if (item.getAccountType() != null) {
            helper.setText(R.id.mXtvTypeName, "【" + item.getAccountType().getName() + "】");
        } else {
            helper.setText(R.id.mXtvTypeName, "");
        }
        helper.setText(R.id.mXtvName, KeyWordUtils.setKeyWordColor(item.getName(), mKeyWord));
        helper.setText(R.id.mXtvAccount, KeyWordUtils.setKeyWordColor(item.getAccount(), mKeyWord));
        helper.setText(R.id.mXtvAddress, KeyWordUtils.setKeyWordColor(item.getAddress(), mKeyWord));
        helper.setText(R.id.mXtvRemark, KeyWordUtils.setKeyWordColor(item.getRemark(), mKeyWord));
        helper.setText(R.id.mXtvViewCount, String.valueOf(item.getReadCount()));
        helper.getView(R.id.mIvCheck).setVisibility(item.getCheck() ? View.VISIBLE : View.GONE);
        helper.getView(R.id.mXtvRemark).setVisibility(!TextUtils.isEmpty(item.getRemark()) ? View.VISIBLE : View.GONE);

        if (callBack != null) {
            helper.getView(R.id.mIvDelete).setOnClickListener(view ->
                    callBack.onDeleteClick(item)
            );
            helper.getView(R.id.mIvEdit).setOnClickListener(view ->
                    callBack.onItemClick(item)
            );
            helper.getView(R.id.mIvCope).setOnClickListener(view ->
                    callBack.onCopeClick(item)
            );
            helper.getView(R.id.mLlRoot).setOnClickListener(view ->
                    callBack.onItemClick(item)
            );
        }
    }
}
