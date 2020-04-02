package com.paraview.gestruresdk.utils;

/**
 * 图案类型
 *
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2018/12/12 22:32
 */
public enum GestrureType {

    GESTRURE_NONE(0),
    GESTRURE_CONFIRM(1),
    GESTRURE_CHECK(2),
    GESTRURE_NO_PWD_AUTHORIZE(3);

    public int value;

    GestrureType(int value) {
        this.value = value;
    }
}