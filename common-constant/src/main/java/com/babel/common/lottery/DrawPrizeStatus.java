package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: joey
 * Date: 2017/10/13
 * Time: 18:27
 * 开奖状态
 */
public enum DrawPrizeStatus {

    un_draw(0, "未开奖"),
    draw(1, "已开奖");

    private Integer code;
    private String msg;

    private static Map<Integer, DrawPrizeStatus> map =new HashMap<>();
    static {
        for (DrawPrizeStatus pj : DrawPrizeStatus.values()) {
            map.put(pj.code, pj);
        }
    }

    DrawPrizeStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

    public static DrawPrizeStatus parse(Integer code) {
        return map.get(code);
    }

}
