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
		,PLAT_ODDS_INFO("platOddsInfo")//平台商彩种赔率
		,LOTTERY_STATUS("lotteryStatus")//玩法状态或玩法信息
//		,LOTTERY("lotteryInfo")
		,PLAYS("plays")
		,PERIOD_DATA("periodData")	//奖期数据
		,PLAYS_ODDS("playsOdds")	//玩法赔率
		,PLAYS_DESCS("playsDescs")	//玩法描述
		,PLAYS_MANAGE("playsManage")//玩法开关
		,PLAYS_LIMIT("playsLimit")	//玩法限制
		,QUOTA_LIMIT("quotaLimit");	//投注限制
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
	 * 下注数据状态-平台商, {platInfoId, status}
	 * 处理位置：forseti:taskLoadService.reloadPlatInfo()
	 * 执行周期3分钟一次
	 */
	public final static String KEY_BET_STATUS_PLAT="_betStatusPlat";
	
	/**
	 * 数据-平台商信息，{platInfoId, platInfoPO}
	 * 处理位置：forseti:taskLoadService.reloadPlatInfo()
	 * 执行周期3分钟一次
	 */
	public final static String KEY_DATA_PLAT_INFO="_dataPlatInfo";
	/**
	 * 数据-平台商彩种及赔率
	 */
	public final static String KEY_DATA_PLAT_ODDS_INFO="_dataPlatOddsInfo";
	
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
	 * 下注数据状态-奖期数据
	 */
	public final static String KEY_BET_STATUS_PERIOD_DATA="_betStatusPeriodData";
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
	 * 下注数据状态-玩法-描述
	 */
	public final static String KEY_BET_STATUS_PLAYS_MANAGE="_betStatusPlaysManage";
	
	public final static String KEY_BET_STATUS_QUOTA_LIMIT="_betStatusQuotaLimit";
	
	
	
//	public final static String LOGIN_KEY_MEMBER_AC_TYPE="_memberAcTypeMap";
//	
//	public final static String LOGIN_KEY_MEMBER_STATUS="_memberStatusMap";
	
	/**
	 * 会员余额缓存，{memberId, Long}
	 * @key getKeyMemberBalAmount
	 * 处理位置：uaa, memberBalanceService.findOneByMemberBalance
	 * 处理方式：首次查询后缓存
	 */
	public final static String LOGIN_KEY_MEMBER_BAL_AMOUNT="_memberBalAmountMap";
	public final static String LOGIN_KEY_MEMBER_BAL_AMOUNT_LOCK="_memberBalAmountLock";
	
	public final static String LOGIN_KEY_PLAT_BAL_AMOUNT="_platBalAmountMap";
	public final static String LOGIN_KEY_PLAT_BAL_AMOUNT_LOCK="_platBalAmountLock";
	
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
	
	public final static String BET_USER_ORDER_MAP="_bet_userOrderMap";
	
	public final static String BET_USER_ORDER_PLAY="_bet_userOrderPlay";
	
	public final static String BET_USER_ORDER_PLAY_MAP="_bet_userOrderPlayMap";
	
//	/**
//	 * 存member的orderId
//	 */
//	public final static String BET_USER_ORDER_PDATE="_bet_userOrderPdate";
	
	/**
	 * 用户下注状态数据
	 */
	public final static String BET_USER_ORDER_STATUS_SET="_bet_userOrStatusSet";
	
//	/**
//	 * 用户追号状态数据
//	 */
//	public final static String BET_USER_CHASE_STATUS_SET="_bet_userOcStatusSet";//用户
	
	/**
	 * 存member的parentOrderId，即追号首条记录
	 */
//	public final static String BET_USER_CHASE_PDATE="_bet_userChasePdate";
//	public final static String BET_USER_CHASE_PDATE_FIRST="_bet_userChasePdateFirst";
	/**
	 * 存parentOrderId对应orderId关系
	 */
	public final static String BET_USER_CHASE_PDATE_DETAIL_MAP="_betUserChaseDetailMap";
	
	public final static String BET_INTERVAL="_betInval_";//股注限制
	
//	public final static String BET_USER_ORDER_PDATE_SET="_bet_userOrderPdateSet";
	
	/**
	 * 存parentOrderId
	 */
//	public final static String BET_USER_CHASE_PDATE_SET="_bet_userChasePdateSet";
	
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
	 * 类型map
	 * 下注成功缓存map
	 * @param lotteryId
	 * @param pcode
	 * @param playId
	 * @return
	 */
	public final static String getKeyUserOrderMap(Long memberId, Long lotteryId, Long pdate){
		return BET_USER_ORDER_MAP+"_"+memberId+"_"+lotteryId+"_"+pdate;
	}
	
	/**
	 * 类型：list
	 * 用户下注成功记录,list
	 * _bet_userOrderPlay_lotteryId_pcode_playId:userOrderList
	 * 有效期1小时
	 * @param pcode
	 * @return
	 */
	public final static String getKeyUserOrderPlay(Long lotteryId, Long pcode, Long playId){
		return BET_USER_ORDER_PLAY+"_"+lotteryId+"_"+pcode+"_"+playId;
	}
	
	/**
	 * 类型：set
	 * _bet_userOrderPlayMap_lotteryId_pcode=[_bet_userOrderPlay_lotteryId_pcode_playId,_bet_userOrderPlay_lotteryId_pcode_playId]
	 * @param lotteryId
	 * @param pcode
	 * @return
	 */
	public final static String getKeyUserOrderPlayMap(Long lotteryId, Long pcode){
		return BET_USER_ORDER_PLAY_MAP+"_"+lotteryId+"_"+pcode;
	}
	
//	/**
//	 * 类型：LIST
//	 * 存member的orderId
//	 * @param memberId
//	 * @param lotteryId
//	 * @param pdate
//	 * @return
//	 */
//	public final static String getKeyUserOrderPdate(Long memberId, Long lotteryId, Long pdate){
//		return BET_USER_ORDER_PDATE+"_"+memberId+"_"+lotteryId+"_"+pdate;
//	}
	
	/**
	 * 用户注单状态数据
	 * @param memberId
	 * @param lotteryId
	 * @param pdate
	 * @param orderStatus
	 * @return
	 */
	public final static String getKeyUserOrderStatusSet(Long memberId, Long lotteryId, Long pdate){
		return BET_USER_ORDER_STATUS_SET+"_"+memberId+"_"+lotteryId+"_"+pdate;
	}

	
//	/**
//	 * 类型：LIST
//	 * 存member的parentOrderId，即追号首条记录
//	 * @param memberId
//	 * @param lotteryId
//	 * @param pdate
//	 * @return
//	 */
//	public final static String getKeyUserChasePdate(Long memberId, Long lotteryId, Long pdate){
//		return BET_USER_CHASE_PDATE+"_"+memberId+"_"+lotteryId+"_"+pdate;
//	}
	
//	/**
//	 * 类型：LIST
//	 * 存member的parentOrderId，即追号首条记录
//	 * @param memberId
//	 * @param lotteryId
//	 * @param pdate
//	 * @return
//	 */
//	public final static String getKeyUserChaseFirstPdate(Long memberId, Long lotteryId, Long pdate){
//		return BET_USER_CHASE_PDATE_FIRST+"_"+memberId+"_"+lotteryId+"_"+pdate;
//	}
	
	/**
	 * 类型：MAP
	 * 存parentOrderId对应orderId关系
	 * @param memberId
	 * @param lotteryId
	 * @param pdate
	 * @return
	 */
	public final static String getKeyUserChasePdateDetailMap(Long lotteryId, Long pdate){
		return BET_USER_CHASE_PDATE_DETAIL_MAP+"_"+lotteryId+"_"+pdate;
	}
	
//	public final static String getKeyUserOrderPdateSet(Long memberId, Long lotteryId, Long pdate){
//		return BET_USER_ORDER_PDATE_SET+"_"+memberId+"_"+lotteryId+"_"+pdate;
//	}
//	
//	public final static String getKeyUserChasePdateSet(Long memberId, Long lotteryId, Long pdate){
//		return BET_USER_CHASE_PDATE_SET+"_"+memberId+"_"+lotteryId+"_"+pdate;
//	}
	
	public final static String getKeyMemberBalAmount(Integer acType){
		return LOGIN_KEY_MEMBER_BAL_AMOUNT+"_"+acType;
	}
	public final static String getKeyMemberBalAmountLock(Integer acType){
		return LOGIN_KEY_MEMBER_BAL_AMOUNT_LOCK+"_"+acType;
	}
	
	public final static String getKeyPlatBalAmount(Integer acType){
		return LOGIN_KEY_PLAT_BAL_AMOUNT+"_"+acType;
	}
	public final static String getKeyPlatBalAmountLock(Integer acType){
		return LOGIN_KEY_PLAT_BAL_AMOUNT_LOCK+"_"+acType;
	}
	
	public final static String getBetInterval(Long memberId, Long lotteryId){
		return BET_INTERVAL+"_"+lotteryId+"_"+memberId;
	}
}
