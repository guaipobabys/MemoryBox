package com.pobaby.memorybox.app;

/**
 * 存放缓存数据的Key值
 *
 * @author chenqh
 * created at 2019/6/13 17:40
 */
public interface KeyConfig {
    String KEY_TOKEN_INFO = "key_token_info"; // 用户Token信息
    String KEY_USER_INFO = "key_user_info"; // 用户信息
    String KEY_USER_STEP = "key_user_step"; // 用户步数
    String KEY_USER_SIGN = "key_user_sign"; // 用户签名，用途在经期信息同步位置
    String KEY_SYSTEM_PARAM_INFO = "key_system_param_info"; // 系统配置信息
    String KEY_VERSION_INFO = "key_version_info"; // 版本信息
    String KEY_MENSTRUATION_INFO = "key_menstruation_info"; // 经期信息
    String KEY_DELIVERY_INFO = "key_delivery_info"; // 分娩期信息
    String KEY_TEMP_PREGNANT_STATUS = "key_temp_pregnant_status"; // 怀孕状态临时信息
    String KEY_PREGNANT_STATUS = "key_pregnant_status"; // 怀孕状态信息
    String KEY_TEMP_SEX_STATUS = "key_temp_sex_status"; // 性别状态信息
    String KEY_SEX_STATUS = "key_sex_status"; // 性别状态信息
}
