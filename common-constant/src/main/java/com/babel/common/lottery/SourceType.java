package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: joey
 * Date: 2017/9/7
 * Time: 19:37
 * 来源类型
 */

public enum SourceType {

    PC(1, "PC"),
    H5(2, "H5");
//    ANDROID(3, "Android"),
//    IOS(4, "IOS");
    

    private static Map<Integer, SourceType> map = new HashMap<>();

    static {
        for (SourceType oj : SourceType.values()) {
            map.put(oj.code , oj);
        }
    }

    private int code;
    private String msg;

    SourceType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    public static SourceType parse(int code) {
        return map.get(code);
    }

}
