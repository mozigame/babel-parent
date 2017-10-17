package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 追号类型
 * created by roachjiang 2017/9/28
 */
public enum LotteryType {
    SSC(1, "时时彩"),//shi shi cai
    ESF(2, "11选5"),//Eleven select file
    SIX(3, "六合彩");//six lottery


    private static Map<Integer, LotteryType> map = new HashMap<>();

    static {
        for (LotteryType oj : LotteryType.values()) {
            map.put(oj.code, oj);
        }
    }

    private int code;
    private String mes;

    LotteryType(int code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public int code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public static LotteryType parse(int code) {
        return map.get(code);
    }
}
