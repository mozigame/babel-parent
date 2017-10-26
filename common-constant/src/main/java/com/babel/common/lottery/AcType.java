package com.babel.common.lottery;

/**
 * User: joey
 * Date: 2017/10/26
 * Time: 16:05
 */
public enum AcType {

    real(1, "真实账号"),
    trial(2, "试玩账号");

    private int code;
    private String msg;



    AcType(int code, String msg) {
        this.code = code;
        this.msg =msg;
    }

    public int getCode() {
        return code;
    }
}
