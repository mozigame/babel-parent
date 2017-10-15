package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 钱款单位类型
 * @author Justin
 *
 */
public enum MoneyMode {
    YUAN('y', "元"),
    JIAO('j', "角"),
    FEN('f', "分");


    private static Map<Character, MoneyMode> map = new HashMap<>();

    static {
        for (MoneyMode oj : MoneyMode.values()) {
            map.put(oj.code, oj);
        }
    }

    private char code;
    private String mes;

    MoneyMode(char code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public int code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public static MoneyMode parse(int code) {
        return map.get(code);
    }

}
