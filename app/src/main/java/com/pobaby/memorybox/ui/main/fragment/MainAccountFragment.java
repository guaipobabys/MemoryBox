package com.pobaby.memorybox.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.pobaby.common.libs.view.XViewPager;
import com.pobaby.common.ui.base.BaseFragment;
import com.pobaby.common.ui.mvp.adapter.BaseFragmentStatePagerAdapter;
import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.ui.account.AccountTabFragment;
import com.pobaby.memorybox.ui.mvp.model.account.AccountTypeModel;
import com.pobaby.memorybox.ui.mvp.model.event.AccountModifyEvent;
import com.pobaby.memorybox.ui.mvp.model.event.AccountTypeModifyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 主页-帐号
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/16 15:39
 */
public class MainAccountFragment extends BaseFragment {

    @BindView(R.id.mPstsAccount)
    PagerSlidingTabStrip mPstsAccount;
    @BindView(R.id.mXvpAccount)
    XViewPager mXvpAccount;

    private Unbinder mUnbinder;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    private List<AccountTypeModel> mTypes;
    private BaseFragmentStatePagerAdapter mPagerAdapter;
    private int mPageIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_account, null);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        mTypes = AccountTypeModel.loadAll();
        if (mTypes == null || mTypes.size() == 0) {
            return;
        }
        mTitles = new LinkedList<>();
        mFragments = new LinkedList<>();
        for (AccountTypeModel model : mTypes) {
            mTitles.add(model.getName());
            mFragments.add(createFragment(model));
        }
        mPagerAdapter = new BaseFragmentStatePagerAdapter(getChildFragmentManager(), mFragments, mTitles);
        mXvpAccount.setAdapter(mPagerAdapter);
        mPstsAccount.setViewPager(mXvpAccount);
        mXvpAccount.setOffscreenPageLimit(mTypes.size()); // 设置预加载为类型的总数
        mPagerAdapter.notifyDataSetChanged();

        if (mPageIndex < mFragments.size()) {
            mXvpAccount.setCurrentItem(mPageIndex);
        }
        initListener();
    }

    private void initListener() {
        mXvpAccount.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPageIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private Fragment createFragment(AccountTypeModel model) {
        Fragment fragment = new AccountTabFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("account_type_model", model);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountModifyEvent(AccountModifyEvent event) {
        ((AccountTabFragment) mFragments.get(mPageIndex)).onAccountModifyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountTypeModifyEvent(AccountTypeModifyEvent event) {
        if (event.getActivityMode() == EnumValues.ActivityMode.MODE_ADD
                || event.getActivityMode() == EnumValues.ActivityMode.MODE_MODIFY) {
            initView();
        }
    }
}
