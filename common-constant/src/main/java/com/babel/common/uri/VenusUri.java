package com.babel.common.uri;

/**
 * venus 服务
 */
public enum VenusUri {


    DRAW_PRIZE("/apis/lottery/draw/prize", "开奖");


    private String uri;
    private String desc;

    VenusUri(String uri, String desc) {
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
