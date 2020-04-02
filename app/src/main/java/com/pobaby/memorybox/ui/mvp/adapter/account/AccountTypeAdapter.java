package com.pobaby.memorybox.ui.mvp.adapter.account;

import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.ui.mvp.model.account.AccountModel;
import com.pobaby.memorybox.ui.mvp.model.account.AccountTypeModel;

import java.util.List;

/**
 * 帐号类型列表适配器
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/17 15:04
 */
public class AccountTypeAdapter extends BaseQuickAdapter<AccountTypeModel, BaseViewHolder> {

    public AccountTypeAdapter(@Nullable List<AccountTypeModel> data) {
        super(R.layout.adapter_item_account_type, data);
    }

    private CallBack callBack;

    public interface CallBack {

        void onItemClick(AccountTypeModel model);

        void onDeleteClick(AccountTypeModel model);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void convert(BaseViewHolder helper, AccountTypeModel item) {
        ((SwipeMenuLayout) helper.itemView).setSwipeEnable(true);

        helper.setText(R.id.mXtvName, item.getName());
        helper.setText(R.id.mXtvRemark, item.getRemark());
        if (callBack != null) {
            helper.getView(R.id.mIvDelete).setOnClickListener(view ->
                    callBack.onDeleteClick(item)
            );
            helper.getView(R.id.mIvEdit).setOnClickListener(view ->
                    callBack.onItemClick(item)
            );
            helper.getView(R.id.mLlRoot).setOnClickListener(view ->
                    callBack.onItemClick(item)
            );
        }
    }
}
