package com.babel.common.lottery;

/**
 * User: joey
 * Date: 2018/1/3
 * Time: 20:25
 * 返水结果状态
 */
public enum CashBackResult {

    no_grant(0),    //未返还
    success(1); //成功

    private int code;

    CashBackResult(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
