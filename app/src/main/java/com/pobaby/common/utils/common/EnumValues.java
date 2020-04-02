package com.pobaby.common.utils.common;


/**
 * 枚举
 *
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2017/12/18 11:34
 */
public class EnumValues {

    /**
     * 窗体模式
     * MODE_ADD(0), MODE_MODIFY(1), MODE_REONLY(2);
     */
    public enum ActivityMode {
        MODE_ADD(0), MODE_MODIFY(1), MODE_REONLY(2);
        public int value;

        ActivityMode(int value) {
            this.value = value;
        }
    }

    /**
     * 验证类型
     * 验证(0), 不验证(1);
     */
    public enum CheckType {
        验证(0), 不验证(1);
        public int value;

        CheckType(int value) {
            this.value = value;
        }
    }

    /**
     * 验证类型
     * 手势验证(0), 指纹验证(1);
     */
    public enum SecurityType {
        手势验证(0), 指纹验证(1);
        public int value;

        SecurityType(int value) {
            this.value = value;
        }
    }

    /**
     * 密码规则类型
     * <p>
     * 大小写字母(0),
     * 数字(1),
     * 特殊字符(2),
     * 大小写字母_数字(3),
     * 大小写字母_特殊字符(4),
     * 数字_特殊字符(5),
     * 大小写字母_数字_特殊字符(7);
     */
    public enum PwdRuleType {
        大小写字母(0),
        数字(1),
        特殊字符(2),
        大小写字母_数字(3),
        大小写字母_特殊字符(4),
        数字_特殊字符(5),
        大小写字母_数字_特殊字符(6);
        public int value;

        PwdRuleType(int value) {
            this.value = value;
        }
    }

    /**
     * 搜索内容类型 1:帐号，2:帐号类型
     */
    public enum SearchType {
        无(0),
        帐号(1),
        帐号类型(2);

        public int value;

        SearchType(int value) {
            this.value = value;
        }
    }

}
