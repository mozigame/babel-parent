package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 追号类型
 * created by roachjiang 2017/9/28
 */
public enum ChaseType {
    YES(1, "追号"),
    YES_PARENT(2, "追号主数据");


    private static Map<Integer, ChaseType> map = new HashMap<>();

    static {
        for (ChaseType oj : ChaseType.values()) {
            map.put(oj.code, oj);
        }
    }

    private int code;
    private String mes;

    ChaseType(int code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public int code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public static ChaseType parse(int code) {
        return map.get(code);
    }
}
