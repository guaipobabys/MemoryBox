package com.pobaby.memorybox.ui.mvp.model.account;


import com.pobaby.common.ui.mvp.base.model.BaseModel;
import com.pobaby.common.utils.common.GsonUtils;
import com.pobaby.memorybox.app.AppConfig;
import com.pobaby.memorybox.app.MainApplication;
import com.pobaby.memorybox.db.AccountModelDao;
import com.pobaby.memorybox.db.AccountTypeModelDao;
import com.pobaby.memorybox.db.DaoSession;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 帐号导入模型类
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/11/6 13:30
 */
public class AccountImportModel extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String typeName;

    private boolean check;

    private String address;

    private String account;

    private String password;

    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Keep
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
