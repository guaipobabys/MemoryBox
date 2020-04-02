package com.pobaby.memorybox.ui.mvp.model.event;

import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.memorybox.ui.mvp.model.account.AccountModel;

/**
 * 帐号维护事件
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/25 15:09
 */
public class AccountModifyEvent {

    public AccountModifyEvent() {

    }

    public AccountModifyEvent(EnumValues.ActivityMode activityMode, AccountModel accountModel) {
        this.activityMode = activityMode;
        this.accountModel = accountModel;
    }

    private EnumValues.ActivityMode activityMode;

    private AccountModel accountModel;

    public AccountModel getAccountModel() {
        return accountModel;
    }

    public void setAccountModel(AccountModel accountModel) {
        this.accountModel = accountModel;
    }

    public EnumValues.ActivityMode getActivityMode() {
        return activityMode;
    }

    public void setActivityMode(EnumValues.ActivityMode activityMode) {
        this.activityMode = activityMode;
    }
}
