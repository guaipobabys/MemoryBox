package com.pobaby.memorybox.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.paraview.fingersdk.finger.utils.FingerSDK;
import com.paraview.fingersdk.finger.utils.FingerType;
import com.paraview.gestruresdk.utils.GestrureSDK;
import com.paraview.gestruresdk.utils.GestrureType;
import com.pobaby.common.libs.view.XViewPager;
import com.pobaby.common.ui.base.BaseActivity;
import com.pobaby.common.ui.mvp.adapter.BaseFragmentStatePagerAdapter;
import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.common.utils.view.StatusBarUtils;
import com.pobaby.common.utils.view.ToastUtils;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.app.MainApplication;
import com.pobaby.memorybox.ui.account.AccountModifyActivity;
import com.pobaby.memorybox.ui.common.BaseSearchActivity;
import com.pobaby.memorybox.ui.main.fragment.MainAccountFragment;
import com.pobaby.memorybox.ui.main.fragment.MainCalendarFragment;
import com.pobaby.memorybox.ui.main.fragment.MainMineFragment;
import com.pobaby.memorybox.ui.mvp.model.other.ConfigurationModel;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;

/**
 * 主页-Activity
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/16 14:50
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.mXvpMain)
    XViewPager mXvpMain;
    @BindView(R.id.mPnvMain)
    PageNavigationView mPnvMain;
    @BindView(R.id.mIvSearch)
    ImageView mIvSearch;
    @BindView(R.id.mIvAdd)
    ImageView mIvAdd;

    private NavigationController mNavigationController;
    private List<Fragment> mFragments = new LinkedList<>();
    private List<String> mTitles = new LinkedList<>();

    private MainAccountFragment mAccountFragment;
    private MainCalendarFragment mCalendarFragment;
    private MainMineFragment mMineFragment;

    private ConfigurationModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarUtils.transparencyBar(this);
        setToolBarPadding();
        initView();
    }

    private void initView() {
        // 申请得到存储权限后初始化数据库配置
        MainApplication.initGreenDao();
        initToolBarAsHome("帐号");
        initPages();
        // ViewPager设置预加载页面的个数方法
        mXvpMain.setOffscreenPageLimit(3);
        mNavigationController = mPnvMain.custom()
                .addItem(getTabItem(R.mipmap.main_home, R.mipmap.main_home_selector, mTitles.get(0), false))
                .addItem(getTabItem(R.mipmap.main_calendar, R.mipmap.main_calendar_selector, mTitles.get(1), false))
                .addItem(getTabItem(R.mipmap.main_mine, R.mipmap.main_mine_selector, mTitles.get(2), false))
                .build();
        setupFragment();
        initListener();

        List<ConfigurationModel> models = ConfigurationModel.loadAll();
        if (models != null && models.size() > 0) {
            mModel = models.get(0);
            EnumValues.SecurityType securityType = EnumValues.SecurityType.values()[mModel.getSecurityType()];
            if (securityType == EnumValues.SecurityType.手势验证) {
                if (!TextUtils.isEmpty(mModel.getGestrurePattern())) {
                    checkGestrure(GestrureType.GESTRURE_CHECK);
                }
            } else if (securityType == EnumValues.SecurityType.指纹验证) {
                checkFinger("验证指纹登录", FingerType.FINGER_CHECK);
            }
        }
    }


    /**
     * 手势验证
     */
    private void checkGestrure(GestrureType type) {

        GestrureSDK
                .from(this)
                .checkGestrure(type
                        , mModel.getGestrurePattern(), new GestrureSDK.OnCheckedGestrureCallBack() {
                            @Override
                            public void onCancel() {
                                // 取消了
                                ToastUtils.show("取消了");
                                onBackPressed();
                                System.exit(0);
                            }

                            @Override
                            public void onReLogin() {
                                // 切换用户
                                checkFinger("验证指纹登录", FingerType.FINGER_CHECK);
                            }

                            @Override
                            public void onConfirmed(GestrureType type) {
                                if (type == GestrureType.GESTRURE_CONFIRM) {
                                    // 取消成功
                                } else {
                                    // 验证成功
                                    ToastUtils.show("验证成功");
                                }
                            }
                        });
    }


    /**
     * 指纹验证
     *
     * @param title
     * @param type
     */
    private void checkFinger(String title, FingerType type) {

        FingerSDK
                .from(this)
                .checkFinger(title, type, new FingerSDK.OnCheckedFingerCallBack() {
                    @Override
                    public void onCancel() {
                        // 取消了
                        ToastUtils.show("取消了");
                        onBackPressed();
                        System.exit(0);
                    }

                    @Override
                    public void onReLogin() {
                        // 切换用户
                        if (!TextUtils.isEmpty(mModel.getGestrurePattern())) {
                            checkGestrure(GestrureType.GESTRURE_CHECK);
                        } else {
                            ToastUtils.show("没有其他验证方式");
                        }
                    }

                    @Override
                    public void onConfirmed(FingerType type) {
                        if (type == FingerType.FINGER_SET) {
                            // 设置成功
                            ToastUtils.show("设置成功");
                        } else if (type == FingerType.FINGER_CONFIRM) {
                            // 取消成功
                            ToastUtils.show("取消成功");
                        } else {
                            // 验证成功
                            ToastUtils.show("验证成功");
                        }
                    }
                });
    }

    private void setupFragment() {
        mXvpMain.setAdapter(new BaseFragmentStatePagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
        //自动适配ViewPager页面切换
        mNavigationController.setupWithViewPager(mXvpMain);
    }

    private void initPages() {
        mFragments = new LinkedList<>();
        mAccountFragment = new MainAccountFragment();
        mCalendarFragment = new MainCalendarFragment();
        mMineFragment = new MainMineFragment();
        mFragments.add(mAccountFragment);
        mFragments.add(mCalendarFragment);
        mFragments.add(mMineFragment);

        mTitles = new LinkedList<>();
        mTitles.add("帐号");
        mTitles.add("日历");
        mTitles.add("我的");
    }

    /**
     * 创建一个新的TabItem
     *
     * @param drawableRes        默认的图标
     * @param checkedDrawableRes 选中时的图标
     * @param title              显示的标题
     * @param hasMessage         是否有消息
     * @return
     */
    private BaseTabItem getTabItem(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes, String title, boolean hasMessage) {
        NormalItemView niv = new NormalItemView(this);
        niv.initialize(drawableRes, checkedDrawableRes, title);
        niv.setTextDefaultColor(ContextCompat.getColor(this, R.color.colorSubtitle));
        niv.setTextCheckedColor(ContextCompat.getColor(this, R.color.colorLabel));
        niv.setHasMessage(hasMessage);
        return niv;
    }

    private void initListener() {

        mXvpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(mXvpMain.getAdapter().getPageTitle(position));
                // 如果是我的，则隐藏顶部菜单
                mIvSearch.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                mIvAdd.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.mIvSearch, R.id.mIvAdd})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mIvSearch:
                intent = new Intent(this, BaseSearchActivity.class);
                intent.putExtra("search_type", EnumValues.SearchType.帐号);
                startActivity(intent);
                break;
            case R.id.mIvAdd:
//                mAccountFragment.addRandomAccount();
                intent = new Intent(this, AccountModifyActivity.class);
                intent.putExtra("activity_mode", EnumValues.ActivityMode.MODE_ADD);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : mFragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
