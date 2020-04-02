package com.pobaby.memorybox.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.pobaby.common.libs.baseview.XEditText;
import com.pobaby.common.libs.baseview.XTextView;
import com.pobaby.common.ui.base.BaseActivity;
import com.pobaby.common.utils.common.DateTimeUtils;
import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.common.utils.common.RandomUtils;
import com.pobaby.common.utils.view.PickerUtils;
import com.pobaby.common.utils.view.StatusBarUtils;
import com.pobaby.common.utils.view.ToastUtils;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.app.MainApplication;
import com.pobaby.memorybox.ui.mvp.model.account.AccountModel;
import com.pobaby.memorybox.ui.mvp.model.account.AccountTypeModel;
import com.pobaby.memorybox.ui.mvp.model.event.AccountModifyEvent;
import com.pobaby.memorybox.ui.mvp.model.event.AccountTypeModifyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.OptionPicker;

import static com.zhl.cbdialog.CBDialogBuilder.onDialogbtnClickListener.BUTTON_CONFIRM;

/**
 * 帐号维护界面
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/21 9:47
 */
public class AccountModifyActivity extends BaseActivity {

    @BindView(R.id.mXetName)
    XEditText mXetName;
    @BindView(R.id.mXetRemark)
    XEditText mXetRemark;
    @BindView(R.id.mIvSave)
    ImageView mIvSave;
    @BindView(R.id.mIvNameClear)
    ImageView mIvNameClear;
    @BindView(R.id.mIvRemarkClear)
    ImageView mIvRemarkClear;
    @BindView(R.id.mXtvType)
    XTextView mXtvType;
    @BindView(R.id.mXetAccount)
    XEditText mXetAccount;
    @BindView(R.id.mXetPwd)
    XEditText mXetPwd;
    @BindView(R.id.mXtvCheck)
    XTextView mXtvCheck;
    @BindView(R.id.mLlType)
    LinearLayout mLlType;
    @BindView(R.id.mLlPwd)
    LinearLayout mLlPwd;
    @BindView(R.id.mXetAddress)
    XEditText mXetAddress;
    @BindView(R.id.mIvPwdView)
    ImageView mIvPwdView;

    private List<String> mOptChecks = Arrays.asList(EnumValues.CheckType.验证.name(), EnumValues.CheckType.不验证.name());
    private List<String> mOptTypes = null;
    private List<AccountTypeModel> mTypes = null;
    private Unbinder mUnbinder;
    private AccountModel mModel;
    private EnumValues.ActivityMode mActivityMode;
    private boolean mIsPwdShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_modify);
        mUnbinder = ButterKnife.bind(this);
        StatusBarUtils.transparencyBar(this);
        setToolBarPadding();
        initView();
    }

    protected void initView() {

        EventBus.getDefault().register(this);

        initToolBarAsHome("帐号信息");
        setBackPressed(R.mipmap.ic_back);

        mActivityMode = (EnumValues.ActivityMode) getIntent().getSerializableExtra("activity_mode");
        if (mActivityMode == null) {
            new IllegalArgumentException(String.format("ActivityMode数据丢失"));
        }
        switch (mActivityMode) {
            case MODE_ADD:
                mModel = new AccountModel();
                mModel.setCreateTime(DateTimeUtils.Now.toString(DateTimeUtils.DateTimeType.ALL));
                setTitle("帐号新增");
                break;
            case MODE_MODIFY:
                mModel = (AccountModel) getIntent().getSerializableExtra("account_model");
                setTitle("帐号编辑");
                break;
            case MODE_REONLY:
                mModel = (AccountModel) getIntent().getSerializableExtra("account_model");
                setTitle("帐号信息");
                mIvSave.setVisibility(View.GONE);
                mIvNameClear.setVisibility(View.GONE);
                mIvRemarkClear.setVisibility(View.GONE);
                break;
        }
        mXetName.setEnabled(mActivityMode != EnumValues.ActivityMode.MODE_REONLY);
        mLlType.setEnabled(mActivityMode != EnumValues.ActivityMode.MODE_REONLY);
        mLlPwd.setEnabled(mActivityMode != EnumValues.ActivityMode.MODE_REONLY);
        mXetRemark.setEnabled(mActivityMode != EnumValues.ActivityMode.MODE_REONLY);
        mXtvType.setEnabled(false);
        mXtvCheck.setEnabled(false);

        if (mModel == null) {
            new IllegalArgumentException(String.format("AccountModel数据丢失"));
        }
        loadType();

        mXetName.setText(mModel.getName());
        if (mActivityMode != EnumValues.ActivityMode.MODE_ADD) {
            mModel.__setDaoSession(MainApplication.getDaoSession());
            mXtvType.setText(mModel.getAccountType().getName());
        }
        mXtvCheck.setText(mOptChecks.get(mModel.getCheck() ? 0 : 1));
        mXetAddress.setText(mModel.getAddress());
        mXetAccount.setText(mModel.getAccount());
        mXetPwd.setText(mModel.getPassword());
        mXetRemark.setText(mModel.getRemark());
    }

    private void loadType() {
        mTypes = AccountTypeModel.loadAll();
        if (mTypes == null || mTypes.size() == 0) {
            showAskDialog("还没有帐号类型，立即新建？", true, (context, dialog, whichBtn) -> {
                switch (whichBtn) {
                    case BUTTON_CONFIRM:
                        Intent intent = new Intent(AccountModifyActivity.this, AccountTypeModifyActivity.class);
                        intent.putExtra("activity_mode", EnumValues.ActivityMode.MODE_ADD);
                        startActivity(intent);
                        break;
                }
            });
        } else {
            mOptTypes = new LinkedList<>();
            for (AccountTypeModel model : mTypes) {
                mOptTypes.add(model.getName());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountTypeModifyEvent(AccountTypeModifyEvent event) {
        if (event.getActivityMode() == EnumValues.ActivityMode.MODE_ADD
                || event.getActivityMode() == EnumValues.ActivityMode.MODE_MODIFY) {
            loadType();
        }
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(mXetName.getText().toString())) {
            ToastUtils.show("请输入名称");
            return false;
        }
        if (TextUtils.isEmpty(mXtvType.getText().toString())) {
            ToastUtils.show("请选择类型");
            return false;
        }
        if (TextUtils.isEmpty(mXetAccount.getText().toString())) {
            ToastUtils.show("请输入帐号");
            return false;
        }
        if (TextUtils.isEmpty(mXetPwd.getText().toString())) {
            ToastUtils.show("请输入密码");
            return false;
        }
        return true;
    }

    @OnClick({R.id.mIvSave, R.id.mLlType, R.id.mIvAccountClear, R.id.mIvAddressClear, R.id.mIvPwdView,
            R.id.mIvPwdRandom, R.id.mIvPwdClear, R.id.mLlCheck, R.id.mIvNameClear, R.id.mIvRemarkClear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mIvSave:
                if (!checkInput()) return;
                mModel.setName(mXetName.getText().toString());
                mModel.setAddress(mXetAddress.getText().toString());
                mModel.setAccount(Objects.requireNonNull(mXetAccount.getText()).toString());
                mModel.setPassword(mXetPwd.getText().toString());
                mModel.setRemark(mXetRemark.getText().toString());
                mModel.saveOrUpdate();
                ToastUtils.show("保存成功");
                EventBus.getDefault().post(new AccountModifyEvent(mActivityMode, mModel));
                finish();
                break;
            case R.id.mLlType:
                if (mOptTypes != null) {
                    int index = mOptChecks.indexOf(mXtvType.getText().toString());
                    if (index == -1) index = 0;
                    PickerUtils.createOptionPicker(this, mOptTypes, "选择帐号类型", index, new OptionPicker.OnOptionPickListener() {
                        @Override
                        public void onOptionPicked(int index, String item) {
                            mXtvType.setText(item);
                            mModel.setTypeId(mTypes.get(index).getId());
                        }
                    }).show();
                }
                break;
            case R.id.mIvPwdView:
                mXetPwd.setTransformationMethod(mIsPwdShow ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
                mIsPwdShow = !mIsPwdShow;
                mIvPwdView.setImageResource(mIsPwdShow ? R.mipmap.ic_eye_close : R.mipmap.ic_eye_open);
                break;
            case R.id.mIvPwdRandom:
                mXetPwd.setText(RandomUtils.genRandomString(16));
                break;
            case R.id.mLlCheck:
                int index = mModel.getCheck() ? 0 : 1;
                PickerUtils.createOptionPicker(this, mOptChecks, "选择验证方式", index, new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        mXtvCheck.setText(item);
                        mModel.setCheck(index == 0);
                    }
                }).show();
                break;
            case R.id.mIvNameClear:
                mXetName.setText("");
                break;
            case R.id.mIvAccountClear:
                mXetAccount.setText("");
                break;
            case R.id.mIvAddressClear:
                mXetAddress.setText("");
                break;
            case R.id.mIvRemarkClear:
                mXetRemark.setText("");
                break;
            case R.id.mIvPwdClear:
                mXetPwd.setText("");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
