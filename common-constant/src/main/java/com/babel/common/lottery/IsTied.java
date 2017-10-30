package com.babel.common.lottery;

/**
 * User: joey
 * Date: 2017/10/25
 * Time: 10:57
 * 是否是平局
 */
public enum IsTied {

    no(0),
    yes(1);

    private int code;

    IsTied(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
