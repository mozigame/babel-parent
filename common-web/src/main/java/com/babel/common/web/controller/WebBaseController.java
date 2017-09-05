package com.babel.common.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.babel.common.core.data.IUser;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.entity.BaseEntity;
import com.babel.common.core.entity.BaseEntityTimestamp;
import com.babel.common.core.service.IUserInfoService;
import com.babel.common.core.util.CommCollections;
import com.babel.common.core.util.SpringContextUtil;
import com.babel.common.web.context.AppContext;


public abstract class WebBaseController extends BaseController {
	private static Logger logger = Logger.getLogger(WebBaseController.class);


	public Map<String, Object> getEnvMap(){
		return AppContext.getEnvMap();
	}
//	
//	public Map<String, Object> getUserEnvMap(){
//		return SysconfigUsers.getUserEnvMap(this.getCurrentUserId());
//	}
	
//	@Inject("userBaseService")
//	@Autowired
//	@Qualifier("userInfoService") 
	private IUserInfoService userInfoService;
	
	/**
	 * request参数转map
	 * @return
	 */
	public Map<String, String> getParamMap(){
		Map<String, String> paramMap=new HashMap<String, String>();
		Map<String, String[]> pMap=this.getRequest().getParameterMap();
    	Set<String> keys=pMap.keySet();
    	for(String key:keys){
    		paramMap.put(key, (String)CommCollections.getFirst(pMap.get(key)));
    	}
    	return paramMap;
	}
	
	protected void initDisp(Collection list){
		if(list!=null){
			if(userInfoService==null && SpringContextUtil.containsBean("userBaseService")){
				userInfoService=(IUserInfoService)SpringContextUtil.getBean("userBaseService");
			}
			if(userInfoService==null){
				logger.warn("userInfoService not found");
				return;
			}
		
			
			Set<Long> userIdSet = BaseEntity.getOperaterIds(list);
			if(userIdSet.isEmpty()){
				return;
			}
			List<Long> idList=new ArrayList<>();
			idList.addAll(userIdSet);
			RetResult<IUser> userRet=userInfoService.findUserByIdList(idList);
			
			if(userRet!=null){
				if(userRet.isSuccess()){
					initOperaterName(list, userRet);
				}
				else{
					logger.warn(userRet.getMsgBody());
				}
			}
		}
		
	}
	
//	public abstract RetResult<IUser> findUserByIdList(List<Long> idList);
	
	/**
	 * 用户id转用户名称处理
	 * @param list
	 * @param userRet
	 */
	private void initOperaterName(Collection<Object> list, RetResult<IUser> userRet) {
		Collection<IUser> userList=userRet.getDataList();
		if(!CollectionUtils.isEmpty(list)){
		    Object object=list.iterator().next();
		    if(object instanceof BaseEntityTimestamp){
		        BaseEntityTimestamp b=null;
		        for(Object o:list){
		            b=(BaseEntityTimestamp)o;
		            for(IUser u:userList){
		                if(b.getCreateUser()!=null && b.getCreateUser().longValue()==u.getCid().longValue()){
		                    b.setCreate_disp(u.getName());
		                }
		                if(b.getModifyUser()!=null && b.getModifyUser().longValue()==u.getCid().longValue()){
		                    b.setModify_disp(u.getName());
		                }
		            }
		        }
		        return;
		    }
		}
		BaseEntity b=null;
		for(Object o:list){
			b=(BaseEntity)o;
			for(IUser u:userList){
				if(b.getCreateUser()!=null && b.getCreateUser().longValue()==u.getCid().longValue()){
					b.setCreate_disp(u.getName());
				}
				if(b.getModifyUser()!=null && b.getModifyUser().longValue()==u.getCid().longValue()){
					b.setModify_disp(u.getName());
				}
			}
		}
	}

	@InitBinder    
	   protected void initBinder(WebDataBinder binder) {    
//	       binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
	       binder.registerCustomEditor(Date.class, new DateEditor());
//	/        binder.registerCustomEditor(int.class, new CustomNumberEditor(int.class, true));    
//	       binder.registerCustomEditor(int.class, new IntegerEditor());    
//	/        binder.registerCustomEditor(long.class, new CustomNumberEditor(long.class, true));  
//	       binder.registerCustomEditor(long.class, new LongEditor());    
//	       binder.registerCustomEditor(double.class, new DoubleEditor());    
//	       binder.registerCustomEditor(float.class, new FloatEditor());    
	   }   

}
