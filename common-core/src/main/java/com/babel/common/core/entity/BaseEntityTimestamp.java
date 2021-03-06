package com.babel.common.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * 时间戳
 * @author Justin
 *
 */
public abstract class BaseEntityTimestamp implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public void initCreate(){
		this.ifDel=0;
//		this.status=1;
		if(this.createTime==null)
			this.createTime=new Date().getTime();
		if(this.modifyTime==null)
			this.modifyTime=new Date().getTime();
	}
	
	public void initUpdate(){
		this.modifyTime=new Date().getTime();
	}
	
	/**
	 * 获取创建人，修改人的用户id
	 * @param list
	 * @return
	 */
	public static Set<Long> getOperaterIds(Collection<Object> list) {
		Set<Long> userIdSet=new HashSet<>();
		//获取第一个对象
		Object obj=null;
		if(!CollectionUtils.isEmpty(list)){
			obj=list.iterator().next();
		}
		else{
			return userIdSet;
		}
		
		if(obj instanceof BaseEntityTimestamp){//检查第一个对象是否继承BaseEntity
			BaseEntityTimestamp b=null;
			for(Object o:list){
				b=(BaseEntityTimestamp)o;
				if(b.getModifyUser()!=null)
					userIdSet.add(b.getModifyUser());
				if(b.getCreateUser()!=null)
					userIdSet.add(b.getCreateUser());
			}
		}
		return userIdSet;
	}
	
	/**
	 * 获取创建人，修改人的用户id
	 * @param list
	 * @return
	 */
	public static List<Long> getCidList(Collection list) {
		Collection<BaseEntityTimestamp> eList=list;
		List<Long> idList=new ArrayList<>();
		for(BaseEntityTimestamp o:eList){
			idList.add(o.getCid());
		}
		return idList;
	}
	
//	protected Long id;
//	private String code;
//	private String name;
	private Integer version = 0;
	private String currentUser;//用于从上下文件中获取用户信息
	private Long currentUserId;//用于从上下文件中获取用户Id
//	不是表中字段的属性必须加@Transient注解
	private String create_disp;
	private String modify_disp;
	
	/** 状态(if_active)0无效，1有效 */
//    private Integer status;

    /** 是否删除 */
    private Integer ifDel;

    /** 创建人 */
    private Long createUser;

    /** 创建时间 */
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Long createTime;

    /** 修改人 */
    private Long modifyUser;

    /** 修改时间 */
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Long modifyTime;
    
    
    public abstract Long getCid();
    
    public abstract void setCid(Long cid);

//	public Long getId() {
//		return getCid();
//	}
//
//	public void setId(Long cid) {
//		this.setCid(cid);
//	}

//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public Integer getStatus() {
//		return status;
//	}
//
//	public void setStatus(Integer status) {
//		this.status = status;
//	}

	public Integer getIfDel() {
		return ifDel;
	}

	public void setIfDel(Integer ifDel) {
		this.ifDel = ifDel;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(Long modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public Long getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(Long currentUserId) {
		this.currentUserId = currentUserId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCreate_disp() {
		return create_disp;
	}

	public void setCreate_disp(String create_disp) {
		this.create_disp = create_disp;
	}

	public String getModify_disp() {
		return modify_disp;
	}

	public void setModify_disp(String modify_disp) {
		this.modify_disp = modify_disp;
	}
    
    
	
}
