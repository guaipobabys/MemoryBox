package com.pobaby.memorybox.ui.mvp.model.account;

import com.pobaby.common.utils.common.GsonUtils;

import org.greenrobot.greendao.annotation.Keep;

import java.util.List;

public class DataImportModel {

    private List<AccountImportModel> modelList;

    public List<AccountImportModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<AccountImportModel> modelList) {
        this.modelList = modelList;
    }

    @Keep
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
