package com.pobaby.memorybox.ui.account;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pobaby.common.libs.baseview.XEditText;
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
import com.pobaby.memorybox.ui.mvp.model.event.SearchHistoryEvent;
import com.pobaby.memorybox.ui.mvp.model.other.SearchHistoryModel;
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
 * 帐号搜索结果界面
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/11/29 14:19
 */
public class AccountSearchResultActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSrlRoot)
    SmartRefreshLayout mSrlRoot;
    @BindView(R.id.mXetSearch)
    XEditText mXetSearch;

    private Unbinder mUnbinder;
    private AccountAdapter mAdapter;
    private List<AccountModel> mAccounts = new LinkedList<>();
    private int mPage = 0;
    private String mKeyWord = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_search_result);
        mUnbinder = ButterKnife.bind(this);
        StatusBarUtils.transparencyBar(this);
        setToolBarPadding();
        initView();
    }

    private void initView() {
        initToolBarAsHome("");
        setBackPressed(R.mipmap.ic_back);

        mKeyWord = getIntent().getStringExtra("key_word");
        mXetSearch.setText(mKeyWord);

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

        mXetSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                toSearch();
                return true;
            }
            return false;
        });

        mSrlRoot.setOnRefreshListener(refreshLayout -> {
//            refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败  0
            mPage = 0;
            mAccounts = AccountModel.queryPageByKeyWord(mPage, mKeyWord);
            refreshLayout.finishRefresh();
            refreshLayout.setEnableLoadMore(mAccounts.size() == AppConfig.ROW_COUNT);
            mAdapter.setmKeyWord(mKeyWord);
            mAdapter.setNewData(mAccounts);
        });
        mSrlRoot.setOnLoadMoreListener(refreshLayout -> {
//            refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            List<AccountModel> modelList = AccountModel.queryPageByKeyWord(++mPage, mKeyWord);
            mAccounts.addAll(modelList);
            refreshLayout.finishLoadMore();
            refreshLayout.setEnableLoadMore(modelList.size() == AppConfig.ROW_COUNT);
            mAdapter.setmKeyWord(mKeyWord);
            mAdapter.notifyItemRangeChanged(mAccounts.size() - modelList.size(), modelList.size());
        });

        mAdapter.setCallBack(new AccountAdapter.CallBack() {
            @Override
            public void onItemClick(AccountModel model) {
                // UPDATE viewCount
                model.setReadCount(model.getReadCount() + 1);
                model.saveOrUpdate();
                mAdapter.notifyItemChanged(mAccounts.indexOf(model));

                Intent intent = new Intent(AccountSearchResultActivity.this, AccountModifyActivity.class);
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

    private void toSearch() {
        mKeyWord = mXetSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(mKeyWord)) {
            SearchHistoryModel.saveOrUpdate(EnumValues.SearchType.帐号, mKeyWord);
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            mSrlRoot.autoRefresh();
            EventBus.getDefault().post(new SearchHistoryEvent());
        } else {
            ToastUtils.show("请输入关键词");
        }
    }

    @OnClick(R.id.mIvSearch)
    public void onViewClicked() {
        toSearch();
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
