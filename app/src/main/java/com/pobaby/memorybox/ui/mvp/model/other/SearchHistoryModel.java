package com.pobaby.memorybox.ui.mvp.model.other;


import com.pobaby.common.ui.mvp.base.model.BaseModel;
import com.pobaby.common.utils.common.EnumValues;
import com.pobaby.common.utils.common.GsonUtils;
import com.pobaby.memorybox.app.MainApplication;
import com.pobaby.memorybox.db.SearchHistoryModelDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索历史数据模型
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/11/27 15:37
 */
@Entity
public class SearchHistoryModel extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * 类型
     */
    private int type;

    /**
     * 关键词
     */
    private String keyWord;


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Generated(hash = 1151426635)
    public SearchHistoryModel(String id, int type, String keyWord) {
        this.id = id;
        this.type = type;
        this.keyWord = keyWord;
    }

    @Generated(hash = 2050687136)
    public SearchHistoryModel() {
    }

    @Keep
    public static void saveOrUpdate(EnumValues.SearchType type, String keyWord) {
        SearchHistoryModel searchHistoryModel = new SearchHistoryModel();
        searchHistoryModel.setId(keyWord + type.value);
        searchHistoryModel.setKeyWord(keyWord);
        searchHistoryModel.setType(type.value);
        searchHistoryModel.saveOrUpdate();
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
    public static void deleteAll(EnumValues.SearchType searchType) {
        QueryBuilder<SearchHistoryModel> queryBuilder = MainApplication.getDaoSession().queryBuilder(SearchHistoryModel.class);
        queryBuilder.where(SearchHistoryModelDao.Properties.Type.eq(searchType.value));
        queryBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Keep
    public static List<SearchHistoryModel> loadAll(EnumValues.SearchType searchType) {
        QueryBuilder<SearchHistoryModel> queryBuilder = MainApplication.getDaoSession().queryBuilder(SearchHistoryModel.class);
        queryBuilder.where(SearchHistoryModelDao.Properties.Type.eq(searchType.value));
        return queryBuilder.list();
    }

    @Keep
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }

}
