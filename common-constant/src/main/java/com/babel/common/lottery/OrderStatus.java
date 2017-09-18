package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: joey
 * Date: 2017/9/5
 * Time: 16:14
 * 用户注单状态
 */
public enum OrderStatus {

    lottery_success(1, "投注成功"),
    withdrawals(2, "用户撤单"),
    prize_no_win(31, "派奖-未中奖"),
    prize_win(32, "派奖-中奖"),
    exception(41, "存在异常,原因见备注"),
    prize_win_stop_chase(51, "中奖停追"),
    withdrawals_chase(52, "用户撤消追号"),
    system_withdrawals(6, "系统撤单,原因见备注");


    private static Map<Integer, OrderStatus> map = new HashMap<>();

    static {
        for (OrderStatus oj : OrderStatus.values()) {
            map.put(oj.code, oj);
        }
    }

    private int code;
    private String mes;

    OrderStatus(int code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public int code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public static OrderStatus parse(int code) {
        return map.get(code);
    }

}
