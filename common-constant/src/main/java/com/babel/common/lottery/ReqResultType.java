package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: joey
 * Date: 2017/10/17
 * Time: 20:53
 */
public enum ReqResultType {

    failed(0),
    success(1);

    private int status;

    private final static Map<Integer, ReqResultType> map = new HashMap<>();
    static {
        for (ReqResultType oj : ReqResultType.values()) {
            map.put(oj.status, oj);
        }
    }
    ReqResultType(Integer status) {
        this.status =status;
    }

    public int status() {
        return status;
    }

    public static ReqResultType parse(int code) {
        return map.get(code);
    }
}
