package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: joey
 * Date: 2017/9/7
 * Time: 15:33
 * 注单操作类型
 */
public enum OrderOperType {


    lot_order(1, "下注"),
    user_withdrawals(2, "用户撤单"),
    system_withdrawals(3, "系统撤单"),
    system_payoff(4, "系统派彩"),
    other(5, "其他");

    private static Map<Integer, OrderOperType> map = new HashMap<>();

    static {
        for (OrderOperType oj : OrderOperType.values()) {
            map.put(oj.code, oj);
        }
    }


    OrderOperType(int code, String mes) {
        this.code = code;
        this.mes = mes;
    }


    private int code;
    private String mes;

    public int code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public static OrderOperType parse(int code) {
        return map.get(code);
    }
}
