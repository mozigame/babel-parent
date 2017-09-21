package com.babel.common.uri;

/**
 * hermes 服务
 */
public enum HermesUri {


    INCREMENT_BALANCE("/apis/balance/increment", "同步增减余额"),
    GET_BALANCE("/apis/balance/get", "查询会员余额"),
    CHARGE_ORDER("/apis/balance/charge/order", "下单扣款"),
    REFUND_ORDER("/apis/balance/refund/order", "撤单返款");



    private String uri;
    private String desc;

    HermesUri(String uri, String desc) {
        this.uri= uri;
        this.desc = desc;
    }

    public String uri() {
        return uri;
    }

    public String desc() {
        return desc;
    }
}
