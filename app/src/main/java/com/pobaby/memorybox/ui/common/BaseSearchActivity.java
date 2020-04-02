package com.pobaby.memorybox.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pobaby.common.libs.baseview.XEditText;
import com.pobaby.common.libs.view.XFlowLayout;
import com.pobaby.common.ui.base.BaseActivity;
import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.common.utils.view.StatusBarUtils;
import com.pobaby.common.utils.view.ToastUtils;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.ui.account.AccountSearchResultActivity;
import com.pobaby.memorybox.ui.mvp.model.event.SearchHistoryEvent;
import com.pobaby.memorybox.ui.mvp.model.other.SearchHistoryModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 基础搜索
 * 搜索内容类型 1:帐号，2:帐号类型
 *
 * @author chenqh
 * created at 2019/7/6 15:56
 */
public class BaseSearchActivity extends BaseActivity {

    @BindView(R.id.mXetSearch)
    XEditText mXetSearch;
    @BindView(R.id.mFlHistory)
    XFlowLayout mFlHistory;
    @BindView(R.id.mLlHistory)
    LinearLayout mLlHistory;

    private Unbinder mUnbinder;
    private List<SearchHistoryModel> mModels = new ArrayList<>();
    private EnumValues.SearchType mSearchType = EnumValues.SearchType.无;
    private String mKeyWord = "";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_search_common);
        mUnbinder = ButterKnife.bind(this);
        StatusBarUtils.transparencyBar(this);
        setToolBarPadding();
        initView();
    }

    private void initView() {
        // 标题栏
        initToolBarAsHome("");
        setBackPressed(R.mipmap.ic_back);
        EventBus.getDefault().register(this);

        mSearchType = (EnumValues.SearchType) getIntent().getSerializableExtra("search_type");
        listener();
        loadHistroyData();

    }

    private void listener() {

        mXetSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchByKeyword();
                return true;
            }
            return false;
        });
    }

    private void loadHistroyData() {
        mModels = SearchHistoryModel.loadAll(mSearchType);
        if (mModels != null && mModels.size() > 0) {
            mLlHistory.setVisibility(View.VISIBLE);
            setHistoryUI();
        } else {
            mLlHistory.setVisibility(View.GONE);
        }
    }

    private void searchByKeyword() {
        mKeyWord = mXetSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(mKeyWord)) {
            toSearchResult(mKeyWord);
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            loadHistroyData();
        } else {
            ToastUtils.show("请输入关键词");
        }
    }

    private void setHistoryUI() {
        mFlHistory.removeAllViews();
        for (int i = 0; i < mModels.size(); i++) {
            TextView tv = (TextView) LayoutInflater.from(this).inflate(
                    R.layout.item_search_history, mFlHistory, false);
            tv.setBackgroundResource(R.drawable.shape_searchbar_bg);
            tv.setText(mModels.get(i).getKeyWord());
            final String str = tv.getText().toString();
            //点击事件
            tv.setOnClickListener(v -> toSearchResult(str));
            mFlHistory.addView(tv);//添加到父View
        }

    }

    private void toSearchResult(String keyWord) {
        SearchHistoryModel.saveOrUpdate(mSearchType, keyWord);

        Intent intent = new Intent();
        if (mSearchType == EnumValues.SearchType.帐号) {
            intent.setClass(getBaseContext(), AccountSearchResultActivity.class);
        }
        intent.putExtra("key_word", keyWord);
        startActivity(intent);
    }

    @OnClick({R.id.mIvSearch, R.id.mXtvClear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mIvSearch:
                searchByKeyword();
                break;
            case R.id.mXtvClear:
                SearchHistoryModel.deleteAll(mSearchType);
                loadHistroyData();
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchHistoryEvent(SearchHistoryEvent event) {
        loadHistroyData();
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
