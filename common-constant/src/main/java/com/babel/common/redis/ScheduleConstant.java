package com.babel.common.redis;

/**
 * schedule常量
 * @author Justin
 *
 */
public class ScheduleConstant {
	/**
     * 处理redis数据的间隔时间
     */
    public static final int cacheFlushRate = 180000;   //3分钟
    /**
     * 订单异步保存间隔
     */
    public static final int cacheSaveBetOrderAsync= 300;   //0.3秒
  

}
