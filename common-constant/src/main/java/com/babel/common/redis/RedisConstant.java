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
		,LOTTERY_STATUS("lotteryStatus")
//		,LOTTERY("lotteryInfo")
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
	 * hash 数据库id生成器key
	 */
	public static final String KEY_SEQUENCE_CID="redisSequenceId";
	
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
	 * 正在处理的下注数据
	 */
	public final static String BET_ORDER_DO_MAP="_bet_orderDoMap";
	/**
	 * 用户当期注单状态,map
	 */
	public final static String BET_ORDER_STATUS="_bet_orderStatus";
	
	public final static String BET_USER_ORDER_PLAY="_bet_userOrderPlay";
	
	public final static String BET_USER_ORDER_PLAY_MAP="_bet_userOrderPlayMap";
	
	public final static String BET_USER_ORDER_PCODE="_bet_userOrderPcode";
	
	/**
	 * 用户当期下注记录,list
	 * 有效期1小时
	 * @param pcode
	 * @return
	 */
	public final static String getKeyOperList(Long lotteryId, Long pcode){
		return BET_ORDER_LIST+"_"+lotteryId+"_"+pcode;
	}
	
	/**
	 * 正在处理map
	 * 有效期1小时
	 * @param pcode
	 * @return
	 */
	public final static String getKeyOperDoMap(Long lotteryId, Long pcode){
		return BET_ORDER_DO_MAP+"_"+lotteryId+"_"+pcode;
	}
	
	/**
	 * 用户当期注单状态 map
	 * 有效期1小时
	 * @param pcode
	 * @return
	 */
	public final static String getKeyOperStatus(Long lotteryId, Long pcode){
		return BET_ORDER_STATUS+"_"+lotteryId+"_"+pcode;
	}
	
	/**
	 * 用户下注成功记录,list
	 * 有效期1小时
	 * @param pcode
	 * @return
	 */
	public final static String getKeyUserOrder(Long lotteryId, Long pcode, Long playId){
		return BET_USER_ORDER_PLAY+"_"+lotteryId+"_"+pcode+"_"+playId;
	}
	
	public final static String getKeyUserOrderMap(Long lotteryId, Long pcode){
		return BET_USER_ORDER_PLAY+"_"+lotteryId+"_"+pcode;
	}
	
	public final static String getKeyUserOrderPcode(Long memberId, Long pdate, Long pcode){
		return BET_USER_ORDER_PCODE+"_"+memberId+"_"+pdate+"_"+pcode;
	}
}
