package com.pobaby.common.ui.mvp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * ViewPager+FragementStatePagerAdapter可以通过setAdapter做到整体刷新,
 * 不然数据更新导致Fragment成员变量数据丢失 getContext null 等问题
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/16 15:31
 */
public class BaseFragmentStatePagerAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private List<T> fragments;
    private List<String> titles;

    public BaseFragmentStatePagerAdapter(FragmentManager fragmentManager, List<T> fragments) {
        this(fragmentManager, fragments, new LinkedList<>());
    }

    public BaseFragmentStatePagerAdapter(FragmentManager fragmentManager, List<T> fragments, List<String> titles) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
