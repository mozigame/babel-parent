package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 彩种id
 * created by roachjiang 2017/9/28
 */
public enum LotteryIds {
//    CQSSC_1(1, "重庆时时彩-官彩"),//shi shi cai
    CQSSC_2(2, "重庆时时彩-双面彩"),
//    JC11X5_1(3, "江西11选5-官彩"),//Eleven select file
    JC11X5_2(4, "江西11选5-双面彩"),//Eleven select file
//    JSQ3_1(5, "江苏快3-官彩"),//quick 3
    JSQ3_2(6, "江苏快3-双面彩"),//quick 3
//    BJPK10_1(7, "北京PK10-官彩"),//beijing pk10
    BJPK10_2(8, "北京PK10-双面彩"),//beijing pk10
//    LHC(9, "香港六合彩"),
    XG_LHC(10, "香港六合彩-双面彩"),
//    tj_ssc_1(11, "香港六合彩-双面彩"),
    tj_ssc_2(12, "香港六合彩-双面彩"),
//    xj_ssc_1(13, "香港六合彩-双面彩"),
    xj_ssc_2(14, "香港六合彩-双面彩"),
//    gd_11x5_1(15, "香港六合彩-双面彩"),
    gd_11x5_2(16, "香港六合彩-双面彩"),
//    sd_11x5_1(17, "香港六合彩-双面彩"),
    sd_11x5_2(18, "香港六合彩-双面彩"),
//    ah_k3_1(19, "香港六合彩-双面彩"),
    ah_k3_2(20, "香港六合彩-双面彩"),
//    hub_k3_1(21, "香港六合彩-双面彩"),
    hub_k3_2(22, "香港六合彩-双面彩"),
    luckship(24, "香港六合彩-双面彩"),
//    bc_ssc_1(101, "秒速时时彩"),
    bc_ssc_2(102, "秒速时时彩双面彩"),
//    bc_11x5_1(103, "秒速11选5"),
    bc_11x5_2(104, "秒速11选5双面彩"),
//    bc_k3_1(105, "秒速快3"),
    bc_k3_2(106, "秒速快3双面彩"),
//    bc_pk10_1(107, "秒速赛车"),
    bc_pk10_2(108, "秒速赛车双面彩");


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
