package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 彩种类型
 * created by roachjiang 2017/9/28
 */
public enum LotteryType {
    SSC(1, "时时彩"),//shi shi cai
    ESF(2, "11选5"),//Eleven select file
    Q3(3, "快3"),//quick 3
    BJPK10(4, "北京PK10"),//beijing pk10
    SIX(6, "六合彩"),//six lottery
    PC_EGG(7, "PC蛋蛋"),
    Q8(8, "快8"),
    Q10(10, "快10");
	


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
