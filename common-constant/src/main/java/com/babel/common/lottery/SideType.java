package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: joey
 * Date: 2017/9/27
 * Time: 16:53
 * 双面彩
 */
public enum SideType {

    official(1, "时时彩-官方彩"),
    dual(2, "时时彩-双面彩"),
    elevenFive_official(3, "11选5-官方彩"),
    elevenFive_dual(4, "11选5-双面彩");
	


    private int code;
    private String msg;

    private final static Map<Integer, SideType> map = new HashMap<>();
    static {
        for (SideType oj : SideType.values()) {
            map.put(oj.code, oj);
        }
    }
    SideType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }
    public String msg() {
        return msg;
    }

    public static SideType parse(int code) {
        return map.get(code);
    }

}
