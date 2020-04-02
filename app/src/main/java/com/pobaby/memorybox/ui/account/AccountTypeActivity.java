package com.pobaby.memorybox.ui.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pobaby.common.ui.base.BaseActivity;
import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.common.utils.view.SmartRefreshUtils;
import com.pobaby.common.utils.view.StatusBarUtils;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.app.AppConfig;
import com.pobaby.memorybox.ui.mvp.adapter.account.AccountTypeAdapter;
import com.pobaby.memorybox.ui.mvp.model.account.AccountTypeModel;
import com.pobaby.memorybox.ui.mvp.model.event.AccountTypeModifyEvent;
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

/**
 * 帐号类型管理界面
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/18 10:30
 */
public class AccountTypeActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSrlRoot)
    SmartRefreshLayout mSrlRoot;

    private Unbinder mUnbinder;
    private AccountTypeAdapter mAdapter;
    private List<AccountTypeModel> mTypes = new LinkedList<>();
    private int mPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);
        mUnbinder = ButterKnife.bind(this);
        StatusBarUtils.transparencyBar(this);
        setToolBarPadding();
        initView();

//        MainApplication.getDaoSession().deleteAll(AccountTypeModel.class);
    }

    private void initView() {

        initToolBarAsHome("帐号类型管理");
        setBackPressed(R.mipmap.ic_back);

        EventBus.getDefault().register(this);

        // mSrlRoot.setRefreshHeader(new ClassicsHeader(getContext()));
        // mSrlRoot.setRefreshFooter(new ClassicsFooter(getContext()));

        mSrlRoot.setRefreshHeader(SmartRefreshUtils.getBezierRadarHeader(getBaseContext()));
        mSrlRoot.setRefreshFooter(SmartRefreshUtils.getBallPulseFooter(getBaseContext()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new AccountTypeAdapter(mTypes);
        mRecyclerView.setAdapter(mAdapter);
        // 初始化事件
        initListener();
        // 初始化刷新
        mSrlRoot.autoRefresh();
    }

    private void initListener() {
        mSrlRoot.setOnRefreshListener(refreshLayout -> {
//            refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            mPage = 0;
            mTypes = AccountTypeModel.queryPage(mPage);
            refreshLayout.finishRefresh();
            refreshLayout.setEnableLoadMore(mTypes.size() == AppConfig.ROW_COUNT ? true : false);
            mAdapter.setNewData(mTypes);
        });
        mSrlRoot.setOnLoadMoreListener(refreshLayout -> {
//            refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            List<AccountTypeModel> modelList = AccountTypeModel.queryPage(++mPage);
            mTypes.addAll(modelList);
            refreshLayout.finishLoadMore();
            refreshLayout.setEnableLoadMore(modelList.size() == AppConfig.ROW_COUNT ? true : false);
            mAdapter.notifyItemRangeChanged(mTypes.size() - modelList.size(), modelList.size());
        });
        mAdapter.setCallBack(new AccountTypeAdapter.CallBack() {
            @Override
            public void onItemClick(AccountTypeModel model) {
                Intent intent = new Intent(AccountTypeActivity.this, AccountTypeModifyActivity.class);
                intent.putExtra("activity_mode", EnumValues.ActivityMode.MODE_MODIFY);
                intent.putExtra("account_type_model", model);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(AccountTypeModel model) {
                mAdapter.notifyItemRemoved(mTypes.indexOf(model));
                mTypes.remove(model);
                model.delete();
                // ToastUtils.show("删除成功");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.mIvAdd)
    public void onViewClicked() {
        Intent intent = new Intent(this, AccountTypeModifyActivity.class);
        intent.putExtra("activity_mode", EnumValues.ActivityMode.MODE_ADD);
        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountTypeModifyEvent(AccountTypeModifyEvent event) {
        if (event.getActivityMode() == EnumValues.ActivityMode.MODE_ADD) {
            mSrlRoot.autoRefresh();
        } else if (event.getActivityMode() == EnumValues.ActivityMode.MODE_MODIFY) {
            int index = -1;
            for (int n = 0; n < mTypes.size(); n++) {
                if (mTypes.get(n).getId() == event.getTypeModel().getId()) {
                    index = n;
                    break;
                }
            }
            if (index >= 0) {
                mTypes.set(index, event.getTypeModel());
                mAdapter.notifyItemChanged(index);
            }
        }
    }


}
