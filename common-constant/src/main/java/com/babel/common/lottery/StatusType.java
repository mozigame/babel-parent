package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 状态类型
 * created by roachjiang 2017/9/28
 */
public enum StatusType {
    ENABLED(1, "启用中"),
    DISABLED(0, "已停用");


    private static Map<Integer, StatusType> map = new HashMap<>();

    static {
        for (StatusType oj : StatusType.values()) {
            map.put(oj.code, oj);
        }
    }

    private int code;
    private String mes;

    StatusType(int code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public int code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public static StatusType parse(int code) {
        return map.get(code);
    }
}
