package com.babel.common.redis;

/**
 * REDIS缓存对应的key
 * @author Justin
 *
 */
public class RedisConstant {
	/**
	 * 最大修改时间
	 */
	public final static String KEY_DATA_MAX_MODIFY_TIME="_dataMaxModifyTime";
	public static enum dataMaxModifyDate{
		PLAT_INFO("platInfo")
		,LOTTERY("lotteryInfo")
		,PLAYS("plays")
		,PLAYS_ODDS("playsOdds")
		,PLAYS_DESCS("playsDescs")
		,PLAYS_LIMIT("playsLimit");
		private String code;
		private dataMaxModifyDate(String code){
			this.code=code;
		}
		public String getCode(){
			return this.code;
		}
		
	}
	/**
	 * 下注数据状态-平台商
	 */
	public final static String KEY_BET_STATUS_PLAT="_betStatusPlat";
	
	/**
	 * 数据-平台商信息
	 */
	public final static String KEY_DATA_PLAT_INFO="_dataPlatInfo";
	
	/**
	 * 下注数据状态-彩种
	 */
	public final static String KEY_BET_STATUS_LOTTERY="_betStatusLottery";
	/**
	 * 数据-彩种信息
	 */
	public final static String KEY_DATA_LOTTERY_INFO="_dataLotteryInfo";
	/**
	 * 下注数据状态-玩法
	 */
	public final static String KEY_BET_STATUS_PLAYS="_betStatusPlays";
	/**
	 * 下注数据状态-玩法-赔率
	 */
	public final static String KEY_BET_STATUS_PLAYS_ODDS="_betStatusPlaysOdds";
	/**
	 * 下注数据状态-玩法-限制
	 */
	public final static String KEY_BET_STATUS_PLAYS_LIMIT="_betStatusPlaysLimit";
	/**
	 * 下注数据状态-玩法-描述
	 */
	public final static String KEY_BET_STATUS_PLAYS_DESCS="_betStatusPlaysDescs";
	
	/**
	 * 平台商id缓存
	 */
	public final static String LOGIN_KEY_PLAT_ID="_platIdMap";
	
	/**
	 * 登入用户login的id
	 */
	public final static String LOGIN_KEY_MEMBER_ID="_memberIdMap";
	
	public final static String LOGIN_KEY_MEMBER_STATUS="_memberStatusMap";
	
	/**
	 * 用户当期下注记录,list
	 */
	public final static String BET_ORDER_LIST="_bet_orderList";
	/**
	 * 用户当期注单状态,map
	 */
	public final static String BET_ORDER_STATUS="_bet_orderStatus";
	
	public final static String BET_USER_ORDER="_bet_userOrder";
	
	/**
	 * 用户当期下注记录,list
	 * 有效期1小时
	 * @param pcode
	 * @return
	 */
	public final static String getKeyOperList(Long lotteryId, Long pcode){
		return BET_ORDER_LIST+"_"+pcode;
	}
	
	/**
	 * 用户当期注单状态 map
	 * 有效期1小时
	 * @param pcode
	 * @return
	 */
	public final static String getKeyOperStatus(Long lotteryId, Long pcode){
		return BET_ORDER_STATUS+"_"+pcode;
	}
	
	/**
	 * 用户下注成功记录,list
	 * 有效期1小时
	 * @param pcode
	 * @return
	 */
	public final static String getKeyUserOrder(Long lotteryId, Long pcode){
		return BET_USER_ORDER+"_"+pcode;
	}
}
