package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 删除类型
 * created by roachjiang 2017/9/28
 */
public enum IfDel {
    DEL(1, "已删除"),
    NO_DEL(0, "未删除");


    private static Map<Integer, IfDel> map = new HashMap<>();

    static {
        for (IfDel oj : IfDel.values()) {
            map.put(oj.code, oj);
        }
    }

    private int code;
    private String mes;

    IfDel(int code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public int code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public static IfDel parse(int code) {
        return map.get(code);
    }
}
