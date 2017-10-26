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
    bet_success(1, "等待开奖"),
    prize_no_win(31, "未中奖"),
    prize_win(32, "已派彩"),
    tied(33, "和局"),
    withdrawals(4, "用户撤单"),
    system_withdrawals(5, "系统撤单"),
    prize_win_stop_chase(6, "中奖停追"),
    exception(71, "存在异常"),
    exception_deal(81, "异常处理中");


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
