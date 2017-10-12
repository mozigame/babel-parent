package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用中/冻结
 * created by roachjiang 2017/9/28
 */
public enum StatusLock {
    ENABLED(1, "启用中"),
    LOCKED(0, "已冻结");


    private static Map<Integer, StatusLock> map = new HashMap<>();

    static {
        for (StatusLock oj : StatusLock.values()) {
            map.put(oj.code, oj);
        }
    }

    private int code;
    private String mes;

    StatusLock(int code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public int code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public static StatusLock parse(int code) {
        return map.get(code);
    }
}
