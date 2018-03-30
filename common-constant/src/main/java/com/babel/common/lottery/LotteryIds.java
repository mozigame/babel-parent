package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 彩种id
 * created by roachjiang 2017/9/28
 */
public enum LotteryIds {
    CQSSC_1(1, "cq_ssc", LotteryType.SSC.code(), 1, "重庆时时彩"),//shi shi cai
    CQSSC_2(2, "cq_ssc", LotteryType.SSC.code(), 2, "重庆时时彩双面彩"),
    JC11X5_1(3, "jx_11x5", LotteryType.ESF.code(), 1, "江西11选5"),//Eleven select file
    JC11X5_2(4, "jx_11x5", LotteryType.ESF.code(), 2, "江西11选5双面彩"),//Eleven select file
    JSQ3_1(5, "js_k3", LotteryType.Q3.code(), 1, "江苏快3"),//quick 3
    JSQ3_2(6, "js_k3", LotteryType.Q3.code(), 2, "江苏快3双面彩"),//quick 3
    BJPK10_1(7, "bj_pk10", LotteryType.BJPK10.code(), 1, "北京PK10"),//beijing pk10
    BJPK10_2(8, "bj_pk10", LotteryType.BJPK10.code(), 2, "北京PK10双面彩"),//beijing pk10
    XG_LHC_1(9, "xg_lhc", LotteryType.SIX.code(), 1,  "香港六合彩"),
    XG_LHC_2(10, "xg_lhc", LotteryType.SIX.code(), 2, "香港六合彩双面彩"),
    tj_ssc_1(11, "tj_ssc", LotteryType.SSC.code(), 1, "天津时时彩"),
    tj_ssc_2(12, "tj_ssc", LotteryType.SSC.code(), 2, "天津时时彩双面彩"),
    xj_ssc_1(13, "xj_ssc", LotteryType.SSC.code(), 1, "新疆时时彩"),
    xj_ssc_2(14, "xj_ssc", LotteryType.SSC.code(), 2, "新疆时时彩双面彩"),
    gd_11x5_1(15, "gd_11x5", LotteryType.ESF.code(), 1, "广东11选5"),
    gd_11x5_2(16, "gd_11x5", LotteryType.ESF.code(), 2, "广东11选5双面彩"),
    sd_11x5_1(17, "sd_11x5", LotteryType.ESF.code(), 1, "山东11选5"),
    sd_11x5_2(18, "sd_11x5", LotteryType.ESF.code(), 2, "山东11选5双面彩"),
    ah_k3_1(19, "ah_k3", LotteryType.Q3.code(), 1, "安徽快3"),
    ah_k3_2(20, "ah_k3", LotteryType.Q3.code(), 2, "安徽快3双面彩"),
    hub_k3_1(21, "hub_k3", LotteryType.Q3.code(), 1, "湖北快3"),
    hub_k3_2(22, "hub_k3", LotteryType.Q3.code(), 2, "湖北快3双面彩"),
    luckship_1(23, "luckship", LotteryType.BJPK10.code(), 1, "幸运飞艇"),
    luckship_2(24, "luckship", LotteryType.BJPK10.code(), 2, "幸运飞艇双面彩"),
    bj_ssc_1(25, "bj_ssc", LotteryType.SSC.code(), 1, "北京时时彩"),
    bj_ssc_2(26, "bj_ssc", LotteryType.SSC.code(), 2, "北京时时彩双面彩"),
    tw_ssc_1(27, "tw_ssc", LotteryType.SSC.code(), 1, "台湾5分彩"),
    tw_ssc_2(28, "tw_ssc", LotteryType.SSC.code(), 2, "台湾5分彩双面彩"),
    pc_dd_1(29, "pc_dd", LotteryType.PC_EGG.code(), 1, "PC蛋蛋"),
    pc_dd_2(30, "pc_dd", LotteryType.PC_EGG.code(), 2, "PC蛋蛋双面彩"),
    qq_ffc_1(31, "qq_ffc", LotteryType.SSC.code(), 1, "QQ分分彩"),
    qq_ffc_2(32, "qq_ffc", LotteryType.SSC.code(), 2, "QQ分分彩双面彩"),
    bc_ssc_1(101, "bc_ssc", LotteryType.SSC.code(), 1, "秒速时时彩"),
    bc_ssc_2(102, "bc_ssc", LotteryType.SSC.code(), 2, "秒速时时彩双面彩"),
    bc_11x5_1(103, "bc_11x5", LotteryType.ESF.code(), 1, "秒速11选5"),
    bc_11x5_2(104, "bc_11x5", LotteryType.ESF.code(), 2, "秒速11选5双面彩"),
    bc_k3_1(105, "bc_k3", LotteryType.Q3.code(), 1, "秒速快3"),
    bc_k3_2(106, "bc_k3", LotteryType.Q3.code(), 2, "秒速快3双面彩"),
    bc_pk10_1(107, "bc_pk10", LotteryType.BJPK10.code(), 1, "秒速赛车"),
    bc_pk10_2(108, "bc_pk10", LotteryType.BJPK10.code(), 2, "秒速赛车双面彩"),
    bc_lhc_1(109, "bc_lhc", LotteryType.SIX.code(), 1, "五分六合彩"),
    bc_lhc_2(110, "bc_lhc", LotteryType.SIX.code(), 2, "五分六合彩双面彩"),
    bc_ssc_korea_1(111, "bc_ssc_korea", LotteryType.SSC.code(), 1, "韩国1.5分彩"),
    bc_ssc_korea_2(112, "bc_ssc_korea", LotteryType.SSC.code(), 2, "韩国1.5分彩双面彩"),
    bc_ssc_tokyo_1(113, "bc_ssc_tokyo", LotteryType.SSC.code(), 1, "东京1.5分彩"),
    bc_ssc_tokyo_2(114, "bc_ssc_tokyo", LotteryType.SSC.code(), 2, "东京1.5分彩双面彩"),
    bc_ssc_chongqing_1(115, "bc_ssc_chongqing", LotteryType.SSC.code(), 1, "重庆秒秒彩"),
    bc_ssc_chongqing_2(116, "bc_ssc_chongqing", LotteryType.SSC.code(), 2, "重庆秒秒彩双面彩");


    private static Map<Integer, LotteryIds> map = new HashMap<>();

    static {
        for (LotteryIds oj : LotteryIds.values()) {
            map.put(oj.code, oj);
        }
    }

    private int code;
    private String lotteryCode;
    private int lotteryType;
    private int sideType;
    private String mes;

    LotteryIds(int code, String lotteryCode, int lotteryType, int sideType, String mes) {
        this.code = code;
        this.lotteryCode=lotteryCode;
        this.lotteryType=lotteryType;
        this.sideType=sideType;
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
    public int lotteryType(){
        return lotteryType;
    }
    public int sideType(){
        return sideType;
    }

    public static LotteryIds parse(int code) {
        return map.get(code);
    }
}
