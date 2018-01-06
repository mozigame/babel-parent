package com.babel.common.lottery;

/**
 * User: joey
 * Date: 2018/1/3
 * Time: 14:32
 * 自动返水开关
 */
public enum CashBackStatus {

    stop(0),    //停止
    running(1); //运行

    private int code;


    CashBackStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CashBackStatus parse(int code) {
        for (CashBackStatus status : CashBackStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
