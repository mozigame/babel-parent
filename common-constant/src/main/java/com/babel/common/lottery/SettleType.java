package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: joey
 * Date: 2017/9/27
 * Time: 16:48
 * 信用方案
 */
public enum SettleType {

    cash(1, "现金方案"),
    credit(2,"信用方案");

    private int code;
    private String msg;

    private final static Map<Integer, SettleType> map = new HashMap<>();
    static {
        for (SettleType oj : SettleType.values()) {
            map.put(oj.code, oj);
        }
    }
    SettleType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }
    public String msg() {
        return msg;
    }

    public static SettleType parse(int code) {
        return map.get(code);
    }




}
