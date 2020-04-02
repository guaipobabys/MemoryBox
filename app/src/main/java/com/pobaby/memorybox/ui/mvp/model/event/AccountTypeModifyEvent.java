package com.pobaby.memorybox.ui.mvp.model.event;

import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.memorybox.ui.mvp.model.account.AccountTypeModel;

/**
 * 帐号类型维护事件
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/25 15:09
 */
public class AccountTypeModifyEvent {

    public AccountTypeModifyEvent() {

    }

    public AccountTypeModifyEvent(EnumValues.ActivityMode activityMode, AccountTypeModel typeModel) {
        this.activityMode = activityMode;
        this.typeModel = typeModel;
    }

    private EnumValues.ActivityMode activityMode;

    private AccountTypeModel typeModel;

    public AccountTypeModel getTypeModel() {
        return typeModel;
    }

    public void setTypeModel(AccountTypeModel typeModel) {
        this.typeModel = typeModel;
    }

    public EnumValues.ActivityMode getActivityMode() {
        return activityMode;
    }

    public void setActivityMode(EnumValues.ActivityMode activityMode) {
        this.activityMode = activityMode;
    }
}
