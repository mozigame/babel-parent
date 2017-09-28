package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * created by roachjiang 2017/9/28
 */
public enum ChaseStatus {
    PROCESSING(1, "进行中"),
    END(2, "已结束"),
    END_FOR_WIN(3, "已终止");


    private static Map<Integer, ChaseStatus> map = new HashMap<>();

    static {
        for (ChaseStatus oj : ChaseStatus.values()) {
            map.put(oj.code, oj);
        }
    }

    private int code;
    private String mes;

    ChaseStatus(int code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public int code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public static ChaseStatus parse(int code) {
        return map.get(code);
    }
}
