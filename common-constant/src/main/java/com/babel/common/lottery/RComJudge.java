package com.babel.common.lottery;

/**
 * User: joey
 * Date: 2018/1/16
 * Time: 12:49
 */
public enum RComJudge {

    no_stockade(1), //未达门槛
    no_standard(2), //未达标
    reach_standard(3);  //已达资格

    private int code;

    RComJudge(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
