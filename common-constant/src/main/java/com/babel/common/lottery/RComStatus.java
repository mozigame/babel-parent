package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: joey
 * Date: 2018/1/16
 * Time: 12:52
 */
public enum RComStatus {
    no_dispose(1),  //未处理
    suc_proc_r_com(2), //已退佣
    cancel(3),  //已取消
    hang_up(4); //已挂账

    private static Map<Integer, RComStatus> map = new HashMap<>();
    static {
        for (RComStatus obj : RComStatus.values()) {
            map.put(obj.getCode(), obj);
        }
    }
    private int code;

    RComStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static RComStatus parse(int code) {
        return map.get(code);
    }
}
