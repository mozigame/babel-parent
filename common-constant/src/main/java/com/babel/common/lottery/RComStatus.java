package com.babel.common.lottery;

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

    private int code;

    RComStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
