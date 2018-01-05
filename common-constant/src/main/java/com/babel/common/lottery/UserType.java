package com.babel.common.lottery;

/**
 * User: joey
 * Date: 2018/1/4
 * Time: 17:19
 * 用户类型
 */
public enum UserType {

    member(1),  //会员
    agent(2),   //代理
    plat(3);    //平台商

    private int code;

    UserType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static UserType parse(int code) {
        for (UserType type : UserType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
