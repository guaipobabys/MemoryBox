package com.pobaby.memorybox.ui.account;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.pobaby.common.libs.baseview.XEditText;
import com.pobaby.common.ui.base.BaseActivity;
import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.common.utils.common.GsonUtils;
import com.pobaby.common.utils.view.StatusBarUtils;
import com.pobaby.common.utils.view.ToastUtils;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.app.AppConfig;
import com.pobaby.memorybox.app.MainApplication;
import com.pobaby.memorybox.ui.mvp.model.account.AccountImportModel;
import com.pobaby.memorybox.ui.mvp.model.account.AccountModel;
import com.pobaby.memorybox.ui.mvp.model.account.DataImportModel;
import com.pobaby.memorybox.ui.mvp.model.event.AccountModifyEvent;
import com.pobaby.memorybox.ui.mvp.model.event.AccountTypeModifyEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 导入Json格式的帐号
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/11/6 10:01
 */
public class AccountImportActivity extends BaseActivity {

    @BindView(R.id.mXetDemo)
    XEditText mXetDemo;
    @BindView(R.id.mXetAccount)
    XEditText mXetAccount;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_import);
        mUnbinder = ButterKnife.bind(this);
        StatusBarUtils.transparencyBar(this);
        setToolBarPadding();
        initView();
    }

    protected void initView() {

        initToolBarAsHome("帐号导入");
        setBackPressed(R.mipmap.ic_back);

        mXetDemo.setText(AppConfig.ACCOUNT_IMPORT_EXAMPLE);
    }


    private boolean checkInput() {
        if (TextUtils.isEmpty(mXetAccount.getText().toString())) {
            ToastUtils.show(mXetAccount.getHint().toString());
            return false;
        }
        return true;
    }

    @OnClick({R.id.mIvSave, R.id.mIvCope, R.id.mIvPaste, R.id.mIvAccountClear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mIvSave:
                importAccount();
                break;
            case R.id.mIvCope:
                copeDemoString();
                break;
            case R.id.mIvPaste:
                pasteAccount();
                break;
            case R.id.mIvAccountClear:
                mXetAccount.setText("");
                break;
        }
    }

    private void importAccount() {
        if (!checkInput()) return;
        String jsonString = mXetAccount.getText().toString();
        DataImportModel dataImportModel = GsonUtils.fromJson(jsonString, DataImportModel.class);
        if (dataImportModel == null || dataImportModel.getModelList() == null || dataImportModel.getModelList().size() == 0) {
            ToastUtils.show("导入的帐号信息解析失败");
            return;
        }
        List<AccountModel> accountModelList = new LinkedList<>();
        for (AccountImportModel importModel : dataImportModel.getModelList()) {
            accountModelList.add(AccountModel.parse(importModel));
        }
        MainApplication.getDaoSession().getAccountModelDao().insertOrReplaceInTx(accountModelList);
        // 消息传递刷新类型
        EventBus.getDefault().post(new AccountTypeModifyEvent(EnumValues.ActivityMode.MODE_ADD, null));
        // 消息传递刷新帐号
        EventBus.getDefault().post(new AccountModifyEvent(EnumValues.ActivityMode.MODE_ADD, null));
        mXetAccount.setText("");
        ToastUtils.show("导入成功");
    }

    private void copeDemoString() {
        ClipboardManager cm;//获取剪贴板管理器：
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("AccountModel", mXetDemo.getText());
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtils.show("复制成功");
    }

    private void pasteAccount() {
        ClipboardManager cm;//获取剪贴板管理器：
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将剪贴板放到文本框。
        if (cm.getPrimaryClip().getItemCount() > 0) {
            mXetAccount.setText(cm.getPrimaryClip().getItemAt(0).getText());
        }
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
