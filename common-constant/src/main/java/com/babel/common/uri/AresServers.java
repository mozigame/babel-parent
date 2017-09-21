package com.babel.common.uri;

/**
 * 所有的微服务名称
 */
public class AresServers {


    public static final String HERMES = "hermes";
    public static final String ARES_ACCOUNT = "account";
    public static final String VENUS = "venus";

    /**
     * hermes
     */
    public enum HERMES_URI {

        INCREMENT_BALANCE("/apis/balance/increment", "同步增减余额"),
        GET_BALANCE("/apis/balance/get", "查询会员余额"),
        CHARGE_ORDER("/apis/balance/charge/order", "下单扣款"),
        REFUND_ORDER("/apis/balance/refund/order", "撤单返款");

        private String uri;
        private String desc;

        HERMES_URI(String uri, String desc) {
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


    /**
     * venus 服务
     */
    public enum VENUS_URI {


        DRAW_PRIZE("/apis/lottery/draw/prize", "开奖");

        private String uri;
        private String desc;

        VENUS_URI(String uri, String desc) {
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



}
