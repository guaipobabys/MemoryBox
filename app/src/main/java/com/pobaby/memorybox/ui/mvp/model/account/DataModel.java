package com.pobaby.memorybox.ui.mvp.model.account;

import com.pobaby.common.utils.common.GsonUtils;
import com.pobaby.memorybox.app.MainApplication;
import com.pobaby.memorybox.ui.mvp.model.other.ConfigurationModel;

import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

public class DataModel {

    private List<AccountTypeModel> typeModelList;
    private List<AccountModel> modelList;
    private List<ConfigurationModel> configModellList;

    public List<AccountTypeModel> getTypeModelList() {
        return typeModelList;
    }

    public void setTypeModelList(List<AccountTypeModel> typeModelList) {
        this.typeModelList = typeModelList;
    }

    public List<AccountModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<AccountModel> modelList) {
        this.modelList = modelList;
    }

    public List<ConfigurationModel> getConfigModellList() {
        return configModellList;
    }

    public void setConfigModellList(List<ConfigurationModel> configModellList) {
        this.configModellList = configModellList;
    }

    public static DataModel getInstance() {
        DataModel dataModel = new DataModel();
        dataModel.setTypeModelList(AccountTypeModel.loadAll());
        dataModel.setModelList(MainApplication.getDaoSession().loadAll(AccountModel.class));
        dataModel.setConfigModellList(ConfigurationModel.loadAll());
        return dataModel;
    }

    @Keep
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
