package com.pobaby.memorybox.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.paraview.fingersdk.finger.utils.FingerSDK;
import com.paraview.fingersdk.finger.utils.FingerType;
import com.paraview.gestruresdk.utils.GestrureSDK;
import com.pobaby.common.libs.baseview.XTextView;
import com.pobaby.common.ui.base.BaseFragment;
import com.pobaby.common.utils.common.DateTimeUtils;
import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.common.utils.common.FileUtils;
import com.pobaby.common.utils.common.GsonUtils;
import com.pobaby.common.utils.common.PathUtils;
import com.pobaby.common.utils.view.PickerUtils;
import com.pobaby.common.utils.view.ToastUtils;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.app.MainApplication;
import com.pobaby.memorybox.ui.account.AccountActivity;
import com.pobaby.memorybox.ui.account.AccountImportActivity;
import com.pobaby.memorybox.ui.mvp.model.account.DataModel;
import com.pobaby.memorybox.ui.mvp.model.event.AccountTypeModifyEvent;
import com.pobaby.memorybox.ui.mvp.model.other.ConfigurationModel;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.FilePicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 主页-我的
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/16 15:39
 */
public class MainMineFragment extends BaseFragment {

    @BindView(R.id.mXtvSecurity)
    XTextView mXtvSecurity;
    @BindView(R.id.mXtvPwdRule)
    XTextView mXtvPwdRule;
    private Unbinder mUnbinder;


    private List<String> mOptSecuritys = Arrays.asList(
            EnumValues.SecurityType.手势验证.name(),
            EnumValues.SecurityType.指纹验证.name());

    private List<String> mOptPwdRules = Arrays.asList(
            EnumValues.PwdRuleType.大小写字母.name(),
            EnumValues.PwdRuleType.数字.name(),
            EnumValues.PwdRuleType.特殊字符.name(),
            EnumValues.PwdRuleType.大小写字母_数字.name(),
            EnumValues.PwdRuleType.大小写字母_特殊字符.name(),
            EnumValues.PwdRuleType.数字_特殊字符.name(),
            EnumValues.PwdRuleType.大小写字母_数字_特殊字符.name());

    private EnumValues.SecurityType mSecurityType = EnumValues.SecurityType.手势验证;
    private EnumValues.SecurityType mXtvSecurityTemp = EnumValues.SecurityType.手势验证;
    private EnumValues.PwdRuleType mPwdRuleType = EnumValues.PwdRuleType.大小写字母_数字;
    private ConfigurationModel mModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_mine, null);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        List<ConfigurationModel> models = ConfigurationModel.loadAll();
        if (models == null || models.size() == 0) {
            mModel = new ConfigurationModel();
            mModel.setSecurityType(mSecurityType.value);
            mModel.setPwdRuleType(mPwdRuleType.value);
            mModel.saveOrUpdate();
        } else {
            mModel = models.get(0);
            mSecurityType = EnumValues.SecurityType.values()[mModel.getSecurityType()];
            mPwdRuleType = EnumValues.PwdRuleType.values()[mModel.getPwdRuleType()];
        }
        mXtvSecurity.setText(mSecurityType.name());
        mXtvPwdRule.setText(mPwdRuleType.name());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick({R.id.mIvAvatar, R.id.mLlAccount, R.id.mLlCalendar, R.id.mLlSecurity,
            R.id.mLlPwdRule, R.id.mLlImport, R.id.mLlBackup, R.id.mLlRestore})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mIvAvatar:
                break;
            case R.id.mLlAccount:
                intent = new Intent(getContext(), AccountActivity.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);
                break;
            case R.id.mLlCalendar:
                break;
            case R.id.mLlSecurity:
                PickerUtils.createOptionPicker(getActivity(), mOptSecuritys, "选择密保安全方式", mSecurityType.value, new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {

                        mXtvSecurityTemp = EnumValues.SecurityType.valueOf(EnumValues.SecurityType.class, item);
                        if (mXtvSecurityTemp == EnumValues.SecurityType.手势验证) {
                            if (TextUtils.isEmpty(mModel.getGestrurePattern())) {
                                setGestrure();
                                return;
                            }
                        } else if (mXtvSecurityTemp == EnumValues.SecurityType.指纹验证) {
                            if (TextUtils.isEmpty(mModel.getGestrurePattern())) {
                                checkFinger("开启指纹验证", FingerType.FINGER_SET);
                                return;
                            }
                        }
                        mXtvSecurity.setText(mXtvSecurityTemp.name());
                        mSecurityType = mXtvSecurityTemp;
                        mModel.setSecurityType(mSecurityType.value);
                        mModel.saveOrUpdate();
                    }
                }).show();
                break;
            case R.id.mLlPwdRule:
                PickerUtils.createOptionPicker(getActivity(), mOptPwdRules, "选择密码规则方式", mPwdRuleType.value, new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        mXtvPwdRule.setText(item);
                        mPwdRuleType = EnumValues.PwdRuleType.valueOf(EnumValues.PwdRuleType.class, item);
                        mModel.setPwdRuleType(mPwdRuleType.value);
                        mModel.saveOrUpdate();

                    }
                }).show();
                break;
            case R.id.mLlImport:
                intent = new Intent(getContext(), AccountImportActivity.class);
                getActivity().startActivity(intent);
                break;

            case R.id.mLlBackup:
                backupAccount();
                break;
            case R.id.mLlRestore:
                restoreAccount();
                break;
            default:
                break;
        }
    }

    private void setGestrure() {
        GestrureSDK
                .from(getContext())
                .setGestrure(new GestrureSDK.OnSetGestrureCallBack() {
                    @Override
                    public void onCancel() {
                        // 取消了
                        ToastUtils.show("取消了");
                    }

                    @Override
                    public void onConfirmed(String pattern) {
                        // 设置成功
                        mXtvSecurity.setText(mXtvSecurityTemp.name());
                        mSecurityType = mXtvSecurityTemp;
                        mModel.setSecurityType(mSecurityType.value);
                        mModel.setGestrurePattern(pattern);
                        mModel.saveOrUpdate();
                        ToastUtils.show("设置成功");
                    }
                });
    }

    private void checkFinger(String title, FingerType type) {

        FingerSDK
                .from(getContext())
                .checkFinger(title, FingerType.FINGER_CONFIRM, new FingerSDK.OnCheckedFingerCallBack() {
                    @Override
                    public void onCancel() {
                        // TODO 取消了
                    }

                    @Override
                    public void onReLogin() {
                        // TODO 切换用户
                    }

                    @Override
                    public void onConfirmed(FingerType type) {
                        if (type == FingerType.FINGER_SET) {
                            // 设置成功
                            mXtvSecurity.setText(mXtvSecurityTemp.name());
                            mSecurityType = mXtvSecurityTemp;
                            mModel.setSecurityType(mSecurityType.value);
                            mModel.saveOrUpdate();
                            ToastUtils.show("设置成功");

                        } else if (type == FingerType.FINGER_CONFIRM) {
                            // TODO 取消成功
                        } else {
                            // TODO 验证成功
                        }
                    }
                });
    }

    private void backupAccount() {
        String jsonString = DataModel.getInstance().toString();
        String fileName = PathUtils.getCachePath() + DateTimeUtils.Now.toString(DateTimeUtils.DateTimeType.ALL) + " account.json";
        FileUtils.setStringToFile(fileName, jsonString);
        ToastUtils.show(String.format("成功备份到：%s", fileName));
    }

    private void restoreAccount() {

        filePickerByPath(currentPath -> {
            String jsonString = FileUtils.getStringFromFile(currentPath);
            if (TextUtils.isEmpty(jsonString)) {
                ToastUtils.show("恢复的文件读取错误");
                return;
            }
            DataModel dataModel = GsonUtils.fromJson(jsonString, DataModel.class);
            if (dataModel == null) {
                ToastUtils.show("恢复的文件解析错误");
                return;
            }
            if (dataModel.getTypeModelList() != null) {
                MainApplication.getDaoSession().getAccountTypeModelDao().insertOrReplaceInTx(dataModel.getTypeModelList());
            }
            if (dataModel.getModelList() != null) {
                MainApplication.getDaoSession().getAccountModelDao().insertOrReplaceInTx(dataModel.getModelList());
            }
            if (dataModel.getConfigModellList() != null) {
                MainApplication.getDaoSession().getConfigurationModelDao().insertOrReplaceInTx(dataModel.getConfigModellList());
            }
            EventBus.getDefault().post(new AccountTypeModifyEvent(EnumValues.ActivityMode.MODE_ADD, null));
            ToastUtils.show("恢复成功");
        });
    }

    /**
     * 选择文件
     *
     * @param pickListener 回调方法
     */
    public void filePickerByPath(FilePicker.OnFilePickListener pickListener) {
        FilePicker picker = new FilePicker(getActivity(), FilePicker.FILE);
        picker.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        picker.setTopBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        picker.setCancelTextColor(ContextCompat.getColor(getContext(), R.color.colorLabel));
        picker.setPressedTextColor(ContextCompat.getColor(getContext(), R.color.colorLabel));
        picker.setSubmitTextColor(ContextCompat.getColor(getContext(), R.color.colorLabel));
        picker.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.colorLabel));
        picker.setShowHideDir(false);
        picker.setRootPath(PathUtils.getCachePath());
//        picker.setRootPath(StorageUtils.getExternalRootPath());
        //picker.setAllowExtensions(new String[]{".apk"});
        picker.setFileIcon(getContext().getResources().getDrawable(android.R.drawable.ic_menu_agenda));
        picker.setFolderIcon(getContext().getResources().getDrawable(android.R.drawable.ic_menu_upload_you_tube));
        picker.setOnFilePickListener(pickListener);
        picker.show();
//        setBackgroundBlack(picker.getRootView());
    }

    private void setBackgroundBlack(ViewGroup viewRoot) {
        if (viewRoot != null) {
            for (int n = 0; n < viewRoot.getChildCount(); n++) {
                View view = viewRoot.getChildAt(n);
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                if (view instanceof ViewGroup) {
                    setBackgroundBlack((ViewGroup) view);
                }
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(ContextCompat.getColor(getContext(), R.color.colorLabel));
                }
            }
        }
    }
}
