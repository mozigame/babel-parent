package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 彩种id
 * created by roachjiang 2017/9/28
 */
public enum LotteryIds {
    CQSSC_1(1, "重庆时时彩-官彩"),//shi shi cai
    CQSSC_2(2, "重庆时时彩-双面彩"),
    JC11X5_1(3, "11选5-官彩"),//Eleven select file
    JC11X5_2(4, "11选5-双面彩"),//Eleven select file
    JSQ3_1(5, "快3-官彩"),//quick 3
    JSQ3_2(6, "快3-双面彩"),//quick 3
    BJPK10_1(7, "北京PK10-官彩"),//beijing pk10
    BJPK10_2(8, "北京PK10-双面彩");//beijing pk10
//    SIX(6, "六合彩");//six lottery
	


    private static Map<Integer, LotteryIds> map = new HashMap<>();

    static {
        for (LotteryIds oj : LotteryIds.values()) {
            map.put(oj.code, oj);
        }
    }

    private int code;
    private String mes;

    LotteryIds(int code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public int code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public static LotteryIds parse(int code) {
        return map.get(code);
    }
}
