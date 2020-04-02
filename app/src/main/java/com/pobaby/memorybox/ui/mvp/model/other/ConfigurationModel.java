package com.pobaby.memorybox.ui.mvp.model.other;

import com.pobaby.common.ui.mvp.base.model.BaseModel;
import com.pobaby.common.utils.common.GsonUtils;
import com.pobaby.memorybox.app.MainApplication;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

import java.io.Serializable;
import java.util.List;

/**
 * 系统配置数据模型
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/29 11:03
 */
@Entity
public class ConfigurationModel extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long id;

    /**
     * 密保验证方式
     */
    private int securityType;

    /**
     * 秘密规则类型
     */
    private int pwdRuleType;

    /**
     * 手势
     */
    private String gestrurePattern;

    @Generated(hash = 569380960)
    public ConfigurationModel(Long id, int securityType, int pwdRuleType,
            String gestrurePattern) {
        this.id = id;
        this.securityType = securityType;
        this.pwdRuleType = pwdRuleType;
        this.gestrurePattern = gestrurePattern;
    }

    @Generated(hash = 1474767525)
    public ConfigurationModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSecurityType() {
        return this.securityType;
    }

    public void setSecurityType(int securityType) {
        this.securityType = securityType;
    }

    public int getPwdRuleType() {
        return this.pwdRuleType;
    }

    public void setPwdRuleType(int pwdRuleType) {
        this.pwdRuleType = pwdRuleType;
    }

    public String getGestrurePattern() {
        return this.gestrurePattern;
    }

    public void setGestrurePattern(String gestrurePattern) {
        this.gestrurePattern = gestrurePattern;
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
    public static List<ConfigurationModel> loadAll() {
        return MainApplication.getDaoSession().loadAll(ConfigurationModel.class);
    }

    @Keep
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }

}
