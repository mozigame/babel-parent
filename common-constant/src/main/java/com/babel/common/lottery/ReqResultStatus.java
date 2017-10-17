package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: joey
 * Date: 2017/10/17
 * Time: 20:53
 */
public enum ReqResultStatus {

    failed(0),
    success(1);

    private int status;

    private final static Map<Integer, ReqResultStatus> map = new HashMap<>();
    static {
        for (ReqResultStatus oj : ReqResultStatus.values()) {
            map.put(oj.status, oj);
        }
    }
    ReqResultStatus(Integer status) {
        this.status =status;
    }

    public int status() {
        return status;
    }

    public static ReqResultStatus parse(int code) {
        return map.get(code);
    }
}
