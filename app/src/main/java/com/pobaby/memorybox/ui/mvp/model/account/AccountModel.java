package com.pobaby.memorybox.ui.mvp.model.account;


import android.text.TextUtils;

import com.pobaby.common.ui.mvp.base.model.BaseModel;
import com.pobaby.common.utils.common.DateTimeUtils;
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
 * 帐号模型类
 *
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2018/5/15 10:36
 */
@Entity
public class AccountModel extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private Long typeId;

    private boolean check;

    private String address;

    private String account;

    private String password;

    private int readCount;

    private String remark;

    private String createTime;

    @ToOne(joinProperty = "typeId")
    private AccountTypeModel accountType;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 563317894)
    private transient AccountModelDao myDao;

    @Generated(hash = 1716833771)
    private transient Long accountType__resolvedKey;

    @Generated(hash = 1991789201)
    public AccountModel(Long id, String name, Long typeId, boolean check, String address, String account, String password,
                        int readCount, String remark, String createTime) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.check = check;
        this.address = address;
        this.account = account;
        this.password = password;
        this.readCount = readCount;
        this.remark = remark;
        this.createTime = createTime;
    }

    @Generated(hash = 202444181)
    public AccountModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public boolean getCheck() {
        return this.check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getReadCount() {
        return this.readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 487366830)
    public AccountTypeModel getAccountType() {
        Long __key = this.typeId;
        if (accountType__resolvedKey == null || !accountType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AccountTypeModelDao targetDao = daoSession.getAccountTypeModelDao();
            AccountTypeModel accountTypeNew = targetDao.load(__key);
            synchronized (this) {
                accountType = accountTypeNew;
                accountType__resolvedKey = __key;
            }
        }
        return accountType;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 25176323)
    public void setAccountType(AccountTypeModel accountType) {
        synchronized (this) {
            this.accountType = accountType;
            typeId = accountType == null ? null : accountType.getId();
            accountType__resolvedKey = typeId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1363003361)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAccountModelDao() : null;
    }


    @Keep
    public long saveOrUpdate() {
        return MainApplication.getDaoSession().insertOrReplace(this);
    }

    @Keep
    public void delete() {
        // 删除
        MainApplication.getDaoSession().delete(this);
    }

    @Keep
    public static List<AccountModel> queryPage(int page) {
        return queryPage(page, 0L, "");
    }

    @Keep
    public static List<AccountModel> queryPageByKeyWord(int page, String keyWord) {
        return queryPage(page, 0L, keyWord);
    }

    @Keep
    public static List<AccountModel> queryPage(int page, Long typeId) {
        return queryPage(page, typeId, "");
    }

    @Keep
    public static List<AccountModel> queryPage(int page, Long typeId, String keyWord) {
        QueryBuilder<AccountModel> queryBuilder = MainApplication.getDaoSession().queryBuilder(AccountModel.class)
                .limit(AppConfig.ROW_COUNT).offset(AppConfig.ROW_COUNT * page)
                .orderDesc(AccountModelDao.Properties.CreateTime);
        if (typeId != 0) {
            queryBuilder.where(AccountModelDao.Properties.TypeId.eq(typeId));
        }
        if (!TextUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
            queryBuilder.whereOr(
                    AccountModelDao.Properties.Name.like(keyWord),
                    AccountModelDao.Properties.Account.like(keyWord),
                    AccountModelDao.Properties.Password.like(keyWord),
                    AccountModelDao.Properties.Address.like(keyWord),
                    AccountModelDao.Properties.Remark.like(keyWord));
        }
        return queryBuilder.list();
    }

    @Keep
    public static AccountModel parse(AccountImportModel importModel) {
        AccountModel model = new AccountModel();
        QueryBuilder<AccountModel> queryBuilder = MainApplication.getDaoSession().queryBuilder(AccountModel.class);
        queryBuilder.where(AccountModelDao.Properties.Name.eq(importModel.getName()));
        List<AccountModel> modelList = queryBuilder.list();
        if (modelList != null && modelList.size() > 0) {
            model = modelList.get(0);
        }
        model.setName(importModel.getName());
        model.setTypeId(AccountTypeModel.getIdByName(importModel.getTypeName()));
        model.setAccount(importModel.getAccount());
        model.setPassword(importModel.getPassword());
        model.setAddress(importModel.getAddress());
        model.setRemark(importModel.getRemark());
        model.setCheck(importModel.isCheck());
        model.setCreateTime(DateTimeUtils.Now.toString(DateTimeUtils.DateTimeType.ALL));
        return model;
    }

    @Keep
    public String toPasteString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("名称：%s\n", this.name));
        sb.append(String.format("地址：%s\n", this.address));
        sb.append(String.format("帐号：%s\n", this.account));
        sb.append(String.format("密码：%s\n", this.password));
        sb.append(String.format("备注：%s", this.remark));
        return sb.toString();
    }

    @Keep
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
