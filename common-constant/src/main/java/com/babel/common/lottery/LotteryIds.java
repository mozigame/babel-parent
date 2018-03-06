package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 彩种id
 * created by roachjiang 2017/9/28
 */
public enum LotteryIds {
    CQSSC_1(1, "cq_ssc", "重庆时时彩"),//shi shi cai
    CQSSC_2(2, "cq_ssc", "重庆时时彩双面彩"),
    JC11X5_1(3, "jx_11x5", "江西11选5"),//Eleven select file
    JC11X5_2(4, "jx_11x5", "江西11选5双面彩"),//Eleven select file
    JSQ3_1(5, "js_k3", "江苏快3"),//quick 3
    JSQ3_2(6, "js_k3", "江苏快3双面彩"),//quick 3
    BJPK10_1(7, "bj_pk10", "北京PK10"),//beijing pk10
    BJPK10_2(8, "bj_pk10", "北京PK10双面彩"),//beijing pk10
    LHC(9, "xg_lhc", "香港六合彩"),
    XG_LHC(10, "xg_lhc", "香港六合彩双面彩"),
    tj_ssc_1(11, "tj_ssc", "天津时时彩"),
    tj_ssc_2(12, "tj_ssc", "天津时时彩双面彩"),
    xj_ssc_1(13, "xj_ssc", "新疆时时彩"),
    xj_ssc_2(14, "xj_ssc", "新疆时时彩双面彩"),
    gd_11x5_1(15, "gd_11x5", "广东11选5"),
    gd_11x5_2(16, "gd_11x5", "广东11选5双面彩"),
    sd_11x5_1(17, "sd_11x5", "山东11选5"),
    sd_11x5_2(18, "sd_11x5", "山东11选5双面彩"),
    ah_k3_1(19, "ah_k3", "安徽快3"),
    ah_k3_2(20, "ah_k3", "安徽快3双面彩"),
    hub_k3_1(21, "hub_k3", "湖北快3"),
    hub_k3_2(22, "hub_k3", "湖北快3双面彩"),
    luckship_1(23, "luckship", "幸运飞艇"),
    luckship_2(24, "luckship", "幸运飞艇双面彩"),
    bc_ssc_1(101, "bc_ssc", "秒速时时彩"),
    bc_ssc_2(102, "bc_ssc", "秒速时时彩双面彩"),
    bc_11x5_1(103, "bc_11x5", "秒速11选5"),
    bc_11x5_2(104, "bc_11x5", "秒速11选5双面彩"),
    bc_k3_1(105, "bc_k3", "秒速快3"),
    bc_k3_2(106, "bc_k3", "秒速快3双面彩"),
    bc_pk10_1(107, "bc_pk10", "秒速赛车"),
    bc_pk10_2(108, "bc_pk10", "秒速赛车双面彩"),
    bc_lhc_1(109, "bc_lhc", "五分六合彩"),
    bc_lhc_2(110, "bc_lhc", "五分六合彩双面彩"),
    bc_ssc_korea_1(111, "bc_ssc_korea", "韩国1.5分彩"),
    bc_ssc_korea_2(112, "bc_ssc_korea", "韩国1.5分彩双面彩"),
    bc_ssc_tokyo_1(113, "bc_ssc_tokyo", "东京1.5分彩"),
    bc_ssc_tokyo_2(114, "bc_ssc_tokyo", "东京1.5分彩双面彩");



    private static Map<Integer, LotteryIds> map = new HashMap<>();

    static {
        for (LotteryIds oj : LotteryIds.values()) {
            map.put(oj.code, oj);
        }
    }

    private int code;
    private String lotteryCode;
    private String mes;

    LotteryIds(int code, String lotteryCode, String mes) {
        this.code = code;
        this.lotteryCode=lotteryCode;
        this.mes = mes;
    }

    public int code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public String getLotteryCode(){
        return lotteryCode;
    }

    public static LotteryIds parse(int code) {
        return map.get(code);
    }
}
