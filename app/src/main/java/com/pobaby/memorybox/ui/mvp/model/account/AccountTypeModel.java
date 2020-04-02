package com.pobaby.memorybox.ui.mvp.model.account;


import com.pobaby.common.ui.mvp.base.model.BaseModel;
import com.pobaby.common.utils.common.DateTimeUtils;
import com.pobaby.common.utils.common.GsonUtils;
import com.pobaby.memorybox.app.AppConfig;
import com.pobaby.memorybox.app.MainApplication;
import com.pobaby.memorybox.db.AccountTypeModelDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
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
public class AccountTypeModel extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private int sort;

    private String remark;

    private String createTime;

    @Generated(hash = 76572128)
    public AccountTypeModel(Long id, String name, int sort, String remark,
                            String createTime) {
        this.id = id;
        this.name = name;
        this.sort = sort;
        this.remark = remark;
        this.createTime = createTime;
    }

    @Generated(hash = 1323524835)
    public AccountTypeModel() {
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

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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

    @Keep
    public long saveOrUpdate() {
        if (this.id == null || this.id == 0) {
            QueryBuilder<AccountTypeModel> queryBuilder = MainApplication.getDaoSession().queryBuilder(AccountTypeModel.class);
            queryBuilder.orderAsc(AccountTypeModelDao.Properties.Sort);
            List<AccountTypeModel> modelList = queryBuilder.list();
            this.sort = modelList.size() + 1;
        }
        return MainApplication.getDaoSession().insertOrReplace(this);
    }

    @Keep
    public void delete() {
        // 查询大于当前排序的记录并都-1
        QueryBuilder<AccountTypeModel> queryBuilder = MainApplication.getDaoSession().queryBuilder(AccountTypeModel.class);
        queryBuilder.where(AccountTypeModelDao.Properties.Sort.gt(this.sort));
        queryBuilder.orderAsc(AccountTypeModelDao.Properties.Sort);
        List<AccountTypeModel> modelList = queryBuilder.list();
        for (AccountTypeModel model : modelList) {
            model.sort = model.sort - 1;
            MainApplication.getDaoSession().insertOrReplace(model);
        }
        // 删除
        MainApplication.getDaoSession().delete(this);
    }

    @Keep
    public static List<AccountTypeModel> queryPage(int page) {
        QueryBuilder<AccountTypeModel> queryBuilder = MainApplication.getDaoSession().queryBuilder(AccountTypeModel.class);
        queryBuilder.limit(AppConfig.ROW_COUNT).offset(AppConfig.ROW_COUNT * page);
        queryBuilder.orderAsc(AccountTypeModelDao.Properties.Sort);
        return queryBuilder.list();
    }

    /**
     * 根据名称查询类型
     * 若没有则新增该类型
     *
     * @param name
     * @return 返回类型id
     */
    @Keep
    public static long getIdByName(String name) {
        AccountTypeModel model = new AccountTypeModel();
        QueryBuilder<AccountTypeModel> queryBuilder = MainApplication.getDaoSession().queryBuilder(AccountTypeModel.class);
        queryBuilder.where(AccountTypeModelDao.Properties.Name.eq(name));
        List<AccountTypeModel> modelList = queryBuilder.list();
        if (modelList != null && modelList.size() > 0) {
            return modelList.get(0).getId();
        }
        model.setName(name);
        model.setCreateTime(DateTimeUtils.Now.toString(DateTimeUtils.DateTimeType.ALL));
        return model.saveOrUpdate();
    }

    @Keep
    public static List<AccountTypeModel> loadAll() {
        QueryBuilder<AccountTypeModel> queryBuilder = MainApplication.getDaoSession().queryBuilder(AccountTypeModel.class);
        queryBuilder.orderAsc(AccountTypeModelDao.Properties.Sort);
        return queryBuilder.list();
    }

    @Keep
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
