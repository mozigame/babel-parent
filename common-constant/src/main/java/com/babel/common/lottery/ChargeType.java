package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: joey
 * Date: 2017/9/7
 * Time: 19:37
 * 账变类型
 */

public enum ChargeType {

    bet(10001, "投注"),
    bet_reforward_point(10002, "投注返点"),
    payoff(10003, "派奖"),
    chase_bet(10004, "追号投注"),
    withdrawal_cashback(10005, "撤单返款"),
    withdrawal_reforward_point(10006, "撤销返点"),
    withdrawal_payoff(10007, "撤销派奖"),
    deposit(10008, "转入"),
    withdrawing(10009, "转出"),
    withdrawal_cancel_fee(10010, "撤单手续费");

    private static Map<Integer, ChargeType> map = new HashMap<>();

    static {
        for (ChargeType oj : ChargeType.values()) {
            map.put(oj.code , oj);
        }
    }

    private int code;
    private String msg;

    ChargeType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    public static ChargeType parse(int code) {
        return map.get(code);
    }

}
