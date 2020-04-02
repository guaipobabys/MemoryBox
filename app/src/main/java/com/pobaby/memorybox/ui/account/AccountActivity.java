package com.pobaby.memorybox.ui.account;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pobaby.common.ui.base.BaseActivity;
import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.common.utils.view.SmartRefreshUtils;
import com.pobaby.common.utils.view.StatusBarUtils;
import com.pobaby.common.utils.view.ToastUtils;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.app.AppConfig;
import com.pobaby.memorybox.ui.mvp.adapter.account.AccountAdapter;
import com.pobaby.memorybox.ui.mvp.model.account.AccountModel;
import com.pobaby.memorybox.ui.mvp.model.event.AccountModifyEvent;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.zhl.cbdialog.CBDialogBuilder.onDialogbtnClickListener.BUTTON_CONFIRM;

/**
 * 帐号管理界面
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/18 10:30
 */
public class AccountActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSrlRoot)
    SmartRefreshLayout mSrlRoot;

    private Unbinder mUnbinder;
    private AccountAdapter mAdapter;
    private List<AccountModel> mAccounts = new LinkedList<>();
    private AccountModel mCurrModel = null;
    private int mPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mUnbinder = ButterKnife.bind(this);
        StatusBarUtils.transparencyBar(this);
        setToolBarPadding();
        initView();
    }

    private void initView() {
        initToolBarAsHome("帐号管理");
        setBackPressed(R.mipmap.ic_back);
        EventBus.getDefault().register(this);

        // mSrlRoot.setRefreshHeader(new ClassicsHeader(getContext()));
        // mSrlRoot.setRefreshFooter(new ClassicsFooter(getContext()));

        mSrlRoot.setRefreshHeader(SmartRefreshUtils.getBezierRadarHeader(getBaseContext()));
        mSrlRoot.setRefreshFooter(SmartRefreshUtils.getBallPulseFooter(getBaseContext()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new AccountAdapter(mAccounts);
        mRecyclerView.setAdapter(mAdapter);
        // 初始化事件
        initListener();
        // 初始化刷新
        mSrlRoot.autoRefresh();
    }

    private void initListener() {
        mSrlRoot.setOnRefreshListener(refreshLayout -> {
//            refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败  0
            mPage = 0;
            mAccounts = AccountModel.queryPage(mPage);
            refreshLayout.finishRefresh();
            refreshLayout.setEnableLoadMore(mAccounts.size() == AppConfig.ROW_COUNT ? true : false);
            mAdapter.setNewData(mAccounts);
        });
        mSrlRoot.setOnLoadMoreListener(refreshLayout -> {
//            refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            List<AccountModel> modelList = AccountModel.queryPage(++mPage);
            mAccounts.addAll(modelList);
            refreshLayout.finishLoadMore();
            refreshLayout.setEnableLoadMore(modelList.size() == AppConfig.ROW_COUNT ? true : false);
            mAdapter.notifyItemRangeChanged(mAccounts.size() - modelList.size(), modelList.size());
        });

        mAdapter.setCallBack(new AccountAdapter.CallBack() {
            @Override
            public void onItemClick(AccountModel model) {
                // UPDATE viewCount
                mCurrModel = model;
                model.setReadCount(model.getReadCount() + 1);
                model.saveOrUpdate();
                mAdapter.notifyItemChanged(mAccounts.indexOf(model));

                Intent intent = new Intent(AccountActivity.this, AccountModifyActivity.class);
                intent.putExtra("activity_mode", EnumValues.ActivityMode.MODE_MODIFY);
                intent.putExtra("account_model", model);
                startActivity(intent);
            }

            @Override
            public void onCopeClick(AccountModel model) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("AccountModel", model.toPasteString());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtils.show("复制成功");
            }

            @Override
            public void onDeleteClick(AccountModel model) {
                showAskDialog("确认删除所选帐号信息吗？", true, (context, dialog, whichBtn) -> {
                    switch (whichBtn) {
                        case BUTTON_CONFIRM:
                            mAdapter.notifyItemRemoved(mAccounts.indexOf(model));
                            mAccounts.remove(model);
                            model.delete();
                            // ToastUtils.show("删除成功");
                            break;
                    }
                });
            }
        });
    }

    @OnClick({R.id.mIvAdd, R.id.mIvType})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mIvAdd:
                intent = new Intent(this, AccountModifyActivity.class);
                intent.putExtra("activity_mode", EnumValues.ActivityMode.MODE_ADD);
                startActivity(intent);
                break;
            case R.id.mIvType:
                intent = new Intent(this, AccountTypeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountModifyEvent(AccountModifyEvent event) {
        if (event.getActivityMode() == EnumValues.ActivityMode.MODE_ADD) {
            mSrlRoot.autoRefresh();
        } else if (event.getActivityMode() == EnumValues.ActivityMode.MODE_MODIFY) {
            int index = -1;
            for (int n = 0; n < mAccounts.size(); n++) {
                if (mAccounts.get(n).getId() == event.getAccountModel().getId()) {
                    index = n;
                    break;
                }
            }
            if (index >= 0) {
                mAccounts.set(index, event.getAccountModel());
                mAdapter.notifyItemChanged(index);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
