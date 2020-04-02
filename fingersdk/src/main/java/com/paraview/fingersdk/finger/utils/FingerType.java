package com.paraview.fingersdk.finger.utils;

/**
 * 指纹类型
 *
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2018/12/12 22:32
 */
public enum FingerType {

    FINGER_NONE(0),
    FINGER_SET(1),
    FINGER_CHECK(2),
    FINGER_CONFIRM(3),
    FINGER_NO_PWD_AUTHORIZE(4);

    public int value;

    FingerType(int value) {
        this.value = value;
    }
}