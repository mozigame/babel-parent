package com.babel.common.lottery;

/**
 * User: joey
 * Date: 2018/1/3
 * Time: 17:36
 * 会员返水执行状态
 */
public enum CashBackProcStatus {

    init(0),    // 未执行
    processing(10), //执行中
    processed(100); //执行完成

    private int code;

    CashBackProcStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
