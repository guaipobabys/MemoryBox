package com.pobaby.memorybox.ui.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.pobaby.common.libs.baseview.XEditText;
import com.pobaby.common.ui.base.BaseActivity;
import com.pobaby.common.utils.common.DateTimeUtils;
import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.common.utils.view.StatusBarUtils;
import com.pobaby.common.utils.view.ToastUtils;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.ui.mvp.model.account.AccountTypeModel;
import com.pobaby.memorybox.ui.mvp.model.event.AccountTypeModifyEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 类型维护界面
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/21 9:47
 */
public class AccountTypeModifyActivity extends BaseActivity {

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

    private Unbinder mUnbinder;
    private AccountTypeModel mModel;
    private EnumValues.ActivityMode mActivityMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type_modify);
        mUnbinder = ButterKnife.bind(this);
        StatusBarUtils.transparencyBar(this);
        setToolBarPadding();
        initView();
    }

    protected void initView() {
        initToolBarAsHome("类型信息");
        setBackPressed(R.mipmap.ic_back);

        mActivityMode = (EnumValues.ActivityMode) getIntent().getSerializableExtra("activity_mode");
        switch (mActivityMode) {
            case MODE_ADD:
                mModel = new AccountTypeModel();
                mModel.setCreateTime(DateTimeUtils.Now.toString(DateTimeUtils.DateTimeType.ALL));
                setTitle("类型新增");
                break;
            case MODE_MODIFY:
                mModel = (AccountTypeModel) getIntent().getSerializableExtra("account_type_model");
                setTitle("类型编辑");
                break;
            case MODE_REONLY:
                mModel = (AccountTypeModel) getIntent().getSerializableExtra("account_type_model");
                setTitle("类型信息");
                mIvSave.setVisibility(View.GONE);
                mIvNameClear.setVisibility(View.GONE);
                mIvRemarkClear.setVisibility(View.GONE);
                break;
        }
        mXetName.setEnabled(mActivityMode == EnumValues.ActivityMode.MODE_REONLY ? false : true);
        mXetRemark.setEnabled(mActivityMode == EnumValues.ActivityMode.MODE_REONLY ? false : true);
        if (mModel == null) {
            new IllegalArgumentException(String.format("AccountTypeModel数据丢失"));
        }
        mXetName.setText(mModel.getName());
        mXetRemark.setText(mModel.getRemark());
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(mXetName.getText().toString())) {
            ToastUtils.show("请输入名称");
            return false;
        }
        /*if (TextUtils.isEmpty(mXetRemark.getText().toString())) {
            ToastUtils.show("请输入备注");
            return false;
        }*/
        return true;
    }

    @OnClick({R.id.mIvSave, R.id.mIvNameClear, R.id.mIvRemarkClear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mIvSave:
                if (!checkInput()) return;
                mModel.setName(mXetName.getText().toString());
                mModel.setRemark(mXetRemark.getText().toString());
                mModel.saveOrUpdate();
                ToastUtils.show("保存成功");
                EventBus.getDefault().post(new AccountTypeModifyEvent(mActivityMode, mModel));
                finish();
                break;
            case R.id.mIvNameClear:
                mXetName.setText("");
                break;
            case R.id.mIvRemarkClear:
                mXetRemark.setText("");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
